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

    <div class="product-container">
        <%
            if (prodotti != null && !prodotti.isEmpty()) {
                Iterator<?> prodIt = prodotti.iterator();
                Iterator<?> imgIt = imgProdotti.iterator();
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
                }
            %>
    </div>

<%@ include file="footer.jsp"%>
</body>
</html>
