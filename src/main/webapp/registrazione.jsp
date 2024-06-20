<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%!
    String result = "";
%>

<%
    result = (String) request.getAttribute("result");
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/registrazione.css">
    <title>WrestleWorld | Registrazione</title>
</head>
<style>
    /* Aggiunta per evitare sovrapposizione con la navbar */
    body {
        margin-top: 70px; /* Altezza della navbar + margine */
        padding-top: 20px; /* Spaziatura sopra il contenuto del body */
    }
</style>
<body>
<%@ include file="navbar.jsp"%>

<div>
    <div class="form-container">

        <form action="UtenteControl?action=registrazione" method="post" class="form">

            <div class="form-title">
                <span>Registrati anche tu nel roster di WrestleWorld e diventa anche tu una Superstar!</span>
            </div>
            <div class="form-title2">
                <span>WrestleWorld Roster</span>
            </div>

            <div class="input-container">
                <input name="email" type="email" placeholder="Email" class="input-email">
                <span> </span>
            </div>

            <div class="input-container">
                <input name="password" type="password" placeholder="Password" class="input-password">
                <span> </span>
            </div>

            <div class="input-container">
                <input name="nome" type="text" placeholder="Nome" class="input-nome">
                <span> </span>
            </div>

            <div class="input-container">
                <input name="cognome" type="text" placeholder="Cognome" class="input-cognome">
                <span> </span>
            </div>

            <div class="input-container">
                <input name="dataNascita" type="date" placeholder="Data di Nascita" class="input-dataNascita">
                <span> </span>
            </div>

            <button type="submit" class="submitButton">
                <span>Registrati</span>
            </button>

        </form>

    </div>
</div>

    <%
        if(result != null) {
    %>
    <h3><%=result%></h3>
    <%
        }
    %>

</body>
</html>
