package org.bihe.servlets;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "OnlineUsersServlet", urlPatterns = "/onlineUsers", asyncSupported = true)

public class OnlineUsersServlet extends HttpServlet {
    private static final List<AsyncContext> CONTEXTS = new LinkedList<>();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        sendUpdatedOnlineUsers(servletContext);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession(false) == null) {
            response.sendError(400);
        } else {
            final AsyncContext asyncOnlineContext = request.startAsync(request, response);
//            asyncOnlineContext.setTimeout(10 * 1000);
            CONTEXTS.add(asyncOnlineContext);
        }

    }

    public static void sendUpdatedOnlineUsers(ServletContext servletContext) {
        List<AsyncContext> asyncContexts = new ArrayList<>(CONTEXTS);
        CONTEXTS.clear();

        // getting all the usernames and make a list of them
        List<HttpSession> onlineUsersSessions;
        onlineUsersSessions = (LinkedList<HttpSession>) servletContext.getAttribute("onlineUsersSessions");
        StringBuilder htmlMessage = new StringBuilder("<p><b> Online users are :</b><br/>");
        for (HttpSession session : onlineUsersSessions) {
            String user = (String) session.getAttribute("username");
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

}
