<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    session = request.getSession();
    String tipoUtente = (String) session.getAttribute("tipo");
    if (tipoUtente == null) {
        response.sendRedirect("page403.jsp");
        return;
    }
%>

<%
    List<ProdottoBean> prodottiOrdine = (List<ProdottoBean>) request.getAttribute("prodottiOrdine");
    List<String> imgProdotti = (List<String>) request.getAttribute("imgProdotti");
    if (prodottiOrdine == null) {
        response.sendRedirect("./OrdineControl?action=visualizzaDettagliOrdini");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/dettagliOrdine.css" rel="stylesheet" type="text/css">
    <title>WrestleWorld | Dettagli Ordine</title>
</head>
<body>
<%@ include file="navbar.jsp"%>


<% if("Utente".equals(tipoUtente)) { %>
<h2 style="color: blue">Dettagli del tuo ordine</h2>
<h4 style="color: gray">Qui troverai tutti i prodotti acquistati in quest'ordine.<br>
    Se desideri acquistare nuovamente uno di questi prodotti puoi aggiungerlo nuovamente al carrello.</h4>
<% } else { %>
<h2 style="color: blue">Dettagli dell'ordine</h2>
<% } %>

<div class="product-order-container">
    <%
        if(prodottiOrdine != null && !prodottiOrdine.isEmpty()) {
            for(int i = 0; i < prodottiOrdine.size(); i++) {
                ProdottoBean prod = prodottiOrdine.get(i);
                String imgProdotto = imgProdotti.get(i);

                if (prod != null) {
                    BigDecimal prezzoOrdine = new BigDecimal(prod.getPrezzoProdotto());
                    int euroOrdine = prezzoOrdine.intValue();
                    int centesimiOrdine = prezzoOrdine.remainder(BigDecimal.ONE).movePointRight(2).intValue();
    %>

    <div class="product">
        <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
            <img src="img/prodotti/<%=imgProdotto%>" alt="IMG Error" class="product-img">
        </a>
        <div class="product-details">
            <div class="price">
                <span class="product-price">
                    <span class="euro"><%= euroOrdine %></span><span class="decimal">,<%= String.format("%02d", centesimiOrdine) %></span>&euro;
                </span>
            </div>
            <div class="name">
                <span class="product-name">
                    <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                        <%= prod.getNomeProdotto() %>
                    </a>
                </span>
            </div>
            <div class="quantity">
                <span class="product-quantity">
                    <span class="order-quantity"><b>Quantità:</b> <%= prod.getQuantitaCarrello() %></span>
                </span>
            </div>
            <div class="size">
                <span class="product-size">
                    <span class="order-size"><b>Taglia:</b> <%= prod.getTagliaSelezionata() %></span>
                </span>
            </div>
            <div class="button">
                <div class="button-layer"></div>
                <button>
                    <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%= ((ProdottoBean) prod).getIDProdotto() %>">Visualizza prodotto</a>
                </button>
            </div>
        </div>
    </div>
    <% } } } else {
        request.setAttribute("error", "Errore verificatori durante il caricamento dei dettagli dell'ordine.");
        response.sendRedirect("page500.jsp");
    } %>
</div>

<%@ include file="footer.jsp"%>
</body>
</html>
