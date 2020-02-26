package org.bihe.servlets;

import org.bihe.daoimpl.UserDoaImpl;
import org.bihe.interfaces.UserDAO;
import org.bihe.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDoa = new UserDoaImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO authenticate users here
        System.out.println("I am in the login page");
        String username =  request.getParameter("username");
        String password =  request.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        User user = this.userDoa.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            request.getRequestDispatcher("onlineUsers").include(request, response);
            if ((boolean) request.getAttribute("valid")) {
                HttpSession session = request.getSession(true);
                session.setAttribute("username", username);
                session.setMaxInactiveInterval(10 * 60);
                response.sendRedirect("chatPage");


            } else { // the user is already logged in and have another session
                System.out.println(" already logged in");

                request.setAttribute("notif", "This user is already logged in");
                request.getRequestDispatcher("signin").forward(request, response);
            }
        } else { // username or password is incorrect
            System.out.println(" incorrect");

            request.setAttribute("notif", "Username or Password is incorrect");
            request.getRequestDispatcher("signin").forward(request, response);
        }
//TODO SHOW THE message in notif in log in and do the same for sign up

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("signin");
    }
}
