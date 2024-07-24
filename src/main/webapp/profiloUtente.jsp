<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <link href="css/profiloUtente.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Profilo Utente</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div class="header-container">
    <h1>Benvenuto nel tuo profilo</h1>
</div>

<div class="dati-container">
    <p class="nome-cognome"><span class="value"><%=nome%> <%=cognome%></span></p>
    <p class="email"><span class="label">Email:</span> <span class="value"><%=emailUtente%></span></p>
    <p class="data-nascita"><span class="label">Data di nascita:</span> <span class="value"><%=dataNascita%></span></p>
</div>

<div class="link-container">
    <% if ("Admin".equals(tipoUtente)) { %>
        <a href="./admin.jsp">Visualizza utenti</a>
    <% } else { %>
        <a href="./modificaDati.jsp">Modifica i tuoi dati</a>
        <a href="./indirizzi.jsp">Visualizza i tuoi indirizzi</a>
        <a href="./ordini.jsp">Visualizza i tuoi ordini</a>
        <a href="./metodiPagamento.jsp">Visualizza i tuoi metodi di pagamento</a>
    <% } %>
</div>

<%@ include file="footer.jsp"%>
</body>
</html>
