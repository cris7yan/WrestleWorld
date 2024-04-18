<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%!
    String email = "";
    String result = "";
%>

<%
    synchronized (session) {
        session = request.getSession();
        email = (String) session.getAttribute("email");
    }
    result = (String) request.getAttribute("result");
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <title>WrestleWorld | HomePage</title>
</head>
<body>

    <form action="UtenteControl?action=login" method="post">
        Email: <input type="email" name="email" placeholder="Email">
        Password: <input type="password" name="password" placeholder="Password">

        <%
            if (result != null) {
        %>
            <h3><%= result %></h3>
        <%
            }
        %>

        <input type="submit">
    </form>

</body>
</html>
