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

    <div class="product-container">
        <%
            if (prodottiCategoria != null && !prodottiCategoria.isEmpty()) {
                Iterator<?> prodIt = prodottiCategoria.iterator();
                Iterator<?> imgIt = imgProdottiCategoria.iterator();
                while (prodIt.hasNext()) {
                    ProdottoBean prod = (ProdottoBean) prodIt.next();
                    String img = (String) imgIt.next();

                    if (prod != null) {
                        BigDecimal prezzo = new BigDecimal(prod.getPrezzoProdotto());
                        int euro = prezzo.intValue();
                        int centesimi = prezzo.remainder(BigDecimal.ONE).movePointRight(2).intValue();
        %>

        <div class="product">

            <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                <img src="img/prodotti/<%=img%>" alt="IMG Error" class="product-img">
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


<%@ include file="footer.jsp"%>
</body>
</html>
