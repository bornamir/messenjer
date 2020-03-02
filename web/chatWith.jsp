<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%
    HttpSession session = request.getSession(false);
    if (session == null) {
        response.sendRedirect("/");
    }
%>
<html>
<head>
    <title><%=request.getParameter("user")%> chat</title>

    <link rel="stylesheet" href="statics/chatPage.css">
    <link rel="icon" href="statics/favicon.ico" type="image/png">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" type="text/css"
          rel="stylesheet"

    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>
<body onbeforeunload="closeSSE()">

<div class="container">
    <h5 class=" text-center"><a href="chatPage"><%=session.getAttribute("username")%> </a>
        You are chatting with <b style="color: blue"><%=request.getParameter("user")%>
    </b></h5>
    <div class="messaging">
        <div class="inbox_msg">
            <div class="mesgs">
                <div class="msg_history" id="msg_history">
                </div>
                <div class="type_msg">
                    <div class="input_msg_write">
                        <input id="message_input" type="text" class="write_msg" placeholder="Type a message"/>
                        <button class="msg_send_btn" type="button" onclick="selectFile()" style="right: 40px;"><i
                                class="fa fa-upload" aria-hidden="true"></i></button>
                        <button class="msg_send_btn" type="button" onclick="send()"><i
                                class="fa fa-paper-plane-o " aria-hidden="true"></i></button>
                    </div>
                </div>
            </div>
        </div>
        <p id="notif" style="color: red"></p>
<%--        <input id="upload_input" type="file" multiple="false" onchange="selectedFile()" style="visibility: hidden">--%>

    </div>
    <jsp:include page="partials/footer.jsp"/>
</div>

<script src="statics/chatWith.js"></script>

</body>
</html>
