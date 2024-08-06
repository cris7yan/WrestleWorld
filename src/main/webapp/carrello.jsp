<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page errorPage="pageError.jsp" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>WrestleWorld | Carrello</title>
    <link href="css/carrello.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@ include file="navbar.jsp"%>

    <%
        if (carrello != null && !carrello.getCarrello().isEmpty()) {
            float prezzoTotaleCarrello = carrello.getPrezzoCarrello();
    %>
    <div class="cart-details">
        <h2>Dettagli del Carrello</h2>
        <%
            for (ProdottoBean prod : carrello.getCarrello()) {
                BigDecimal prezzoOriginale = new BigDecimal(prod.getPrezzoProdotto());
                BigDecimal prezzoOfferta = new BigDecimal(prod.getPrezzoOffertaProdotto());
                BigDecimal prezzoDaMostrare;

                if (prezzoOfferta.compareTo(BigDecimal.ZERO) > 0 && prezzoOfferta.compareTo(prezzoOriginale) < 0) {
                    prezzoDaMostrare = prezzoOfferta;
                } else {
                    prezzoDaMostrare = prezzoOriginale;
                }

                prezzoDaMostrare = prezzoDaMostrare.setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal prezzoTotaleProdotto = prezzoDaMostrare.multiply(new BigDecimal(prod.getQuantitaCarrello())).setScale(2, BigDecimal.ROUND_HALF_UP);
        %>
        <div class="product-details">
            <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                <p class="product-name"><%= prod.getNomeProdotto() %></p>
            </a>
            <p class="product-price"><%= prezzoDaMostrare %>€</p>
            <p class="product-size"><%= prod.getTagliaSelezionata() %></p>
            <p class="product-quantity"><%= prod.getQuantitaCarrello() %></p>
            <p class="product-total-price"><%= prezzoTotaleProdotto %>€</p>
            <a class="remove-button" href="ProdottoControl?action=rimuoviDalCarrello&IDProd=<%= prod.getIDProdotto() %>&taglia=<%= prod.getTagliaSelezionata() %>">Rimuovi</a>
        </div>
        <%
            }
        %>
        <div class="total-price">
            <h3>Prezzo Totale: <%= new BigDecimal(prezzoTotaleCarrello).setScale(2, BigDecimal.ROUND_HALF_UP) %>€</h3>
        </div>
        <div>
            <a href="paginaAcquisto.jsp">Acquista</a><br>
        </div>
    </div>
    <%
    } else {
    %>
    <h2>Il carrello è vuoto</h2>
    <%
        }
    %>

</body>
</html>
