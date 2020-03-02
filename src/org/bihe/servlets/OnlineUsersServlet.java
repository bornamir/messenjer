package org.bihe.servlets;

import com.google.gson.Gson;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "OnlineUsersServlet", urlPatterns = "/onlineUsers", asyncSupported = true)

public class OnlineUsersServlet extends HttpServlet {
    private static final List<AsyncContext> CONTEXTS = new LinkedList<>();
    private static final List<String> onlineUsers = new LinkedList<>();

    public static List<String> getOnlineUsers() {
        return onlineUsers;
    }


    /* user is trying to logged in, should add new online user and send to all
     if the user is not online already! if so, user can not log in again.*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        if (username != null && !onlineUsers.contains(username)) {  // user is not in the online users's list
            request.setAttribute("valid",true);
            onlineUsers.add(username);
            sendUpdatedUsersList(request.getServletContext()); // send new online user to others.

        } else { // user in on the onlineusers's list and already logged in
            request.setAttribute("valid",false);
        }


    }

     /*A user is reached to ChatPage and wants to get the online users updates.*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession(false) == null) {
            response.sendError(400);
        } else if ("1".equals(request.getParameter("updated"))) {
            final AsyncContext asyncOnlineContext = request.startAsync(request, response);
            asyncOnlineContext.setTimeout(15 * 10 * 1000);
            CONTEXTS.add(asyncOnlineContext);
        } else {
            try (ServletOutputStream out = response.getOutputStream()) {
                List<String> users = (LinkedList<String>) request.getServletContext().getAttribute("users");
                String jsonMessage = getUserListToJson(onlineUsers, users);
                response.setContentType("application/json;charset=UTF-8");
                out.print(jsonMessage);
                out.flush();
            }
        }

    }

    /*This method sends <users> and <onlineusers> list to all the online users ( who have a async context
    in the CONTEXTS list. This information is sent in the json format and will be used by the client to
    produce the desire UI*/
    public static void sendUpdatedUsersList(ServletContext servletContext) {

        List<AsyncContext> asyncContexts = new ArrayList<>(CONTEXTS);
        CONTEXTS.clear();
        List<String> users = (LinkedList<String>) servletContext.getAttribute("users");

        String message = getUserListToJson(onlineUsers, users);
        for (AsyncContext asyncContext : asyncContexts) {
            try (ServletOutputStream out = asyncContext.getResponse().getOutputStream()) {
                asyncContext.getResponse().setContentType("application/json;charset=UTF-8");
                out.println(message);
                out.flush();
                asyncContext.complete();
            } catch (Exception ignored) {
            }
        }
    }
    /*get two lists and returns a json object with first list as users and second list as onlineUsers*/
    private static String getUserListToJson(List<String> onlineUsers, List<String> users) {
        Map<String, List<String>> messageMap = new TreeMap<>();
        messageMap.put("users", users);
        messageMap.put("onlineUsers", onlineUsers);
        Gson gson = new Gson();
        return gson.toJson(messageMap);
    }
    /* Removes a user from onlineusers list. used by session listener when a session is timeout or terminated.*/
    public static boolean removeOnlineUserFromList(String username) {
        return onlineUsers.remove(username);
    }

}
