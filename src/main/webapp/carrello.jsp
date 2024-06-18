<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" language="java" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="ISO-8859-1">
    <title>WrestleWorld | Carrello</title>
    <link href="css/paginaProdotto.css" rel="stylesheet" type="text/css">
</head>
<style>
    /* Aggiunta per evitare sovrapposizione con la navbar */
    body {
        margin-top: 70px; /* Altezza della navbar + margine */
        padding-top: 5px; /* Spaziatura sopra il contenuto del body */
    }
</style>
<body>
<%@ include file="navbar.jsp"%>

    <%
        if(carrello != null && !carrello.getCarrello().isEmpty()) {
            for(ProdottoBean prod : carrello.getCarrello()) {
    %>
        <div class="product-details">
            <p class="product-name"> <%=((ProdottoBean) prod).getNomeProdotto()%>  <br></p>
            <p class="product-price"><%=((ProdottoBean) prod).getPrezzoProdotto()%>&euro; <br></p>
            <a href="ProdottoControl?action=rimuoviDalCarrello&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">Rimuovi dal carrello</a>
            <br><br>
        </div>
    <%
            }

        } else {
    %>
        <h2>Il carrello � vuoto</h2>
    <%
        }
    %>
</body>
</html>
