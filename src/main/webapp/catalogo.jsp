<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
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
  <link href="css/index.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@ include file="navbar.jsp"%>

    <h1>PRODOTTI</h1>

    <div class="product-container">
        <%
          if(prodotti != null && !prodotti.isEmpty()) {
            Iterator<?> prodIt = prodotti.iterator();
            Iterator<?> imgIt = imgProdotti.iterator();
            while(prodIt.hasNext()) {
              ProdottoBean prod = (ProdottoBean) prodIt.next();
              String img = (String) imgIt.next();
        %>

        <div class="product">
          <%
            if(prod != null) {
          %>

          <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
              <img src="img/prodotti/<%=img%>" alt="IMG Error" class="product-img">
          </a>
          <div class="product-details">
              <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                  <p class="product-name"> <%= prod.getNomeProdotto() %> <br> </p>
              </a>
              <p class="product-price"> <i><%= prod.getPrezzoProdotto() %></i>&euro; <br> </p>
              <p class="product-description"> <i><%= prod.getDescrizioneProdotto() %> <br> </p>
              <br><br>
          </div>

          <%
            }
          %>
        </div>

        <%
            }
          }
        %>
    </div>

<%@ include file="footer.jsp"%>
</body>
</html>
