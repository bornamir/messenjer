<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<% String notif = (String) request.getAttribute("notif");
    if (notif != null) { %>
<div class="notif">
    <p style="color: red"> <%=notif%> </p>
</div>
<% } %>
