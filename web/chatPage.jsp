<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%
    HttpSession session = request.getSession(false);
    if (session == null) {
        response.sendRedirect("/");
    }
%>

<html>
<head>
    <link rel="stylesheet" href="statics/chatPage.css">
    <%--    <link rel="stylesheet" href="statics/bootstrap.min.css">--%>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="statics/main.js"></script>


    <%--    <title>MessenJer</title>--%>
</head>
<body>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" type="text/css"
      rel="stylesheet">
<div class="container">
    <h3 class=" text-center">Welcome <%=session.getAttribute("username")%></h3>
    <div class="messaging">
        <div class="inbox_msg">
            <div class="inbox_people">
                <div class="inbox_chat" id="inbox_chat"></div>
            </div>
            <div class="inbox_people">
                <div class="headind_srch">
                    <div class="recent_heading">
                        <h4>Recent</h4>
                    </div>
                    <div class="srch_bar">
                        <div class="stylish-input-group">
                            <input id="search_input" type="text" class="search-bar" placeholder="Search"/>
                            <span class="input-group-addon">
                                    <button onclick="searchUser(this)" type="button"> <i class="fa fa-search" aria-hidden="true"></i> </button>
                                </span>
                        </div>
                    </div>
                </div>
                <div class="inbox_chat inbox_search" id="search_inbox"></div>
            </div>
        </div>


        <p class="text-center top_spac"> Design by <a target="_blank" href="https://bootsnipp.com/snippets/1ea0N">Sunil
            Rajput</a></p>

    </div>
</div>


<%--<jsp:include page="partials/footer.jsp"/>--%>


</body>
</html>


