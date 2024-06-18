<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" language="java" %>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="ISO-8859-1">
    <link href="css/modificaDati.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Indirizzi</title>
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

</body>
</html>
