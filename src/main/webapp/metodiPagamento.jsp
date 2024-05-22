<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.MetodoPagamentoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" language="java" %>

<%
    List<MetodoPagamentoBean> metodi = (List<MetodoPagamentoBean>) request.getAttribute("metodiPagamento");
    if(metodi == null) {
        response.sendRedirect("./UtenteControl?action=visualizzaMetodiPagamento");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="ISO-8859-1">
    <link href="css/metodiPagamento.css" type="text/css" rel="stylesheet">
    <link href="css/modificaDati.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Metodi di Pagamento</title>
</head>
<style>
    /* Aggiunta per evitare sovrapposizione con la navbar */
    body {
        margin-top: 70px; /* Altezza della navbar + margine */
        padding-top: 10px; /* Spaziatura sopra il contenuto del body */
    }
</style>
<body>
<%@ include file="navbar.jsp"%>

<div class="form-container">

    <div class="metodi-container">
        <h1>Metodi di pagamento personali</h1>

        <%
            if(metodi != null && !metodi.isEmpty()) {
                Iterator<?> indIt =  metodi.iterator();
                while (indIt.hasNext()) {
                    MetodoPagamentoBean metodo = (MetodoPagamentoBean) indIt.next();
        %>

        <%
            if (metodo != null) {
        %>

        <div class="metodoPagamento">
            Numero Carta: <%= metodo.getNumeroCarta() %> <br>
            Intestatario: <%= metodo.getIntestatario() %> <br>
            Data Scadenza: <%= metodo.getDataScadenza() %> <br><br>
            <a href="UtenteControl?action=rimuoviMetodoPagamento&ID_Pagamento=<%=metodo.getIdMetodoPagamento()%>" class="removeMetod-link">Rimuovi metodo</a>
        </div>

        <%
                    }

                }

            }
        %>

    </div>

    <form action="UtenteControl?action=aggiungiMetodoPagamento" method="post" class="form">
        <div class="form-title">
            <span>Nuovo metodo di pagamento</span>
        </div>

        <div class="input-container">
            <input class="input-intestatario" name="intestatario" type="text" placeholder="Nome e Cognome">
            <span> </span>
        </div>

        <div class="input-container">
            <input class="input-numeroCarta" name="numeroCarta" type="text" placeholder="IBAN">
            <span> </span>
        </div>

        <div class="input-container">
            <input class="input-dataScadenza" name="dataScadenza" type="date" placeholder="Data Scadenza">
            <span> </span>
        </div>

        <button type="submit" class="submitButton">
            <span class="sign text">Conferma</span>
        </button>

    </form>

</div>

</body>
</html>
