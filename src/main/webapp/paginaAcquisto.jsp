<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.IndirizzoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.wrestleworld.model.MetodoPagamentoBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<IndirizzoBean> indirizziUtente = (List<IndirizzoBean>) request.getAttribute("indirizziUtente");
    List<MetodoPagamentoBean> metodiPagamentoUtente = (List<MetodoPagamentoBean>) request.getAttribute("metodiPagamentoUtente");

    System.out.println("Indirizzi Utente: " + indirizziUtente);
    System.out.println("Metodi Pagamento Utente: " + metodiPagamentoUtente);

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

    <form action="OrdineControl?action=checkout" method="post">
        <%
            for (IndirizzoBean indirizzo : indirizziUtente) {
        %>
        <input type="radio" name="indirizzoScelto" value="<%=indirizzo.getIdIndirizzo()%>" id="<%=indirizzo.getIdIndirizzo()%>" required>
        <label><%=indirizzo.getViaIndirizzo()%>, <%=indirizzo.getCittaIndirizzo()%>, <%=indirizzo.getProvinciaIndirizzo()%>, <%=indirizzo.getNomeCompletoIndirizzo()%></label>
        <%
            }
        %>

        <%
            for (MetodoPagamentoBean metodo : metodiPagamentoUtente) {
        %>
        <input type="radio" name="metodoScelto" value="<%=metodo.getIdMetodoPagamento()%>" id="<%=metodo.getIdMetodoPagamento()%>" required>
        <label><%=metodo.getNumeroCarta()%>, <%=metodo.getIntestatario()%>, <%=metodo.getDataScadenza()%></label>
        <%
            }
        %>

        <button type="submit">Acquista</button>
    </form>

</body>
</html>
