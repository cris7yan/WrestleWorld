<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/modificaDati.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Indirizzi</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div>
    <form action="UtenteControl?action=modificaDati" method="post" class="form">

        <div class="form-title">
            <span>Modifica i tuoi dati da Superstar!</span>
        </div>
        <div class="form-title2">
            <span>WrestleWorld Superstar</span>
        </div>

        <div class="input-container">
            <input name="nome" type="text" placeholder="Nome" class="input-nome" value="<%=nome%>">
            <span> </span>
        </div>

        <div class="input-container">
            <input name="cognome" type="text" placeholder="Cognome" class="input-cognome" value="<%=cognome%>">
            <span> </span>
        </div>

        <div class="input-container">
            <input name="dataNascita" type="date" placeholder="Data di Nascita" class="input-dataNascita" value="<%=dataNascita%>">
            <span> </span>
        </div>

        <button type="submit" class="submitButton">
            <span>Conferma modifiche</span>
        </button>

    </form>
</div>

<%@ include file="footer.jsp"%>
</body>
</html>
