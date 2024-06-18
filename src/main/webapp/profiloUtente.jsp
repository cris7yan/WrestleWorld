<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" language="java" %>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <link href="css/profiloUtente.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Profilo Utente</title>
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

<div id="main-container">
    <h1>Sei nella tua pagina profilo</h1>

    <div class="dati-container">
        <p><span class="label">Email:</span> <span class="value"><%=emailUtente%></span></p>
        <p><span class="label">Nome:</span> <span class="value"><%=nome%></span></p>
        <p><span class="label">Cognome:</span> <span class="value"><%=cognome%></span></p>
        <p><span class="label">Data di nascita:</span> <span class="value"><%=dataNascita%></span></p>
    </div>
    <a href="./modificaDati.jsp">Modifica i tuoi dati</a>
    <br>
    <a href="./indirizzi.jsp">Visualizza i tuoi indirizzi</a>
    <br>
    <a href="./ordini.jsp">Visualizza i tuoi ordini</a>
    <br>
    <a href="./metodiPagamento.jsp">Visualizza i tuoi metodi di pagamento</a>
</div>

</body>
</html>
