<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>

<% if (request.getSession(false) != null) { %>
<footer>
    <form method="POST" action="logout">
        <table>
            <tr>
                <td><input type="submit" value="Logout"/></td>
            </tr>
        </table>
    </form>
</footer>
<% } else {%>
<footer>

<div class="container" style="background-color:#f1eaed">

    <div class="row align-items-center ">
        <div class="col-lg-8 col-md-7 col-sm-6">
                <p><a href="#">Terms of Use</a>  <a href="#">Privacy Policy</a></p>
        </div>
        <div class="col-lg-4 col-md-5 col-sm-6">
            <p class="mb-0 text-right ">Copyright © 2020 <a href="signin">Borna</a></p>
        </div>
    </div>
</div>
</footer>

<% }%>
