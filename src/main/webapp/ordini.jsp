<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.OrdineBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page errorPage="page500.jsp" %>

<%
    session = request.getSession();
    String tipoUtente = (String) session.getAttribute("tipo");
    if (tipoUtente == null) {
        response.sendRedirect("page403.jsp");
        return;
    }
%>

<%
    List<OrdineBean> ordini = (List<OrdineBean>) request.getAttribute("ordini");
    if(ordini == null) {
        response.sendRedirect("./OrdineControl?action=visualizzaOrdini");
        return;
    }
%>

<%
    String email = (String) request.getAttribute("email");
    String nomeOrdine = (String) request.getAttribute("nomeOrdine");
    String cognomeOrdine = (String) request.getAttribute("cognomeOrdine");
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
        <div class="filter-section">
            <h3>Prezzo</h3>
            <div class="filter-option">
                <label for="min-price">Minimo:</label>
                <input type="number" id="min-price" name="min-price" placeholder="Prezzo minimo" min="0">
            </div>
            <div class="filter-option">
                <label for="max-price">Massimo:</label>
                <input type="number" id="max-price" name="max-price" placeholder="Prezzo massimo" min="0">
            </div>
        </div>

        <div class="filter-section">
            <h3>Periodo</h3>
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
            <button id="apply-filters">Applica filtri</button>
            <button id="reset-filters">Resetta filtri</button>
        </div>
    </div>

    <div class="orders-container">
            <%
                if ("Utente".equals(tipoUtente)) {
            %>
            <h1>I tuoi ordini</h1>
            <%
                if (ordini != null && !ordini.isEmpty()) {
                    Iterator<?> ordIt = ordini.iterator();
                    while (ordIt.hasNext()) {
                        OrdineBean ordine = (OrdineBean) ordIt.next();
                        if (ordine != null) {
            %>

            <div class="ordine" data-prezzo="<%= ordine.getPrezzoTotaleOrdine() %>" data-data="<%= ordine.getDataOrdine() %>">
                ID Ordine: <%= ordine.getIdOrdine() %> <br>
                Data: <%= ordine.getDataOrdine() %> <br>
                Totale: <%= ordine.getPrezzoTotaleOrdine() %> <br>
                <a href="./OrdineControl?action=visualizzaDettagliOrdine&idOrdine=<%=ordine.getIdOrdine()%>">
                    <button>Visualizza dettagli</button>
                </a>
                <%  if("Utente".equals(tipoUtente)) { %>
                <button onclick="location.href='OrdineControl?action=generaFattura&IdOrdine=<%=ordine.getIdOrdine()%>'">Scarica fattura</button>
                <%  }  %>
            </div>

            <%
                    }
                }
            } else {
            %>
            <p>Non hai effettuato ancora nessun ordine</p>
            <p><a href="./catalogo.jsp">Dai un'occhiata al nostro catalogo</a></p>
            <%
                }
            } else if ("Admin".equals(tipoUtente)) {
                if (ordini != null && !ordini.isEmpty()) {
            %>
            <h1>Ordini effettuati da <%= nomeOrdine %> <%= cognomeOrdine %></h1>
            <%
                Iterator<?> ordIt = ordini.iterator();
                while (ordIt.hasNext()) {
                    OrdineBean ordine = (OrdineBean) ordIt.next();
            %>

            <div class="ordine" data-prezzo="<%= ordine.getPrezzoTotaleOrdine() %>" data-data="<%= ordine.getDataOrdine() %>">
                ID Ordine: <%= ordine.getIdOrdine() %> <br>
                Data: <%= ordine.getDataOrdine() %> <br>
                Totale: <%= ordine.getPrezzoTotaleOrdine() %> <br>
                <a href="./OrdineControl?action=visualizzaDettagliOrdine&idOrdine=<%=ordine.getIdOrdine()%>">
                    <button>Visualizza dettagli</button>
                </a>
            </div>

            <%
                }
            } else {
            %>
            <h1>Ordini effettuati da <%= nomeOrdine %> <%= cognomeOrdine %></h1>
            <p>L'utente <%= nomeOrdine %> <%= cognomeOrdine %> non ha ancora effettuato alcun ordine</p>
            <%
                    }
                }
            %>
        </div>

</div>

<script src="js/gestioneFiltroOrdini.js"></script>

<%@ include file="footer.jsp"%>
</body>
</html>
