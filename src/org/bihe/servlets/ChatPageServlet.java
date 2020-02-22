package org.bihe.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChatPageServlet", urlPatterns = "/chatPage")
public class ChatPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        String username = (String) session.getAttribute("username");
        //TODO chat logic here
//
        if (request.getSession(false) == null) {
            response.sendRedirect("/messenjer");
        } else {
            String username = (String) request.getSession(false).getAttribute("username");
            request.getRequestDispatcher("/chatPage.jsp").forward(request, response);
        }
    }
}
