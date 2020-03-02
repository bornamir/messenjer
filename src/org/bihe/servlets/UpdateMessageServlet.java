package org.bihe.servlets;


import com.google.gson.Gson;
import org.bihe.daoimpl.MessageDoaImpl;
import org.bihe.daoimpl.UserDoaImpl;
import org.bihe.interfaces.MessageDAO;
import org.bihe.interfaces.UserDAO;
import org.bihe.models.Message;
import org.bihe.models.User;
import org.bihe.sevices.MessageForSending;
import org.javatuples.Pair;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UpdateMessageServlet", urlPatterns = "/updateMessage", asyncSupported = true)
public class UpdateMessageServlet extends HttpServlet {
    private static Map<Pair<String, String>, AsyncContext> asyncContextMap = new Hashtable<>();
    private MessageDAO messageDAO = new MessageDoaImpl();
    private UserDAO userDAO = new UserDoaImpl();

    // for sending messages, saving them in database and sending them to the async SSE request.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession(false) == null) { // user is not logged in
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }


        String senderUsername = (String) request.getSession(false).getAttribute("username");
        String receiverUsername = request.getParameter("receiver");
        String messageText = request.getParameter("message");
        String messageType = request.getParameter("type");
        User senderUser = userDAO.getUserByUsername(senderUsername);
        User receiverUser = userDAO.getUserByUsername(receiverUsername);
        if (senderUser == null | receiverUser == null) { // User is not logged in or the receiver does not exist
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            ZonedDateTime zn = ZonedDateTime.now();
            Message message = new Message(messageText, senderUser, receiverUser, zn, messageType);

            int httpStatus = sendTextMessage(message);

            if (httpStatus == 200 | httpStatus == 202) {
                response.setStatus(httpStatus);
                try (ServletOutputStream out = response.getOutputStream()) {
                    String preparedMessage = message.toJsonSendingFormat();
                    out.println(preparedMessage);
                    out.flush();
                    return;
                }
            }
            response.sendError(httpStatus);
        }

    }


    private int sendTextMessage(Message message) {
        if (messageDAO.saveMessage(message)) {
            Pair<String, String> pair =
                    new Pair<>(message.getReceiverUser().getUsername(), message.getSenderUser().getUsername());
            if (asyncContextMap.containsKey(pair)) {
                AsyncContext asyncContext = asyncContextMap.get(pair);
                try {
                    ServletOutputStream out = asyncContext.getResponse().getOutputStream();
                    String preparedMessage = message.toJsonSendingFormat();
                    System.out.println(preparedMessage);
                    out.println("event: textMessage\ndata: " + preparedMessage + "\n\n");
                    out.flush();
                    return (HttpServletResponse.SC_OK);
                } catch (Exception e) {
                    asyncContext = asyncContextMap.remove(pair);
                    asyncContext.complete();
                    return HttpServletResponse.SC_ACCEPTED; // connection to the receiver is lost
                }

            } else { // The pair does not exist ( Probably the receiver is not logged in)
                return (HttpServletResponse.SC_ACCEPTED);
            }
        } else {  // Message could not be saved
            return (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // for receiving async SSE request to listen to new messages
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession(false) == null) { // user is not logged in
            response.sendRedirect("/messenjer");
        }
        //initialize for server-sent events
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        String listeningUser = (String) request.getSession(false).getAttribute("username");
        String sendingUser = request.getParameter("receiver");
        if (sendingUser == null) { // the receiver does not exist
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        String sendAllChat = request.getParameter("all");
        if (sendAllChat == null) { // An async request for receiving message updates.

            final Pair<String, String> userPair = Pair.with(listeningUser, sendingUser);

            //to clear threads and allow for asynchronous execution
            final AsyncContext asyncContext = request.startAsync(request, response);
            asyncContext.setTimeout(10 * 1000); // Inactive user or left the page

            try {
                ServletOutputStream out = asyncContext.getResponse().getOutputStream();
                out.println("retry:500\nevent: retryTime\ndata: retry time has been sent " + "\n\n");
                out.flush();
            } catch (Exception e) {
                asyncContext.complete();
            }

            asyncContext.addListener(new AsyncListener() {
                @Override
                public void onComplete(AsyncEvent asyncEvent) throws IOException {

                }

                @Override
                public void onTimeout(AsyncEvent asyncEvent) throws IOException {
                    HttpServletResponse response = (HttpServletResponse) asyncEvent.getAsyncContext().getResponse();
//                    response.sendError(205);
                    asyncContextMap.remove(userPair);
                    System.out.println("Async Context Timeout");
                    asyncEvent.getAsyncContext().complete();

                }

                @Override
                public void onError(AsyncEvent asyncEvent) throws IOException {
                    HttpServletResponse response = (HttpServletResponse) asyncEvent.getAsyncContext().getResponse();
                    response.sendError(400);
                    asyncContextMap.remove(userPair);
                    System.out.println("Erro in async context happend");
                    asyncEvent.getAsyncContext().complete();
                }

                @Override
                public void onStartAsync(AsyncEvent asyncEvent) throws IOException {

                }
            });


            //add context to list for later use
            if (asyncContextMap == null || asyncContextMap.isEmpty() || !asyncContextMap.containsKey(userPair)) {
                if (asyncContextMap == null) {
                    asyncContextMap = new Hashtable<Pair<String, String>, AsyncContext>();
                }
                asyncContextMap.put(userPair, asyncContext);
            }
        } else { //first get req, needs all the chats
            List<Message> messages = messageDAO.getAllMessagesBetweenTwoUsers(listeningUser, sendingUser);
            List<MessageForSending> allMessages = new LinkedList<>();
            for (Message message : messages) {
                MessageForSending sendMessage = new MessageForSending(message);
                allMessages.add(sendMessage);
            }
            Gson gson = new Gson();
            String sendAllMessages = gson.toJson(allMessages);
            try (ServletOutputStream out = response.getOutputStream()) {
                out.println(sendAllMessages);
                out.flush();
                response.sendError(HttpServletResponse.SC_OK);
            } catch (Exception ignored) {

            }
        }
    }

}
