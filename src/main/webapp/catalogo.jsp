<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" language="java" %>

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
  <meta charset="ISO-8859-1">
  <title>WrestleWorld | Catalogo</title>
  <link href="css/index.css" rel="stylesheet" type="text/css">
</head>
<style>
  /* Aggiunta per evitare sovrapposizione con la navbar */
  body {
    margin-top: 70px; /* Altezza della navbar + margine */
    padding-top: 5px; /* Spaziatura sopra il contenuto del body */
  }
</style>
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

          <img src="img/prodotti/<%=img%>" alt="IMG Error" class="product-img">
          <div class="product-details">
            <p class="product-name"> <%= prod.getNomeProdotto() %> <br> </p>
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

</body>
</html>
