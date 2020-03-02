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
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*no logic needed. just sends the view page*/
        if (request.getSession(false) == null) {
            response.sendRedirect("/messenjer");
        } else {
            request.getRequestDispatcher("/chatPage.jsp").forward(request, response);
        }
    }
}
