<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>

<% if (request.getSession(false) != null) { %>
<footer>
    <div class="row  ">
        <div class="col-lg-3 col-md-3 col-sm-3">
            <p><a href="#">Terms of Use</a><br> <a href="#">Privacy Policy</a></p>
        </div>
        <div class="col-lg-6 col-md-6 col-sm-6">
            <p class="mb-0 text-center" >Design by <a target="_blank" href="https://bootsnipp.com/snippets/1ea0N">Sunil
                Rajput</a> <br> Copyright © 2020 <a href="login">Borna</a></p>
        </div>
        <div class="col-lg-3 col-md-3 col-sm-3">
            <form method="POST" action="logout">
                <input type="submit" value="Logout" style="float: right"/>
            </form>
        </div>
    </div>
</footer>
<% } else {%>
<footer>

    <div class="container" style="background-color:#f1eaed">

        <div class="row align-items-center ">
            <div class="col-lg-8 col-md-7 col-sm-6">
                <p><a href="#">Terms of Use</a> <a href="#">Privacy Policy</a></p>
            </div>
            <div class="col-lg-4 col-md-5 col-sm-6">
                <p class="mb-0 text-right ">Copyright © 2020 <a href="login">Borna</a></p>
            </div>
        </div>
    </div>
</footer>

<% }%>
