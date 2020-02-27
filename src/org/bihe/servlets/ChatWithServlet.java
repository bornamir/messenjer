package org.bihe.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChatWithServlet",urlPatterns = "/chatWith")
public class ChatWithServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) == null) {
            response.sendRedirect("/messenjer");
        } else if (request.getParameter("user") != null) {


            String username = (String) request.getSession(false).getAttribute("username");
            String userChatWith = request.getParameter("user");

            request.getRequestDispatcher("/chatWith.jsp").forward(request, response);
        } else { // chat with user is not defined, hence redirect to chatpage for selecting user
            response.sendRedirect("/messenjer/chatPage");

        }
    }
}
