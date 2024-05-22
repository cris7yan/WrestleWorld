<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" language="java" %>

<%
    Object prod = request.getAttribute("prodotto");
    List<String> imgProd = (List<String>) request.getAttribute("imgProd");
%>

<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="ISO-8859-1">
  <title>WrestleWorld | Pagina prodotto</title>
  <link href="css/paginaProdotto.css" rel="stylesheet" type="text/css">
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

<div class="product-container">

    <div class="product-img">
      <%
        if(prod instanceof ProdottoBean) {
          for(String img : imgProd) {
      %>

      <img src="img/prodotti/<%=img%>" alt="IMG Error" class="product-img">

      <%
        }
      %>
    </div>

    <div class="product-details">
      <p class="product-name"> <%=((ProdottoBean) prod).getNomeProdotto()%>  <br></p>
      <p class="product-description"><%=((ProdottoBean) prod).getDescrizioneProdotto()%>  <br></p>
      <p class="product-price"><%=((ProdottoBean) prod).getPrezzoProdotto()%>&euro; <br></p>
      <p class="product-marca">Marca: <%=((ProdottoBean) prod).getMarcaProdotto()%>  <br></p>
      <p class="product-materiale">Materiale: <%=((ProdottoBean) prod).getMaterialeProdotto()%>  <br></p>
      <p class="product-modello">Modello: <%=((ProdottoBean) prod).getModelloProdotto()%>  <br></p>
      <br><br>
    </div>

  <%
    }
  %>

</div>

</body>
</html>
