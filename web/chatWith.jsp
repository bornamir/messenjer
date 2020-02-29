<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<html>
<head>
    <title><%=request.getParameter("user")%> chat</title>

    <link rel="stylesheet" href="statics/chatPage.css">
    <%--    <link rel="stylesheet" href="statics/bootstrap.min.css">--%>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="statics/chatWith.js"></script>
</head>
<body>

<div class="container">
    <h5 class=" text-center">Chatting with <b style="color: blue"><%=request.getParameter("user")%>
    </b></h5>
    <div class="messaging">
        <div class="inbox_msg">
            <div class="mesgs">
                <div class="msg_history" id="msg_history">
                </div>
                <div class="type_msg">
                    <div class="input_msg_write">
                        <input id="message_input" type="text" class="write_msg" placeholder="Type a message"/>
                        <button class="msg_send_btn" type="button" onclick="sendMessasge()"><i
                                class="fa fa-paper-plane-o"
                                aria-hidden="true"></i></button>
                    </div>
                </div>
            </div>
        </div>
        <p id="notif" style="color: red"></p>


    </div>
    <jsp:include page="partials/footer.jsp"/>
</div>


</body>
</html>
