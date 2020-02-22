package org.bihe.listeners;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.LinkedList;
import java.util.List;

@WebListener()
public class SessionListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    ServletContext servletContext = null;
    private static List<HttpSession> onlineUsersSessions = new LinkedList<>();
    private static List<AsyncContext> onlineUsersContexts = new LinkedList<>();


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
        servletContext = sce.getServletContext();
        servletContext.setAttribute("onlineUsersContext", onlineUsersContexts);
        servletContext.setAttribute("onlineUsersSessions", onlineUsersSessions);

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

        /* adding the session to the session list*/
        servletContext = se.getSession().getServletContext();
        onlineUsersSessions = (LinkedList<HttpSession>) servletContext.getAttribute("onlineUsersSessions");
        onlineUsersSessions.add(se.getSession());
        servletContext.setAttribute("onlineUsersSessions", onlineUsersSessions);


    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
        servletContext = se.getSession().getServletContext();
        onlineUsersSessions = (LinkedList<HttpSession>) servletContext.getAttribute("onlineUsersSessions");
        onlineUsersSessions.remove(se.getSession());
        servletContext.setAttribute("onlineUsersSessions", onlineUsersSessions);



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

//    private void sendOnlineUsers() {
//        List<AsyncContext> asyncContexts = (LinkedList<AsyncContext>) servletContext.getAttribute("onlineUsersContexts");
//        List<HttpSession> onlineUsersSessions = (LinkedList<HttpSession>) servletContext.getAttribute("onlineUsersSessions");
//        for (HttpSession session : onlineUsersSessions) {
//            session.
//        String htmlMessage = "<p><b>" + name + "</b><br/>"
//        }
//        for (AsyncContext asyncContext : asyncContexts) {
//            try (PrintWriter writer = asyncContext.getResponse().getWriter()) {
//                writer.println(htmlMessage);
//                writer.flush();
//                asyncContext.complete();
//            } catch (Exception ignored) {
//            }
//        }
//    }
}
