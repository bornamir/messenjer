package org.bihe.listeners;

import org.bihe.daoimpl.UserDoaImpl;
import org.bihe.interfaces.UserDAO;
import org.bihe.servlets.OnlineUsersServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

@WebListener()
public class SessionListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {


    // Public constructor is required by servlet spec
    public SessionListener() {

    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed).
         You can initialize servlet context related data here.
      */

      /*get all the users list from database and add it to the app context
      * So that other servlets has access to all the users list and
      * do not execute query on database every time*/
        List<String> users;
        UserDAO userDAO = new UserDoaImpl();
        users = userDAO.getAllUsernames();
        sce.getServletContext().setAttribute("users", users);

    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */

    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. Remove user from onlineusers list in OnlineUsersServlet.
        * Then send the update list to others.*/
        ServletContext servletContext = se.getSession().getServletContext();
        if (OnlineUsersServlet.removeOnlineUserFromList(
                (String) se.getSession().getAttribute("username"))) {
            OnlineUsersServlet.sendUpdatedUsersList(se.getSession().getServletContext());
        }
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attribute
         is replaced in a session.
      */
    }


}
