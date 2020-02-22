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
<% } %>
