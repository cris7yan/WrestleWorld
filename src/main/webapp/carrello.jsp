<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page errorPage="page500.jsp" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>WrestleWorld | Carrello</title>
    <link href="css/carrello.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@ include file="navbar.jsp"%>

<div class="cart-container">
    <%
        if (carrello != null && !carrello.getCarrello().isEmpty()) {
            float prezzoTotaleCarrello = carrello.getPrezzoCarrello();
    %>
    <div class="cart-details">
        <h2>Prodotti aggiunti al carrello</h2>
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
            <button class="custom-btn-2 btn" onclick="location.href='ProdottoControl?action=rimuoviDalCarrello&IDProd=<%= prod.getIDProdotto() %>&taglia=<%= prod.getTagliaSelezionata() %>'">Rimuovi</button>
        </div>
        <% } %>
        <div class="total-price">
            <h3>Prezzo Totale: <%= new BigDecimal(prezzoTotaleCarrello).setScale(2, BigDecimal.ROUND_HALF_UP) %>€</h3>
            <h3 style="color: red">Spedizione: 5€</h3>
        </div>
    </div>
    <div>
        <button onclick="location.href='paginaAcquisto.jsp'" class="custom-btn btn">Acquista</button>
    </div>
    <% } else { %>
    <h3>Non hai ancora aggiunto nessun prodotto al carrello</h3>
    <h4>Dai un'occhiata al nostro <a href="./catalogo.jsp">catalogo</a> per trovare cià che gradisci</h4>
    <% } %>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>
