<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<ProdottoBean> prodottiCategoria = (List<ProdottoBean>) request.getAttribute("prodottiCategoria");
    List<String> imgProdottiCategoria = (List<String>) request.getAttribute("imgProdottiCategoria");
    if(prodottiCategoria == null) {
        response.sendRedirect("./ProdottoControl?action=visualizzaProdottiCategoria");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/catalogo.css" rel="stylesheet" type="text/css">
    <title>WrestleWorld | Prodotti di Categoria</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div class="catalog-container">

    <div class="filters">
        <div class="filter-section">
            <h3>Genere</h3>
            <div class="filter-option">
                <input type="radio" id="Uomo" name="gender">
                <label for="Uomo">Uomo</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="Donna" name="gender">
                <label for="Donna">Donna</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="Unisex" name="gender">
                <label for="Unisex">Unisex</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <div class="filter-option">
                <input type="checkbox" id="Firmato">
                <label for="Firmato">Firmati</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <h3>Marca</h3>
            <div class="filter-option">
                <input type="checkbox" id="brand1">
                <label for="brand1">Antigua</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="brand2">
                <label for="brand2">Chalk Line</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="brand3">
                <label for="brand3">Fanatics Authentic</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="brand4">
                <label for="brand4">Fanatics Branded</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="brand5">
                <label for="brand5">Funko Pop</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="brand6">
                <label for="brand6">Keyscaper</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="brand7">
                <label for="brand7">WWE Authentic</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <h3>Range di prezzo</h3>
            <div class="filter-option">
                <input type="radio" id="0-50" name="price">
                <label for="0-50">0 - 50</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="51-100" name="price">
                <label for="51-100">51 - 100</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="101-500" name="price">
                <label for="101-500">101 - 500</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="501-" name="price">
                <label for="501-">500+</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <div class="filter-option">
                <input type="checkbox" id="on-sale">
                <label for="on-sale">In offerta</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <div class="filter-option">
                <input type="checkbox" id="disponibile" name="disponibile">
                <label for="disponibile">Solo disponibili</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-apply">
            <button id="apply-filters">Applica filtri</button>
        </div>
    </div>

    <div class="product-container">
        <%
            if (prodottiCategoria != null && !prodottiCategoria.isEmpty()) {
                Iterator<?> prodIt = prodottiCategoria.iterator();
                Iterator<?> imgIt = imgProdottiCategoria.iterator();
                while (prodIt.hasNext()) {
                    ProdottoBean prod = (ProdottoBean) prodIt.next();
                    String img = (String) imgIt.next();

                    if (prod != null) {
                        BigDecimal prezzoOriginale = new BigDecimal(prod.getPrezzoProdotto());
                        BigDecimal prezzoOfferta = new BigDecimal(prod.getPrezzoOffertaProdotto());
                        int euroOriginale = prezzoOriginale.intValue();
                        int centesimiOriginale = prezzoOriginale.remainder(BigDecimal.ONE).movePointRight(2).intValue();
                        int euroOfferta = prezzoOfferta.intValue();
                        int centesimiOfferta = prezzoOfferta.remainder(BigDecimal.ONE).movePointRight(2).intValue();
        %>

        <div class="product"
             data-gender="<%= prod.getSessoProdotto() %>"
             data-brand="<%= prod.getMarcaProdotto() %>"
             data-category="<%= prod.getCategoriaProdotto() %>"
             data-signed="<%= prod.prodottoFirmato() %>"
             data-price="<%= prod.getPrezzoVenditaProdotto() %>"
             data-on-sale="<%= prod.getPrezzoOffertaProdotto() > 0 && prod.getPrezzoOffertaProdotto() < prod.getPrezzoProdotto() %>"
             data-availability="<%= prod.getDisponibilitaProdotto() %>"
        >

            <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                <img src="img/prodotti/<%=img%>" alt="IMG Error" class="product-img">
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

                <div class="promo-attiva">Promo attiva!</div>

                <%  } else {   %>

                <div class="price">
                    <span class="product-price">
                        <span class="euro"><%= euroOriginale %></span><span class="decimal">,<%= String.format("%02d", centesimiOriginale) %></span>&euro;
                    </span>
                </div>

                <%  }   %>

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
            } else {
        %>
        <div class="error-message">
            <p>Nessun prodotto disponibile per questa categoria.</p>
            <p>A breve saranno disponibili.</p>
        </div>
        <%
            }
        %>
    </div>
</div>

<script src="js/gestioneFiltro.js"></script>

<%@ include file="footer.jsp"%>
</body>
</html>
