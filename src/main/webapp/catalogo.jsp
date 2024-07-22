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
    List<ProdottoBean> prodotti = (List<ProdottoBean>) request.getAttribute("prodotti");
    List<String> imgProdotti = (List<String>) request.getAttribute("imgProdotti");
    if(prodotti == null) {
      response.sendRedirect("./ProdottoControl?action=visualizzaCatalogo");
      return;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>WrestleWorld | Catalogo</title>
    <link href="css/catalogo.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@ include file="navbar.jsp"%>

<h1>WrestleWorld Catalogo</h1>

<div class="catalog-container">

    <div class="filters">
        <div class="filter-section">
            <h3>Superstar</h3>
            <div class="filter-option">
                <input type="checkbox" id="superstar1">
                <label for="superstar1">Superstar 1</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="superstar2">
                <label for="superstar2">Superstar 2</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="superstar3">
                <label for="superstar3">Superstar 3</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <h3>Genere</h3>
            <div class="filter-option">
                <input type="radio" id="male" name="gender">
                <label for="male">Uomo</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="female" name="gender">
                <label for="female">Donna</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="unisex" name="gender">
                <label for="unisex">Unisex</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <h3>Categoria</h3>
            <div class="filter-option">
                <input type="checkbox" id="clothing">
                <label for="clothing">Abbigliamento</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="accessories">
                <label for="accessories">Accessori</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="collectibles">
                <label for="collectibles">Collezionabili</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="signed">
                <label for="signed">Firmati</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="title-belts">
                <label for="title-belts">Title Belts</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <h3>Marca</h3>
            <div class="filter-option">
                <input type="checkbox" id="brand1">
                <label for="brand1">Marca 1</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="brand2">
                <label for="brand2">Marca 2</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="brand3">
                <label for="brand3">Marca 3</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <h3>Prezzo</h3>
            <div class="filter-option">
                <input type="radio" id="under-10" name="price">
                <label for="under-10">< 10€</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="10-50" name="price">
                <label for="10-50">da 10€ a 50€</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="50-75" name="price">
                <label for="50-75">da 50€ a 75€</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="75-100" name="price">
                <label for="75-100">da 75€ a 100€</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="100-200" name="price">
                <label for="100-200">da 100€ a 200€</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="200-500" name="price">
                <label for="200-500">da 200€ a 500€</label>
            </div>
            <div class="filter-option">
                <input type="radio" id="over-500" name="price">
                <label for="over-500">> 500€</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="on-sale">
                <label for="on-sale">In offerta</label>
            </div>
        </div>

        <div class="filter-apply">
            <button id="apply-filters">Applica filtri</button>
        </div>
    </div>

    <div class="product-container">
        <% if (prodotti != null && !prodotti.isEmpty()) {
            Iterator<?> prodIt = prodotti.iterator();
            Iterator<?> imgIt = imgProdotti.iterator();
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

        <div class="product">
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
            </div>
        </div>

        <% }    }   } %>
    </div>
</div>

<%@ include file="footer.jsp"%>
</body>
</html>
