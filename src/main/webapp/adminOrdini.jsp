<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.OrdineBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="page500.jsp" %>

<%
    session = request.getSession();
    String tipoUtente = (String) session.getAttribute("tipo");
    if (tipoUtente == null || tipoUtente.equals("Utente")) {
        response.sendRedirect("page403.jsp");
        return;
    }
%>

<%
    List<OrdineBean> ordini = (List<OrdineBean>) request.getAttribute("ordini");
    if(ordini == null) {
        response.sendRedirect("./AdminControl?action=visualizzaOrdiniTotali");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/ordini.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Ordini</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

    <div class="ordini-container">

        <div class="filters">
            <h3>Filtri per Ordini</h3>

            <div class="filter-section">
                <h4>Prezzo</h4>
                <input type="radio" id="0-50" name="price">
                <label for="0-50">0 - 50</label>
                <input type="radio" id="51-100" name="price">
                <label for="51-100">51 - 100</label>
                <input type="radio" id="101-500" name="price">
                <label for="101-500">101 - 500</label>
                <input type="radio" id="501-" name="price">
                <label for="501-">500+</label>
            </div>

            <div class="filter-section">
                <h4>Periodo</h4>
                <label for="start-date">Data Inizio:</label>
                <input type="date" id="start-date">
                <label for="end-date">Data Fine:</label>
                <input type="date" id="end-date">
            </div>

            <div class="filter-apply">
                <button id="apply-filters">Applica filtri</button>
                <button id="reset-filters">Resetta filtri</button>
            </div>
        </div>

        <h1>Ordini effettuati sulla piattaforma</h1>

        <%
            if (ordini != null && !ordini.isEmpty()) {
                for (OrdineBean ordine : ordini) {
                    if (ordine != null) {
        %>

        <div class="ordine" data-prezzo="<%= ordine.getPrezzoTotaleOrdine() %>" data-data="<%= ordine.getDataOrdine() %>">
            ID Ordine: <%= ordine.getIdOrdine() %> <br>
            Utente: <%= ordine.getUtenteOrdine() != null ? (ordine.getUtenteOrdine().getNome() + " " + ordine.getUtenteOrdine().getCognome()) : "N/A" %> <br>
            Email: <%= ordine.getUtenteOrdine() != null ? ordine.getUtenteOrdine().getEmail() : "N/A" %> <br>
            Data: <%= ordine.getDataOrdine() %> <br>
            Totale: <%= ordine.getPrezzoTotaleOrdine() %> <br>
            <a href="./OrdineControl?action=visualizzaDettagliOrdine&idOrdine=<%= ordine.getIdOrdine() %>">
                <button>Visualizza dettagli</button>
            </a>
        </div>

        <%
                }
            }
        } else {
        %>
        <p>Non è stato effettuato ancora nessun ordine</p>
        <% } %>

    </div>

<script src="js/gestioneFiltroOrdini.js"></script>

<%@ include file="footer.jsp"%>
</body>
</html>
