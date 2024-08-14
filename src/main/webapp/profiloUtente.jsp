<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.MetodoPagamentoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.wrestleworld.model.IndirizzoBean" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page errorPage="page500.jsp" %>

<%
    session = request.getSession();
    String tipoUtente = (String) session.getAttribute("tipo");
    if (tipoUtente == null) {
        response.sendRedirect("page403.jsp");
        return;
    }
%>

<%
    List<MetodoPagamentoBean> metodi = (List<MetodoPagamentoBean>) request.getAttribute("metodiPagamento");
    List<IndirizzoBean> indirizzi = (List<IndirizzoBean>) request.getAttribute("indirizzi");
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <link href="css/profiloUtente.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Profilo Utente</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

<% if("Utente".equals(tipoUtente)) { %>
<div class="top-links">
    <a href="./modificaDatiPersonali.jsp">Dati Personali</a>
    <a href="./modificaDatiAccesso.jsp">Dati di accesso</a>
    <a href="./ordini.jsp">I tuoi ordini</a>
</div>
<% } else if("Admin".equals(tipoUtente)) { %>
<div class="top-links">
    <a href="./modificaDatiPersonali.jsp">Dati Personali</a>
    <a href="./adminUtenti.jsp">Visualizza utenti</a>
    <a href="./adminOrdini.jsp">Visualizza ordini</a>
</div>
<% } %>

<% if("Utente".equals(tipoUtente)) { %>
<div class="header-container">
    <h1>Benvenuto nel tuo profilo utente</h1>
</div>
<% } else if("Admin".equals(tipoUtente)) { %>
<div class="header-container">
    <h1>Benvenuto nel tuo profilo admin</h1>
</div>
<% } %>

<% if("Utente".equals(tipoUtente)) { %>
<div class="dati-container">
    <p class="nome-cognome"><span class="value"><%=nome%> <%=cognome%></span></p>
    <p class="email"><span class="label">email:</span> <span class="value"><%=emailUtente%></span></p>
    <p class="data-nascita"><span class="label">data di nascita:</span> <span class="value"><%=dataNascita%></span></p>
</div>
<% } else if("Admin".equals(tipoUtente)) { %>
<div class="dati-container">
    <p class="nome-cognome"><span class="value"><%=nome%> <%=cognome%></span></p>
    <p class="email"><span class="label">email:</span> <span class="value"><%=emailUtente%></span></p>
</div>
<% } %>

<div class="link-container">
    <% if ("Admin".equals(tipoUtente)) { %>
    <div class="box-container">
        <a href="./nuovoProdotto.jsp">
            <div class="box">
                <img src="img/sitoweb/plus-circle.png" alt="Aggiungi prodotto" class="box-image">
                <span class="text">Aggiungi un nuovo prodotto</span>
            </div>
        </a>
        <a href="./catalogo.jsp">
            <div class="box">
                <img src="img/sitoweb/eye.png" alt="Visualizza prodotti" class="box-image">
                <span class="text">Visualizza prodotti</span>
            </div>
        </a><br>
        <a href="./nuovaCategoria.jsp">
            <div class="box">
                <img src="img/sitoweb/plus-circle.png" alt="Aggiungi prodotto" class="box-image">
                <span class="text">Aggiungi una nuova categoria</span>
            </div>
        </a>
        <a href="./categorie.jsp">
            <div class="box">
                <img src="img/sitoweb/eye.png" alt="Visualizza categorie" class="box-image">
                <span class="text">Visualizza categoria</span>
            </div>
        </a>
    </div>
    <% } else if("Utente".equals(tipoUtente)) { %>
    <div class="box-container">
        <a href="./indirizzi.jsp#add-indirizzi">
            <div class="box">
                <img src="img/sitoweb/plus-circle.png" alt="Aggiungi indirizzo" class="box-image">
                <span class="text">Aggiungi indirizzo</span>
            </div>
        </a>
        <a href="./indirizzi.jsp#view-indirizzi">
            <div class="box">
                <img src="img/sitoweb/eye.png" alt="Visualizza indirizzi" class="box-image">
                <span class="text">Visualizza i tuoi indirizzi</span>
            </div>
        </a><br>
        <a href="./metodiPagamento.jsp#add-payment-method">
            <div class="box">
                <img src="img/sitoweb/plus-circle.png" alt="Aggiungi metodo di pagamento" class="box-image">
                <span class="text">Aggiungi metodo di pagamento</span>
            </div>
        </a>
        <a href="./metodiPagamento.jsp#view-payment-method">
            <div class="box">
                <img src="img/sitoweb/eye.png" alt="Visualizza metodi di pagamento" class="box-image">
                <span class="text">Visualizza i tuoi metodi di pagamento</span>
            </div>
        </a>
    </div>
    <% } %>
</div>

<%@ include file="footer.jsp"%>
</body>
</html>
