<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%
    List<ProdottoBean> bestSellers = (List<ProdottoBean>) request.getAttribute("bestSellers");
    List<String> imgBestProd = (List<String>) request.getAttribute("imgBestProd");
    if(bestSellers == null) {
        response.sendRedirect("./ProdottoControl?action=visualizzaHomePage");
        return;
    }
%>

<%!
    String email = "";
    String tipo = "";
%>

<%
    synchronized (session) {
        session = request.getSession();
        email = (String) session.getAttribute("email");
        tipo = (String) session.getAttribute("tipo");
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="css/index.css" rel="stylesheet" type="text/css">
    <title>WrestleWorld | HomePage</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

    <!-- Poster promo -->
    <section id="carousel">
        <div class="slider-wrapper">
            <div class="slider">
                <img src="img/sitoweb/posterReigns.jpeg" alt="First Slide">
                <img src="img/sitoweb/posterBloodLine.jpeg" alt="Second Slide">
                <img src="img/sitoweb/posterTheRock.jpeg" alt="Third Slide">
            </div>
            <div class="slider-nav">
                <button class="slider-nav-btn" onclick="prevSlide()">&#10094;</button>
                <button class="slider-nav-btn" onclick="nextSlide()">&#10095;</button>
            </div>
        </div>
    </section>
    <script src="js/index.js"></script>

    <!-- Pulsanti promo -->

    <h1>WrestleWorld Migliori Prodotti</h1>

    <div class="product-container">
        <%
            if(bestSellers != null && !bestSellers.isEmpty()) {
                Iterator<?> prodBestIt = bestSellers.iterator();
                Iterator<?> imgBestIt = imgBestProd.iterator();
                while(prodBestIt.hasNext()) {
                    ProdottoBean prod = (ProdottoBean) prodBestIt.next();
                    String imgBest = (String) imgBestIt.next();

                    if (prod != null) {
                        BigDecimal prezzo = new BigDecimal(prod.getPrezzoProdotto());
                        int euro = prezzo.intValue();
                        int centesimi = prezzo.remainder(BigDecimal.ONE).movePointRight(2).intValue();
        %>

        <div class="product">

            <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                <img src="img/prodotti/<%=imgBest%>" alt="IMG Error" class="product-img">
            </a>

            <div class="product-details">

                <div class="price">
                    <span class="product-price">
                        <span class="euro"><%= euro %></span><span class="decimal">,<%= String.format("%02d", centesimi) %></span>&euro;
                    </span>
                </div>

                <div class="name">
                    <span class="product-name">
                        <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                            <%= prod.getNomeProdotto() %>
                        </a>
                    </span>
                </div>

                <div class="button">
                    <div class="button-layer"></div>
                    <button>
                        <a href="ProdottoControl?action=aggiungiAlCarrello&IDProd=<%= ((ProdottoBean) prod).getIDProdotto() %>">Aggiungi al carrello</a>
                    </button>
                </div>

                <%
                    }
                %>
            </div>

        </div>

        <%
                }
            }
        %>
    </div>

    <!-- WrestleWorld Prodotti top 10 prodotti in sconto -->

<%@ include file="footer.jsp" %>
</body>
</html>
