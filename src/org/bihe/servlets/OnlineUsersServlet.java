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
    private List<AsyncContext> contexts = new LinkedList<>();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<AsyncContext> asyncContexts = new ArrayList<>(this.contexts);
        this.contexts.clear();

        ServletContext servletContext = request.getServletContext();
        List<HttpSession> onlineUsersSessions;
        onlineUsersSessions = (LinkedList<HttpSession>) servletContext.getAttribute("onlineUsersSessions");
        StringBuilder htmlMessage = new StringBuilder("<p><b> Online users are :</b><br/>");
        for (HttpSession session : onlineUsersSessions) {
            String user = (String) session.getAttribute("username");
            htmlMessage.append("<p>").append(user).append("</p>");
        }

//        String name = request.getParameter("name");
//        String message = request.getParameter("message");
//        ServletContext sc = request.getServletContext();
//        if (sc.getAttribute("messages") == null) {
//            sc.setAttribute("messages", htmlMessage);
//        } else {
//            String currentMessages = (String) sc.getAttribute("messages");
//            sc.setAttribute("messages", htmlMessage + currentMessages);
//        }

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final AsyncContext asyncOnlineContext = request.startAsync(request, response);
        asyncOnlineContext.setTimeout(10 * 60 * 1000);
        contexts.add(asyncOnlineContext);
    }
}
