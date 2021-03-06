<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%
    if (request.getSession(false) != null) {
        response.sendRedirect("chatPage");
    }
%>
<html>
<head>
    <link rel="icon" href="statics/favicon.ico" type="image/png">
    <link rel="stylesheet" href="statics/custom.css">
    <link rel="stylesheet" href="statics/bootstrap.min.css">
    <title>Login:Messenjer</title>
</head>
<body>
<div class="container">

    <div class="row align-items-center">
        <div class="col-md-5 ">
            <h1>Login</h1>
            <jsp:include page="partials/notifications.jsp"/>

            <form action="login" method="post">
                <div class="imgcontainer">
                    <img src="statics/img_avatar2.png" alt="Avatar" class="avatar">
                </div>

                <div class="container">
                    <label><b>Username</b></label>
                    <input type="text" placeholder="Enter Username" name="username" required>

                    <label><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="password" required>

                    <button type="submit">Login</button>
                </div>
                <div class="container" style="background-color:#f1f1f1">
                    <span>Not Registered yet? <a href="signup">SignUp!</a></span>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="partials/footer.jsp"/>


</body>
</html>

