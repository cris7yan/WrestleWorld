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
            <h3>Superstar</h3>
            <div class="filter-option">
                <input type="checkbox" id="cat1">
                <label for="cat1">Superstar 1</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Superstar 2</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Superstar 3</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <h3>Genere</h3>
            <div class="filter-option">
                <input type="checkbox" id="cat1">
                <label for="cat1">Uomo</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Donna</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Unisex</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <h3>Categoria</h3>
            <div class="filter-option">
                <input type="checkbox" id="cat1">
                <label for="cat1">Abbigliamento</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Accessori</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Collezionabili</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Firmati</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Title Belts</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <h3>Marca</h3>
            <div class="filter-option">
                <input type="checkbox" id="cat1">
                <label for="cat1">Marca 1</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Marca 2</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Marca 3</label>
            </div>
        </div>
        <hr class="filter-divider">

        <div class="filter-section">
            <h3>Prezzo</h3>
            <div class="filter-option">
                <input type="checkbox" id="cat1">
                <label for="cat1">Categoria 1</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">Categoria 2</label>
            </div>
            <div class="filter-option">
                <input type="checkbox" id="cat2">
                <label for="cat2">In offerta</label>
            </div>
        </div>
        <hr class="filter-divider">

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

<%@ include file="footer.jsp"%>
</body>
</html>
