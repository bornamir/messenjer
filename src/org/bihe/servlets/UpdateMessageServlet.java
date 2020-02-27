package org.bihe.servlets;


import org.bihe.daoimpl.MessageDoaImpl;
import org.bihe.interfaces.MessageDAO;
import org.bihe.models.Message;
import org.javatuples.Pair;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@WebServlet(name = "UpdateMessageServlet", urlPatterns = "/updateMessage", asyncSupported = true)
public class UpdateMessageServlet extends HttpServlet {
    private static Map<Pair<String, String>, AsyncContext> asyncContextMap = new Hashtable<>();
    private MessageDAO messageDAO = new MessageDoaImpl();

    // for sending messages, saving them in database and sending them to the async SSE request.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        Message message = new Message();
        messageDAO.saveMessage();
        String message = request.getParameter("message");
        String user = request.getParameter("user");
        System.out.println(message + user);
        System.out.println(request.getParameterMap().toString() );

    }

    // for receiving async SSE request to listen to new messages
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession(false) == null) {
            response.sendRedirect("/messenjer");
        }

        //initialize for server-sent events
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        String listeningUser = (String) request.getSession(false).getAttribute("username");
        String sendingUser = request.getParameter("user");
        if (sendingUser == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        final Pair<String, String> userPair = Pair.with(listeningUser, sendingUser);

        //to clear threads and allow for asynchronous execution
        final AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(0);
        System.out.println(Pair.with(1, 1) == Pair.with(1, 1));
        //add context to list for later use
        if (asyncContextMap == null || asyncContextMap.isEmpty() || !asyncContextMap.containsKey(userPair)) {
            if (asyncContextMap == null) {
                asyncContextMap = new Hashtable<Pair<String, String>, AsyncContext>();
            }
            asyncContextMap.put(userPair, asyncContext);
        }
    }

}
