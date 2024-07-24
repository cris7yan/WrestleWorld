<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.OrdineBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

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

<div>
    <div class="ordini-container">
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

        <div class="ordine">
            ID Ordine: <%= ordine.getIdOrdine() %> <br>
            Data: <%= ordine.getDataOrdine() %> <br>
            Totale: <%= ordine.getPrezzoTotaleOrdine() %> <br>
            <a href="./OrdineControl?action=visualizzaDettagliOrdine&idOrdine=<%=ordine.getIdOrdine()%>">
                <button>Visualizza dettagli</button>
            </a>
            <button>Scarica fattura</button>
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

        <%
            if (ordine != null) {
        %>

        <div class="ordine">
            ID Ordine: <%= ordine.getIdOrdine() %> <br>
            Data: <%= ordine.getDataOrdine() %> <br>
            Totale: <%= ordine.getPrezzoTotaleOrdine() %> <br>
            <a href="./OrdineControl?action=visualizzaDettagliOrdine&idOrdine=<%=ordine.getIdOrdine()%>">
                <button>Visualizza dettagli</button>
            </a>
            <button>Scarica fattura</button>
        </div>

        <%
                }
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

<%@ include file="footer.jsp"%>
</body>
</html>
