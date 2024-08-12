<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.unisa.wrestleworld.model.IndirizzoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.wrestleworld.model.MetodoPagamentoBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    List<IndirizzoBean> indirizziUtente = (List<IndirizzoBean>) request.getAttribute("indirizziUtente");
    List<MetodoPagamentoBean> metodiPagamentoUtente = (List<MetodoPagamentoBean>) request.getAttribute("metodiPagamentoUtente");

    if(indirizziUtente == null || metodiPagamentoUtente == null) {
        response.sendRedirect("./OrdineControl?action=visualizzaDatiUtente");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/paginaAcquisto.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Pagina di acquisto</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div class="cart-container">
    <%
        if (carrello != null && !carrello.getCarrello().isEmpty()) {
            float prezzoTotaleCarrello = carrello.getPrezzoCarrello();
    %>
    <div class="cart-details">
        <h2>Prodotti che stai acquistando:</h2>
        <%
            for (ProdottoBean prod : carrello.getCarrello()) {
                BigDecimal prezzoOriginale = new BigDecimal(prod.getPrezzoProdotto());
                BigDecimal prezzoOfferta = new BigDecimal(prod.getPrezzoOffertaProdotto());
                BigDecimal prezzoDaMostrare;

                if (prezzoOfferta.compareTo(BigDecimal.ZERO) > 0 && prezzoOfferta.compareTo(prezzoOriginale) < 0) {
                    prezzoDaMostrare = prezzoOfferta;
                } else {
                    prezzoDaMostrare = prezzoOriginale;
                }

                prezzoDaMostrare = prezzoDaMostrare.setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal prezzoTotaleProdotto = prezzoDaMostrare.multiply(new BigDecimal(prod.getQuantitaCarrello())).setScale(2, BigDecimal.ROUND_HALF_UP);
        %>
        <div class="product-details">
            <a href="ProdottoControl?action=visualizzaDettagliProdotto&IDProd=<%=((ProdottoBean) prod).getIDProdotto()%>">
                <p class="product-name"><%= prod.getNomeProdotto() %></p>
            </a>
            <p class="product-price"><%= prezzoDaMostrare %>€</p>
            <p class="product-size"><%= prod.getTagliaSelezionata() %></p>
            <p class="product-quantity"><%= prod.getQuantitaCarrello() %></p>
            <p class="product-total-price"><%= prezzoTotaleProdotto %>€</p>
        </div>
        <% } %>
        <div class="total-price">
            <h3>Prezzo Totale: <%= new BigDecimal(prezzoTotaleCarrello).setScale(2, BigDecimal.ROUND_HALF_UP) %>€</h3>
            <h3 style="color: red">Spedizione: 5€</h3>
        </div>
    </div>
    <% } %>
</div>

<h2 style="color: blue">Completa l'operazione di acquisto</h2>
<h5 style="color: red">Seleziona un indirizzo ed un metodo di pagamento per completare l'operazione</h5>

<% if(indirizziUtente.isEmpty() && metodiPagamentoUtente.isEmpty()) { %>
<p style="color: red; text-align: center; font-width: bold;">Aggiungi prima un indirizzo ed un metodo di pagamento per effettuare l'acquisto.</p>
<p style="color: red; text-align: center; font-width: bold;">Clicca <a href="./indirizzi.jsp">qui</a> per aggiungere un indirizzo</p>
<p style="color: red; text-align: center; font-width: bold;">Clicca <a href="./metodiPagamento.jsp">qui</a> per aggiungere un metodo di pagamento</p>
<% } else if (indirizziUtente.isEmpty()) { %>
<p style="color: red; text-align: center; font-width: bold;">Aggiungi prima un indirizzo per effettuare l'acquisto.</p>
<p style="color: red; text-align: center; font-width: bold;">Clicca <a href="./indirizzi.jsp">qui</a> per aggiungere un indirizzo</p>
<% } else if (metodiPagamentoUtente.isEmpty()) { %>
<p style="color: red; text-align: center; font-width: bold;">Aggiungi prima un metodo di pagamento per effettuare l'acquisto.</p>
<p style="color: red; text-align: center; font-width: bold;">Clicca <a href="./metodiPagamento.jsp">qui</a> per aggiungere un metodo di pagamento</p>
<% } %>

<div class="acquisto-container">
    <form id="checkoutForm" action="OrdineControl?action=checkout" method="post">
        <h3 class="section-title">I tuoi indirizzi:</h3>
        <div class="options">
            <% for (IndirizzoBean indirizzo : indirizziUtente) { %>
            <input type="radio" name="indirizzoScelto" value="<%=indirizzo.getIdIndirizzo()%>" id="indirizzo_<%=indirizzo.getIdIndirizzo()%>" required>
            <label for="indirizzo_<%=indirizzo.getIdIndirizzo()%>">
                <strong>Via:</strong> <%=indirizzo.getViaIndirizzo()%>,
                <strong>Città:</strong> <%=indirizzo.getCittaIndirizzo()%>,
                <strong>Provincia:</strong> <%=indirizzo.getProvinciaIndirizzo()%>,
                <strong>Destinatario:</strong> <%=indirizzo.getNomeCompletoIndirizzo()%>
            </label>
            <% } %>
        </div>

        <h3 class="section-title">I tuoi metodi di pagamento:</h3>
        <div class="options">
            <% for (MetodoPagamentoBean metodo : metodiPagamentoUtente) { %>
            <input type="radio" name="metodoScelto" value="<%=metodo.getIdMetodoPagamento()%>" id="metodo_<%=metodo.getIdMetodoPagamento()%>" required>
            <label for="metodo_<%=metodo.getIdMetodoPagamento()%>">
                <strong>Numero carta:</strong> <%=metodo.getNumeroCarta()%>,
                <strong>Intestatario:</strong> <%=metodo.getIntestatario()%>,
                <strong>Scadenza:</strong> <%=metodo.getDataScadenza()%>
            </label>
            <% } %>
        </div>

        <button class="custom-btn-2 btn" type="submit">Acquista</button>
        <h2 class="warning-message" id="warningMessage">Prima di effettuare l'acquisto, seleziona un indirizzo e un metodo di pagamento.</h2>
    </form>
</div><br><br>

<script>
    document.getElementById('checkoutForm').addEventListener('submit', function() {
        document.getElementById('searchForm').querySelector('.search-input').disabled = true;
        document.getElementById('searchForm').querySelector('.search-button').disabled = true;
    });

    document.getElementById('checkoutForm').addEventListener('submit', function(event) {
        var indirizzoSelezionato = document.querySelector('input[name="indirizzoScelto"]:checked');
        var metodoSelezionato = document.querySelector('input[name="metodoScelto"]:checked');
        var warningMessage = document.getElementById('warningMessage');

        if (!indirizzoSelezionato || !metodoSelezionato) {
            event.preventDefault();
            warningMessage.style.display = 'block';
        } else {
            warningMessage.style.display = 'none';
        }
    });
</script>

<%@ include file="footer.jsp" %>
</body>
</html>
