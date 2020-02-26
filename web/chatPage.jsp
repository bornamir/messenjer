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
%>

<html>
<head>
    <title>MessenJer</title>
</head>
<body>
<h1>Welcome <%=session.getAttribute("username")%>
</h1>


<h2> Online Users:</h2>

<div id="onlineUsersDiv">

</div>
<jsp:include page="partials/footer.jsp"/>

<script src="statics/main.js"></script>
</body>
</html>
