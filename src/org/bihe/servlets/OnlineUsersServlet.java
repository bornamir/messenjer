package org.bihe.servlets;

import com.google.gson.Gson;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "OnlineUsersServlet", urlPatterns = "/onlineUsers", asyncSupported = true)

public class OnlineUsersServlet extends HttpServlet {
    private static final List<AsyncContext> CONTEXTS = new LinkedList<>();
    private static final List<String> onlineUsers = new LinkedList<>();

//    private static ServletContext servletContext = getServletContext();
//    private static List<AsyncContext> onlineUsersContexts = new LinkedList<>();

    // New user is trying to logged in, should add new online user and send to all
    // if the user is not online already!
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        if (username != null && !onlineUsers.contains(username)) {
            request.setAttribute("valid",true);
            onlineUsers.add(username);
            sendUpdatedUsersList(request.getServletContext());

        } else {
            request.setAttribute("valid",false);
        }


    }

    // A user is reached to ChatPage and wants to get the online users updates.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession(false) == null) {
            response.sendError(400);
        } else if ("1".equals(request.getParameter("updated"))) {
            final AsyncContext asyncOnlineContext = request.startAsync(request, response);
            asyncOnlineContext.setTimeout(15 * 10 * 1000);
            CONTEXTS.add(asyncOnlineContext);
        } else {
            try (ServletOutputStream out = response.getOutputStream()) {
                List<String> users = (LinkedList<String>) request.getServletContext().getAttribute("users");
                Map<String, List<String>> messageMap = new TreeMap<>();
                messageMap.put("users", users);
                messageMap.put("onlineUsers", onlineUsers);
                Gson gson = new Gson();
                String jsonMessage = gson.toJson(messageMap);
                System.out.println(jsonMessage);

//                String message = getUserListToHtml(onlineUsers, users);
                response.setContentType("application/json;charset=UTF-8");
                out.print(jsonMessage);
                out.flush();
            }
        }

    }

    public static void sendUpdatedUsersList(ServletContext servletContext) {
        List<AsyncContext> asyncContexts = new ArrayList<>(CONTEXTS);
        CONTEXTS.clear();
        List<String> users = (LinkedList<String>) servletContext.getAttribute("users");

        String message = getUserListToHtml(onlineUsers,users);
        for (AsyncContext asyncContext : asyncContexts) {
            try (PrintWriter writer = asyncContext.getResponse().getWriter()) {
                writer.println(message);
                writer.flush();
                asyncContext.complete();
            } catch (Exception ignored) {
            }
        }
    }

    private static String getUserListToHtml(List<String> onlineUsers, List<String> users) {
        // getting all the usernames and make a list of them
        StringBuilder htmlMessage = new StringBuilder("<div id=\"onlineUsersList\"><p><b> Online users are :</b></p>");
        for (String user : onlineUsers) {
            htmlMessage.append("<p>").append(user).append("</p>");
        }
        htmlMessage.append("</div> <div id=\"allUsers\"><p><b> All users:</b></p>");
        for (String user : users) {
            htmlMessage.append("<p>").append(user).append("</p>");
        }
        htmlMessage.append("</div>");
        // sending the usernames list to all async requests
        return htmlMessage.toString();
    }

    public static boolean removeOnlineUserFromList(String username) {
        return onlineUsers.remove(username);
    }

}
