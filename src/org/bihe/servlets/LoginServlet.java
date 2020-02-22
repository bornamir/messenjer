package org.bihe.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO authenticate users here
        System.out.println("I am in the login page");


        HttpSession session = request.getSession(true);
        String username = request.getParameter("username");
        session.setAttribute("username", username);
        //TODO if login is successful, update online users

        request.getRequestDispatcher("onlineUsers").include(request,response);


        response.sendRedirect("chatPage");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("i am in the  GET");
    }
}
