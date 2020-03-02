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
        String username =  request.getParameter("username");
        String password =  request.getParameter("password");

        User user = this.userDoa.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            /* checking if user is already online. Then adding it to the online users list
             and send the updated list to others. this is done by a post req to onlineUsers servlet*/
            request.getRequestDispatcher("onlineUsers").include(request, response);
            if ((boolean) request.getAttribute("valid")) {

                /*session is only created here. */
                HttpSession session = request.getSession(true);
                session.setAttribute("username", username);
                session.setMaxInactiveInterval(10 * 60);
                response.sendRedirect("chatPage");


            } else { // the user is already logged in and have another session
                request.setAttribute("notif", "This user is already logged in");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } else { // username or password is incorrect

            request.setAttribute("notif", "Username or Password is incorrect");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
