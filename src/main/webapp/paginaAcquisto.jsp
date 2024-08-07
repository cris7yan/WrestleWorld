<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.IndirizzoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.wrestleworld.model.MetodoPagamentoBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    List<IndirizzoBean> indirizziUtente = (List<IndirizzoBean>) request.getAttribute("indirizziUtente");
    List<MetodoPagamentoBean> metodiPagamentoUtente = (List<MetodoPagamentoBean>) request.getAttribute("metodiPagamentoUtente");

    if(indirizziUtente == null || metodiPagamentoUtente == null) {
        response.sendRedirect("./OrdineControl?action=visualizzaDatiUtente");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/paginaAcquisto.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Pagina di acquisto</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

    <div class="acquisto-container">
        <form id="checkoutForm" action="OrdineControl?action=checkout" method="post">
            <% for (IndirizzoBean indirizzo : indirizziUtente) { %>
            <input type="radio" name="indirizzoScelto" value="<%=indirizzo.getIdIndirizzo()%>" id="indirizzo_<%=indirizzo.getIdIndirizzo()%>" required>
            <label for="indirizzo_<%=indirizzo.getIdIndirizzo()%>">
                <%=indirizzo.getViaIndirizzo()%>, <%=indirizzo.getCittaIndirizzo()%>, <%=indirizzo.getProvinciaIndirizzo()%>, <%=indirizzo.getNomeCompletoIndirizzo()%>
            </label>
            <% } %>

            <% for (MetodoPagamentoBean metodo : metodiPagamentoUtente) { %>
            <input type="radio" name="metodoScelto" value="<%=metodo.getIdMetodoPagamento()%>" id="metodo_<%=metodo.getIdMetodoPagamento()%>" required>
            <label for="metodo_<%=metodo.getIdMetodoPagamento()%>">
                <%=metodo.getNumeroCarta()%>, <%=metodo.getIntestatario()%>, <%=metodo.getDataScadenza()%>
            </label>
            <% } %>

            <button type="submit">Acquista</button>
        </form>
    </div>

<script>
    document.getElementById('checkoutForm').addEventListener('submit', function() {
        document.getElementById('searchForm').querySelector('.search-input').disabled = true;
        document.getElementById('searchForm').querySelector('.search-button').disabled = true;
    });
</script>

</body>
</html>
