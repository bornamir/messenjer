<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%
    if (request.getSession(false) != null) {
        response.sendRedirect("chatPage");
    }
%>
<html>
<head>
    <link rel="stylesheet" href="statics/custom.css">
    <link rel="stylesheet" href="statics/bootstrap.min.css">
    <title>SignUp:Messenjer</title>
</head>
<body>
<div class="container">

    <div class="row align-items-center">
        <div class="col-md-5 ">
            <h1>Sign Up</h1>
            <form action="signup" method="post">
                <div class="imgcontainer">
                    <img src="statics/img_avatar2.png" alt="Avatar" class="avatar">
                </div>

                <div class="container">
                    <label><b>Username</b></label>
                    <input type="text" placeholder="Enter Username" name="username" required>

                    <label><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="password1" required>

                    <label><b>Re-Password</b></label>
                    <input type="password" placeholder="Enter Password again" name="password2" required>

                    <label><b>First Name</b></label>
                    <input type="text" placeholder="Enter First Name" name="firstName" >

                    <label><b>Last Name</b></label>
                    <input type="text" placeholder="Enter Last Name" name="Last Name" >

                    <label><b>Email</b></label>
                    <input type="text" placeholder="Enter Email" name="email" >

                    <button type="submit">SignUp</button>
                </div>
                <div class="container" style="background-color:#f1f1f1">
                    <span> Registered before? <a href="signin">Log In!</a></span>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>


</body>
</html>

