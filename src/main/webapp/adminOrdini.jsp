<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.OrdineBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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

<div>
    <div class="ordini-container">
        <h1>Ordini effettuati sulla piattaforma</h1>

        <%
            if (ordini != null && !ordini.isEmpty()) {
                for (OrdineBean ordine : ordini) {
                    if (ordine != null) {
        %>

        <div class="ordine">
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
</div>

<%@ include file="footer.jsp"%>
</body>
</html>
