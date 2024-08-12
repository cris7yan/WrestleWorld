<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.UtenteBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
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
    List<UtenteBean> utenti = (List<UtenteBean>) request.getAttribute("utenti");
    if (utenti == null) {
        response.sendRedirect("./AdminControl?action=visualizzaUtenti");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/utenti.css" rel="stylesheet" type="text/css">
    <title>WrestleWorld | Utenti</title>
</head>
<body>
<%@ include file="navbar.jsp" %>

<div class="utenti-container">
    <div class="filters">
        <div class="filter-section">
            <h3>Ordina per</h3>
            <div class="filter-option">
                <label for="order-by">Criterio di ordinamento:</label>
                <select id="order-by">
                    <option value="nome-asc">Nome (A-Z)</option>
                    <option value="nome-desc">Nome (Z-A)</option>
                    <option value="cognome-asc">Cognome (A-Z)</option>
                    <option value="cognome-desc">Cognome (Z-A)</option>
                </select>
            </div>
        </div>

        <div class="filter-section">
            <h3>Cerca per cognome</h3>
            <div class="filter-option">
                <label for="cognome-search">Cognome:</label>
                <input type="text" id="cognome-search" name="cognome-search" placeholder="Inserisci il cognome">
            </div>
        </div>

        <div class="filter-section">
            <h3>Periodo di nascita:</h3>
            <div class="filter-option">
                <label for="start-date">Data Inizio:</label>
                <input type="date" id="start-date">
            </div>
            <div class="filter-option">
                <label for="end-date">Data Fine:</label>
                <input type="date" id="end-date">
            </div>
        </div>

        <div class="filter-apply">
            <button id="apply-filters" class="custom-btn btn">Applica filtri</button>
            <button id="reset-filters" class="custom-btn btn">Resetta filtri</button>
        </div>
    </div>

    <div class="container">
        <h1>Utenti registrati alla piattaforma</h1>

        <% for (UtenteBean utente : utenti) { %>
        <div class="user-box" data-nome="<%= utente.getNome() %>" data-cognome="<%= utente.getCognome() %>" data-datanascita="<%=utente.getDataNascita() %>">
            <div>
                <div class="img-user">
                    <img src="img/sitoweb/box-user.png" alt="Immagine utente">
                    <img src="img/logo/WrestleWorldIconremove.png " alt="WrestleWorld Logo" class="logo">
                </div>
                <div class="dettagli">
                    <div class="dettaglio">
                        <strong>Utente:</strong>
                        <span><%= utente.getNome() %> <%= utente.getCognome() %></span>
                    </div>
                    <div class="dettaglio">
                        <strong>Email:</strong>
                        <span><%= utente.getEmail() %></span>
                    </div>
                    <div class="dettaglio">
                        <strong>Data di Nascita</strong>
                        <span><%= utente.getDataNascita() %></span>
                    </div>
                    <div class="dettaglio">
                        <strong>Tipo Utente:</strong>
                        <span><%= utente.getTipoUtente() %></span>
                    </div>
                </div>
                <div class="bottoni">
                    <button onclick="location.href='AdminControl?action=visualizzaOrdiniUtenti&email=<%= utente.getEmail() %>'" class="custom-btn btn">Ordini Utente</button>
                </div>
            </div>
        </div>
        <% } %>
    </div>
</div>

<script src="js/gestioneFiltroUtenti.js"></script>

</body>
</html>
