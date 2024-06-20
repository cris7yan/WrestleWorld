<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
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
    <title>WrestleWorld | HomePage</title>
    <link href="css/index.css" rel="stylesheet" type="text/css">
    <link href="css/profiloUtente.css" rel="stylesheet" type="text/css">
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

<div>
    <h1>BEST PRODOTTI</h1>

    <div class="product-container">
        <%
            if(bestSellers != null && !bestSellers.isEmpty()) {
                Iterator<?> prodBestIt = bestSellers.iterator();
                Iterator<?> imgBestIt = imgBestProd.iterator();
                while(prodBestIt.hasNext()) {
                    ProdottoBean prod = (ProdottoBean) prodBestIt.next();
                    String imgBest = (String) imgBestIt.next();
        %>

        <div class="product">
            <%
                if(prod != null) {
            %>

            <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                <img src="img/prodotti/<%=imgBest%>" alt="IMG Error" class="product-img">
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

</div>

<%@ include file="footer.jsp"%>
</body>
</html>
