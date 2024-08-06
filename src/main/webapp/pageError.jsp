<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
  <link href="css/pageError.css" rel="stylesheet" type="text/css">
  <title>WrestleWorld | Error Page</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

    <div class="text-error">
      <span>Page Error</span><br>
      <span>Attenzione!</span><br>
      <span><%= request.getAttribute("error") %></span>
    </div>

    <div class="torna">
      <a href="./index.jsp"><input class="error-comeback" type="button" value="Torna alla Homepage"> </a>
    </div>

</body>
</html>
