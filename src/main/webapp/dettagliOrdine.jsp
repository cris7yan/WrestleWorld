<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<ProdottoBean> prodottiOrdine = (List<ProdottoBean>) request.getAttribute("prodottiOrdine");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>WrestleWorld | Dettagli Ordine</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

    <%
        if(prodottiOrdine != null && !prodottiOrdine.isEmpty()) {
            for(ProdottoBean prod : prodottiOrdine) {
    %>

        <%= ((ProdottoBean) prod).getNomeProdotto()%>
        <%= ((ProdottoBean) prod).getPrezzoProdotto()%> <br>
        <%= ((ProdottoBean) prod).getQuantitaCarrello() %> <br>

    <%
            }
        }
    %>

<%@ include file="footer.jsp"%>
</body>
</html>
