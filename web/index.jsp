<%--
  Created by IntelliJ IDEA.
  User: borna
  Date: 2/6/20
  Time: 2:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%
    if (request.getSession(false) != null) {
        response.sendRedirect("chatPage");
    }
%>
<html>
<head>
    <title>$Title$</title>
</head>
<body>
<h1>Login</h1>
<form method="POST" action="login">
    <table>
        <tr>
            <td>Your name:</td>
            <td><input type="text" id="username" name="username"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Login"/></td>
        </tr>
    </table>
</form>

<% request.getRequestDispatcher("footer.jsp").include(request, response); %>

</body>
</html>
