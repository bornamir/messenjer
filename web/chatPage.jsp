<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %><%--
  Created by IntelliJ IDEA.
  User: borna
  Date: 2/8/20
  Time: 2:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%
    HttpSession session = request.getSession(false);
    if (session == null) {
        response.sendRedirect("/");
    }
    assert session != null;%>

<html>
<head>
    <title>MessenJer</title>
</head>
<body>
<h1>Welcome <%=session.getAttribute("username")%>
</h1>


<h2> Online Users:</h2>

<div id="content">
    <%
        StringBuilder htmlMessage = new StringBuilder("<p><b> Online users are :</b><br/>");
        if (application.getAttribute("onlineUsersSessions") != null) {
            List<HttpSession> onlineUsersSessions;
            onlineUsersSessions = (LinkedList<HttpSession>) application.getAttribute("onlineUsersSessions");
            for (HttpSession se : onlineUsersSessions) {
                String user = (String) se.getAttribute("username");
                htmlMessage.append("<p>").append(user).append("</p>");
            }
        }
    %>
    <%= htmlMessage.toString()%>
</div>



<script>
    var messagesWaiting = false;

    function getMessages() {
        if (!messagesWaiting) {
            messagesWaiting = true;
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    messagesWaiting = false;
                    var contentElement = document.getElementById("content");
                    contentElement.innerHTML = xmlhttp.responseText;
                }
            }
            xmlhttp.open("GET", "onlineUsers?t=" + new Date(), true);
            xmlhttp.send();
        }
    }

    setInterval(getMessages, 1000);
</script>
<jsp:include page="footer.jsp" />
</body>
</html>
