<%@ page import="java.util.Iterator" %><%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.CategoriaBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<CategoriaBean> superstar = (List<CategoriaBean>) request.getAttribute("superstar");
    List<CategoriaBean> ple = (List<CategoriaBean>) request.getAttribute("ple");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/categorie.css" rel="stylesheet" type="text/css">
    <title>WrestleWorld | Categorie</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

    <div class="poster">
        <img src="img/sitoweb/WWESuperstar.jpg" alt="Superstar Poster">
    </div>

    <div id="superstar-section" class="category-section">
        <h2 class="category-title">Superstar</h2>
        <div class="category-container">
            <% if (superstar != null && !superstar.isEmpty()) {
                Iterator<?> superstarIt = superstar.iterator();
                while (superstarIt.hasNext()) {
                CategoriaBean ss = (CategoriaBean) superstarIt.next();
            %>
            <div class="category-box superstar-box">
                <a href="#"><img src="img/categorie/<%= ss.getImg() %>" alt="<%= ss.getNome() %>"></a>
                <div class="category-overlay"></div>
                <div class="category-name"><%= ss.getNome() %></div>
            </div>
            <%
                }
                } else {
            %>
            <div class="error-message">
                <p>Nessun elemento presente</p>
            </div>
            <% } %>
        </div>
    </div>

    <div id="ple-section" class="category-section">
        <h2 class="category-title">Premium Live Event</h2>
        <div class="category-container">
            <% if (ple != null && !ple.isEmpty()) {
                 Iterator<?> pleIt = ple.iterator();
                 while (pleIt.hasNext()) {
                    CategoriaBean plev = (CategoriaBean) pleIt.next();
            %>
            <div class="category-box ple-box">
                <a href="#"><img src="img/categorie/<%= plev.getImg() %>" alt="<%= plev.getNome() %>"></a>
                <div class="category-overlay"></div>
                <div class="category-name"><%= plev.getNome() %></div>
            </div>
            <%  }
                } else {
            %>
            <div class="error-message">
                <p>Nessun elemento presente</p>
            </div>
            <% } %>
        </div>
    </div>


<%@ include file="footer.jsp"%>
</body>
</html>
