package org.bihe.servlets;

import org.bihe.daoimpl.UserDoaImpl;
import org.bihe.interfaces.UserDAO;
import org.bihe.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "SignUpServlet", urlPatterns = "/signup")
public class SignUpServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        if (password1.equals(password2)) {
            List<String> users = (LinkedList<String>) request.getServletContext().getAttribute("users");
            if (!users.contains(username)) {
                UserDAO userDAO = new UserDoaImpl();
                String firstname = request.getParameter("firstName");
                String lastname = request.getParameter("LastName");
                String email = request.getParameter("email");
                User user = new User(firstname, lastname, username, password1, email);
                if (userDAO.saveUser(user)) {
                    // adding new user to the users list and set attribute
                    users.add(username);
                    request.getServletContext().setAttribute("users",users);

                    request.setAttribute("notif", "You have successfully registered. Now you can log in.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else { // saving to the database failed
                    request.setAttribute("notif", "Somethin went wrong. Try again or contact support");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }

            } else {  // the username is taken
                request.setAttribute("notif", "Username is taken");
                request.getRequestDispatcher("register.jsp").forward(request, response);

            }

        } else { // passwords do not match
            request.setAttribute("notif", "Passwords do not match");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request,response);
    }
}
