<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>WrestleWorld | Carrello</title>
    <link href="css/paginaProdotto.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@ include file="navbar.jsp"%>

    <%
        if(carrello != null && !carrello.getCarrello().isEmpty()) {
            for(ProdottoBean prod : carrello.getCarrello()) {
    %>
        <div class="product-details">
            <p class="product-name"> <%=((ProdottoBean) prod).getNomeProdotto()%>  <br></p>
            <p class="product-price"><%=((ProdottoBean) prod).getPrezzoProdotto()%>&euro; <br></p>
            <a href="paginaAcquisto.jsp">Acquista</a><br>
            <a href="ProdottoControl?action=rimuoviDalCarrello&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">Rimuovi dal carrello</a>
            <br><br>
        </div>
    <%
            }

        } else {
    %>
        <h2>Il carrello è vuoto</h2>
    <%
        }
    %>

</body>
</html>
