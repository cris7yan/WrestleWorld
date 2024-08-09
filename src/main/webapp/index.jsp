<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page errorPage="page500.jsp" %>

<%
    List<ProdottoBean> bestSellers = (List<ProdottoBean>) request.getAttribute("bestSellers");
    List<String> imgBestProd = (List<String>) request.getAttribute("imgBestProd");

    List<ProdottoBean> bestOnOffer = (List<ProdottoBean>) request.getAttribute("bestOnOffer");
    List<String> imgBestOnOffer = (List<String>) request.getAttribute("imgBestOnOffer");

    if(bestSellers == null && bestOnOffer == null) {
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
    <link href="css/index.css" rel="stylesheet" type="text/css">
    <title>WrestleWorld | HomePage</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

    <!-- Poster promo -->
    <section id="carousel1" class="carousel">
        <div class="slider-wrapper">
            <div class="slider">
                <div class="slide">
                    <a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Roman Reigns">
                        <img src="img/sitoweb/PosterRomanReignsOTC.png" alt="First Slide">
                    </a>
                </div>
                <div class="slide">
                    <a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=The BloodLine">
                        <img src="img/sitoweb/posterBloodLine.jpeg" alt="Second Slide">
                    </a>
                </div>
                <div class="slide">
                    <a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=The Rock">
                        <img src="img/sitoweb/posterTheRock.jpeg" alt="Third Slide">
                    </a>
                </div>
            </div>
            <div class="slider-nav">
                <button class="slider-nav-btn" onclick="prevSlide(1)">&#10094;</button>
                <button class="slider-nav-btn" onclick="nextSlide(1)">&#10095;</button>
            </div>
        </div>
    </section>

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
                        BigDecimal prezzoOriginale = new BigDecimal(prod.getPrezzoProdotto());
                        BigDecimal prezzoOfferta = new BigDecimal(prod.getPrezzoOffertaProdotto());
                        int euroOriginale = prezzoOriginale.intValue();
                        int centesimiOriginale = prezzoOriginale.remainder(BigDecimal.ONE).movePointRight(2).intValue();
                        int euroOfferta = prezzoOfferta.intValue();
                        int centesimiOfferta = prezzoOfferta.remainder(BigDecimal.ONE).movePointRight(2).intValue();
        %>

        <div class="product">

            <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                <img src="img/prodotti/<%=imgBest%>" alt="IMG Error" class="product-img">
            </a>

            <div class="product-details">

                <%  if (prod.getPrezzoOffertaProdotto() > 0 && prod.getPrezzoOffertaProdotto() < prod.getPrezzoProdotto()) {    %>

                <div class="price">
                    <div class="product-price-container">
                        <span class="product-price-offerta">
                            <span class="euro"><%= euroOfferta %></span><span class="decimal">,<%= String.format("%02d", centesimiOfferta) %></span>&euro;
                        </span>
                        <span class="product-price-originale">
                            <span class="euro"><%= euroOriginale %></span><span class="decimal">,<%= String.format("%02d", centesimiOriginale) %></span>&euro;
                        </span>
                    </div>
                </div>
                <%  if(!prod.getDisponibilitaProdotto()) { %>
                <div class="promo-attiva">Non disponibile</div>
                <%  } else  {%>
                <div class="promo-attiva">Promo attiva!</div>
                <% } %>
                <%  } else {   %>
                <div class="price">
                    <span class="product-price">
                        <span class="euro"><%= euroOriginale %></span><span class="decimal">,<%= String.format("%02d", centesimiOriginale) %></span>&euro;
                    </span>
                </div>
                <%  if(!prod.getDisponibilitaProdotto()) { %>
                <div class="promo-attiva">Non disponibile</div>
                <%  }   }  %>
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
                        <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%= ((ProdottoBean) prod).getIDProdotto() %>">Visualizza prodotto</a>
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

    <section id="carousel2" class="carousel">
        <div class="slider-wrapper">
            <div class="slider">
                <div class="slide">
                    <a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=CM Punk">
                        <img src="img/sitoweb/posterCMPunk.jpeg" alt="First Slide">
                    </a>
                </div>
                <div class="slide">
                    <a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Cody Rhodes">
                        <img src="img/sitoweb/posterCody.jpeg" alt="Second Slide">
                    </a>
                </div>
                <div class="slide">
                    <a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Seth%20%22Freakin%22%20Rollins">
                        <img src="img/sitoweb/posterRollins.jpeg" alt="Third Slide">
                    </a>
                </div>
                <div class="slide">
                    <a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Randy Orton">
                        <img src="img/sitoweb/posterOrton.jpeg" alt="Fourth Slide">
                    </a>
                </div>
                <div class="slide">
                    <a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Brock Lesnar">
                        <img src="img/sitoweb/posterLesnar.jpeg" alt="Fifth Slide">
                    </a>
                </div>
            </div>
            <div class="slider-nav">
                <button class="slider-nav-btn" onclick="prevSlide(2)">&#10094;</button>
                <button class="slider-nav-btn" onclick="nextSlide(2)">&#10095;</button>
            </div>
        </div>
    </section>


    <script src="js/index.js"></script>

    <h1>WrestleWorld Migliori in Offerta</h1>

    <div class="product-container">
        <%
            if(bestOnOffer != null && !bestOnOffer.isEmpty()) {
                Iterator<?> prodBestIt = bestOnOffer.iterator();
                Iterator<?> imgBestIt = imgBestOnOffer.iterator();
                while(prodBestIt.hasNext()) {
                    ProdottoBean prod = (ProdottoBean) prodBestIt.next();
                    String imgBest = (String) imgBestIt.next();

                    if (prod != null) {
                        BigDecimal prezzoOriginale = new BigDecimal(prod.getPrezzoProdotto());
                        BigDecimal prezzoOfferta = new BigDecimal(prod.getPrezzoOffertaProdotto());
                        int euroOriginale = prezzoOriginale.intValue();
                        int centesimiOriginale = prezzoOriginale.remainder(BigDecimal.ONE).movePointRight(2).intValue();
                        int euroOfferta = prezzoOfferta.intValue();
                        int centesimiOfferta = prezzoOfferta.remainder(BigDecimal.ONE).movePointRight(2).intValue();
        %>

        <div class="product">

            <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                <img src="img/prodotti/<%=imgBest%>" alt="IMG Error" class="product-img">
            </a>

            <div class="product-details">

                <div class="price">
                    <div class="product-price-container">
                        <span class="product-price-offerta">
                            <span class="euro"><%= euroOfferta %></span><span class="decimal">,<%= String.format("%02d", centesimiOfferta) %></span>&euro;
                        </span>
                        <span class="product-price-originale">
                            <span class="euro"><%= euroOriginale %></span><span class="decimal">,<%= String.format("%02d", centesimiOriginale) %></span>&euro;
                        </span>
                    </div>
                </div>

                <div class="promo-attiva">Promo attiva!</div>

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
                        <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%= ((ProdottoBean) prod).getIDProdotto() %>">Visualizza prodotto</a>
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

    <div class="block-img-container">
        <div class="block-img">
            <a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Roman Reigns">
                <img src="img/sitoweb/blockPosterReigns.jpeg" alt="Poster Reigns">
            </a>
        </div>
        <div class="block-img">
            <a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=The Undertaker">
                <img src="img/sitoweb/blockPosterUndertaker.jpeg" alt="Poster Undertaker">
            </a>
        </div>
    </div>

    <br><br><br>

<%@ include file="footer.jsp" %>
</body>
</html>
