package org.bihe.servlets;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "OnlineUsersServlet", urlPatterns = "/onlineUsers", asyncSupported = true)

public class OnlineUsersServlet extends HttpServlet {
    private static final List<AsyncContext> CONTEXTS = new LinkedList<>();
    private static final List<String> onlineUsers = new LinkedList<>();

    ServletContext servletContext = null;
//    private static List<AsyncContext> onlineUsersContexts = new LinkedList<>();

    // New user is trying to logged in, should add new online user and send to all
    // if the user is not online already!
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        if (username != null && !onlineUsers.contains(username)) {
            request.setAttribute("valid",true);
            onlineUsers.add(username);
            sendUpdatedOnlineUsers();

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
            try (PrintWriter writer = response.getWriter()) {
                String message = getOnlineUsersToHtml();
                writer.println(message);
                writer.flush();
            }
        }

    }

    public static void sendUpdatedOnlineUsers() {
        List<AsyncContext> asyncContexts = new ArrayList<>(CONTEXTS);
        CONTEXTS.clear();
        String message = getOnlineUsersToHtml();

        for (AsyncContext asyncContext : asyncContexts) {
            try (PrintWriter writer = asyncContext.getResponse().getWriter()) {
                writer.println(message);
                writer.flush();
                asyncContext.complete();
            } catch (Exception ignored) {
            }
        }
    }

    private static String getOnlineUsersToHtml() {
        // getting all the usernames and make a list of them
        StringBuilder htmlMessage = new StringBuilder("<div id=\"onlineUsersList\"><p><b> Online users are :</b></p><br/>");
        for (String user : onlineUsers) {
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
