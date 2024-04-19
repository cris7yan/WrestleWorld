<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" language="java" %>

<%
    List<ProdottoBean> prodotti = (List<ProdottoBean>) request.getAttribute("prodotti");
    if(prodotti == null) {
        response.sendRedirect("./ProdottoControl");
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
    <meta charset="ISO-8859-1">
    <title>WrestleWorld | HomePage</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

    <h1>Prodotti</h1>

    <%
        if(prodotti != null && !prodotti.isEmpty()) {
            for (int i = 0; i < prodotti.size(); i++) {
                ProdottoBean prod = prodotti.get(i);
    %>

    <%
        if(prod != null) {
    %>

        Nome: <b><%= prod.getNomeProdotto() %></b> <br>
        Descrizione: <i><%= prod.getDescrizioneProdotto() %></i> <br>
        Prezzo: <%= prod.getPrezzoProdotto() %>
        <br><br>

    <%
        }
    %>

    <%
            }
        }
    %>

</body>
</html>
