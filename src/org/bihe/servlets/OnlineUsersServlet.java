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
        String username = (String) request.getParameter("username");
        if (username != null && !onlineUsers.contains(username)) {
            request.setAttribute("valid",true);
            onlineUsers.add((String) request.getAttribute("username"));
            ServletContext servletContext = request.getServletContext();
            sendUpdatedOnlineUsers(servletContext);

        } else {
            request.setAttribute("valid",false);
        }


    }

    // A user is reached to ChatPage and wants to get the online users updates.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession(false) == null) {
            response.sendError(400);
        } else {
            final AsyncContext asyncOnlineContext = request.startAsync(request, response);
            asyncOnlineContext.setTimeout(15 * 10 * 1000);
            CONTEXTS.add(asyncOnlineContext);
        }

    }

    public static void sendUpdatedOnlineUsers(ServletContext servletContext) {
        List<AsyncContext> asyncContexts = new ArrayList<>(CONTEXTS);
        CONTEXTS.clear();

        // getting all the usernames and make a list of them
        StringBuilder htmlMessage = new StringBuilder("<p><b> Online users are :</b><br/>");
        for (String user : onlineUsers) {
            htmlMessage.append("<p>").append(user).append("</p>");
        }

        // sending the usernames list to all async requests
        String message = htmlMessage.toString();
        for (AsyncContext asyncContext : asyncContexts) {
            try (PrintWriter writer = asyncContext.getResponse().getWriter()) {
                writer.println(message);
                writer.flush();
                asyncContext.complete();
            } catch (Exception ignored) {
            }
        }
    }

    public static boolean removeOnlineUserFromList(String username) {
        return onlineUsers.remove(username);
    }

}
