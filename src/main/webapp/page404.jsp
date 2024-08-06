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
  <title>WrestleWorld | Pagina 404</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

    <div class="text-error">
      <span>Page 404</span><br>
      <span>Attenzione!</span><br>
      <span>La pagina che stai cercando non esiste!</span>
    </div>

    <div class="torna">
      <a href="./index.jsp"><input class="error-comeback" type="button" value="Torna alla Homepage"> </a>
    </div>

</body>
</html>
