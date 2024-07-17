<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.wrestleworld.model.TagliaProdottoBean" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%
    Object prod = request.getAttribute("prodotto");
    List<String> imgProd = (List<String>) request.getAttribute("imgProd");
    List<TagliaProdottoBean> taglieProd = (List<TagliaProdottoBean>) request.getAttribute("taglieProd");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>WrestleWorld | Pagina prodotto</title>
    <link href="css/paginaProdotto.css" rel="stylesheet" type="text/css">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js" integrity="sha384-UG8ao2jwOWB7/oDdObZc6ItJmwUkR/PfMyt9Qs5AwX7PsnYn1CRKCTWyncPTWvaS" crossorigin="anonymous"></script>
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
            }
        %>
    </div>

    <div class="product-details">
        <p class="product-name"> <%=((ProdottoBean) prod).getNomeProdotto()%>  <br></p>
        <p class="product-description"><%=((ProdottoBean) prod).getDescrizioneProdotto()%>  <br></p>

        <%
            BigDecimal prezzoOriginale = new BigDecimal(((ProdottoBean) prod).getPrezzoProdotto());
            BigDecimal prezzoOfferta = new BigDecimal(((ProdottoBean) prod).getPrezzoOffertaProdotto());
            int euroOriginale = prezzoOriginale.intValue();
            int centesimiOriginale = prezzoOriginale.remainder(BigDecimal.ONE).movePointRight(2).intValue();
            int euroOfferta = prezzoOfferta.intValue();
            int centesimiOfferta = prezzoOfferta.remainder(BigDecimal.ONE).movePointRight(2).intValue();

            BigDecimal prezzoDaUsare;
            if (((ProdottoBean) prod).getPrezzoOffertaProdotto() > 0 && ((ProdottoBean) prod).getPrezzoOffertaProdotto() < ((ProdottoBean) prod).getPrezzoProdotto()) {
                prezzoDaUsare = prezzoOfferta;
        %>
        <p class="product-price">
            <span class="product-price-offerta">
                <span class="euro"><%= euroOfferta %></span><span class="decimal">,<%= String.format("%02d", centesimiOfferta) %></span>&euro;
            </span>
            <span class="product-price-originale">
                <span class="euro"><%= euroOriginale %></span><span class="decimal">,<%= String.format("%02d", centesimiOriginale) %></span>&euro;
            </span>
        </p>
        <%
        } else {
            prezzoDaUsare = prezzoOriginale;
        %>
        <p class="product-price">
            <span class="euro"><%= euroOriginale %></span><span class="decimal">,<%= String.format("%02d", centesimiOriginale) %></span>&euro;
        </p>
        <%
            }
        %>

        <input type="hidden" id="prezzo-da-usare" value="<%= prezzoDaUsare %>">

        <p class="product-marca">Marca: <%=((ProdottoBean) prod).getMarcaProdotto()%>  <br></p>
        <p class="product-materiale">Materiale: <%=((ProdottoBean) prod).getMaterialeProdotto()%>  <br></p>
        <p class="product-modello">Modello: <%=((ProdottoBean) prod).getModelloProdotto()%>  <br></p>
        <p class="product-sizes">Taglie disponibili:</p>
        <div class="select-container">
            <select name="taglie" id="taglia-select" required>
                <option value="" disabled selected>--Seleziona una taglia--</option>
                <%
                    for(TagliaProdottoBean taglia : taglieProd) {
                %>
                <option value="<%= taglia.getTaglia() %>"><%= taglia.getTaglia() %></option>
                <%
                    }
                %>
            </select>
        </div>
        <br><br>
        <button id="add-to-cart-button">Aggiungi al carrello</button>
    </div>
</div>

<script>
    document.getElementById('add-to-cart-button').addEventListener('click', function() {
        var taglia = document.getElementById('taglia-select').value;
        var idProd = '<%= ((ProdottoBean) prod).getIDProdotto() %>';
        var prezzoDaUsare = document.getElementById('prezzo-da-usare').value;
        var link = 'ProdottoControl?action=aggiungiAlCarrello&IDProd=' + idProd + '&taglia=' + taglia + '&prezzo=' + prezzoDaUsare;

        if (!taglia) {
            alert('Seleziona una taglia prima di aggiungere al carrello.');
            return;
        }

        $.ajax({
            type: 'POST',
            url: link,
            success: function(response) {
                var carrello = JSON.parse(response);
                aggiornaVisualizzazioneCarrello(carrello);
                alert('Prodotto aggiunto al carrello con successo!');
            },
            error: function() {
                alert('Errore durante l\'aggiunta del prodotto al carrello.');
            }
        });
    });

    function aggiornaVisualizzazioneCarrello(carrello) {
        // Implementa la logica per aggiornare la visualizzazione del carrello
        // con le informazioni contenute nella variabile `carrello`
        // Ad esempio, puoi aggiornare un contatore del carrello nella navbar
        document.getElementById('cart-count').innerText = carrello.length;
    }
</script>

<%@ include file="footer.jsp"%>
</body>
</html>
