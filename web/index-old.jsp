<%--
  Created by IntelliJ IDEA.
  User: borna
  Date: 2/6/20
  Time: 2:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>
<h1>SHOUT-OUT!</h1>
<form method="POST" action="shoutServlet">
    <table>
        <tr>
            <td>Your name:</td>
            <td><input type="text" id="name" name="name"/></td>
        </tr>
        <tr>
            <td>Your shout:</td>
            <td><input type="text" id="message" name="message"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="SHOUT"/></td>
        </tr>
    </table>
</form>
<h2> Current Shouts </h2>
<div id="content">
    <% if (application.getAttribute("messages") != null) {%>
    <%= application.getAttribute("messages")%>
    <% }%>
</div>
<script>
    var messagesWaiting = false;
    function getMessages(){
        if(!messagesWaiting){
            messagesWaiting = true;
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.onreadystatechange=function(){
                if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                    messagesWaiting = false;
                    var contentElement = document.getElementById("content");
                    contentElement.innerHTML = xmlhttp.responseText + contentElement.innerHTML;
                }
            }
            xmlhttp.open("GET", "shoutServlet?t="+new Date(), true);
            xmlhttp.send();
        }
    }
    setInterval(getMessages, 1000);
</script>
</body>
</html>
