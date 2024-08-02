<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="it.unisa.wrestleworld.model.TagliaProdottoBean" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%
    Object prod = request.getAttribute("prodotto");
    List<String> imgProd = (List<String>) request.getAttribute("imgProd");
    List<TagliaProdottoBean> taglieProd = (List<TagliaProdottoBean>) request.getAttribute("taglieProd");

    List<String> ordineTaglie = Arrays.asList("XS", "S", "M", "L", "XL", "XXL", "XXXL");
    taglieProd.sort((taglia1, taglia2) -> {
        int index1 = ordineTaglie.indexOf(taglia1.getTaglia());
        int index2 = ordineTaglie.indexOf(taglia2.getTaglia());
        return Integer.compare(index1, index2);
    });
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>WrestleWorld | Pagina prodotto</title>
    <link href="css/paginaProdotto.css" rel="stylesheet" type="text/css">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js" integrity="sha384-UG8ao2jwOWB7/oDdObZc6ItJmwUkR/PfMyt9Qs5AwX7PsnYn1CRKCTWyncPTWvaS" crossorigin="anonymous"></script>
    <script src="js/funzioniAdmin.js" type="text/javascript"></script>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div class="product-container">
    <div class="product-img">
        <%
            if (prod instanceof ProdottoBean) {
                for (String img : imgProd) {
        %>
        <img src="img/prodotti/<%= img %>" alt="IMG Error" class="product-img">
        <%
                }
            }
        %>
    </div>

    <div class="product-details">
        <p class="product-name"><%= ((ProdottoBean) prod).getNomeProdotto() %>  <br></p>
        <p class="product-description"><%= ((ProdottoBean) prod).getDescrizioneProdotto() %>  <br></p>

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

        <%-- Sezione per l'Admin --%>
        <% if ("Admin".equals(tipoUtente)) { %>
        <div class="admin-price-update-container">
            <button onclick="mostraModificaPrezzo()">Modifica Prezzo</button>
            <button onclick="mostraModificaPrezzoOfferta()">Modifica Prezzo Offerta</button>
        </div>
        <div id="modifica-prezzo" style="display:none;">
            <input type="number" id="nuovo-prezzo" placeholder="Nuovo Prezzo">
            <button onclick="modificaPrezzo('<%= ((ProdottoBean) prod).getIDProdotto() %>')">Aggiorna Prezzo</button>
        </div>
        <div id="modifica-prezzo-offerta" style="display:none;">
            <input type="number" id="nuovo-prezzo-offerta" placeholder="Nuovo Prezzo Offerta">
            <button onclick="modificaPrezzoOfferta('<%= ((ProdottoBean) prod).getIDProdotto() %>')">Aggiorna Prezzo Offerta</button>
        </div>
        <% } %>

        <p class="product-marca">Marca: <%= ((ProdottoBean) prod).getMarcaProdotto() %>  <br></p>
        <p class="product-materiale">Materiale: <%= ((ProdottoBean) prod).getMaterialeProdotto() %>  <br></p>
        <p class="product-modello">Modello: <%= ((ProdottoBean) prod).getModelloProdotto() %>  <br></p>

        <%
            boolean disponibilita = ((ProdottoBean) prod).getDisponibilitaProdotto();
            if (!disponibilita) {
        %>
        <p class="out-of-stock">Questo prodotto non è attualmente disponibile.</p>
        <%
            }
        %>

        <div class="admin-add-category-container">
            <button onclick="mostraAggiungiCategoria()">Aggiungi Categoria</button>
        </div>
        <div id="aggiungi-categoria" style="display:none;">
            <h3>Aggiungi una nuova appartenenza</h3>
            <input type="text" id="nuova-categoria" placeholder="Nome Categoria">
            <button onclick="aggiungiAppartenenza('<%= ((ProdottoBean) prod).getIDProdotto() %>')">Aggiungi Appartenenza</button>
        </div>

        <p class="product-sizes">Taglie disponibili:</p>
        <% if ("Admin".equals(tipoUtente)) { %>
        <div class="admin-quantita-container">
            <table>
                <tr>
                    <th>Taglia</th>
                    <th>Quantità</th>
                    <th>Aggiungi quantità</th>
                </tr>
                <%
                    for (TagliaProdottoBean taglia : taglieProd) {
                %>
                <tr>
                    <td><%= taglia.getTaglia() %></td>
                    <td><%= taglia.getQuantita() %></td>
                    <td>
                        <input type="number" id="quantita-<%= taglia.getTaglia() %>" placeholder="Quantità">
                        <button onclick="aggiungiQuantita('<%= ((ProdottoBean) prod).getIDProdotto() %>', '<%= taglia.getTaglia() %>')">Aggiungi quantità</button>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>

            <div class="admin-add-size-container">
                <button onclick="mostraAggiungiTaglia()">Aggiungi una nuova taglia</button>
            </div>
            <div id="aggiungi-taglia" style="display:none;">
                <h3>Aggiungi una nuova taglia</h3>
                <input type="text" id="nuova-taglia" placeholder="Nuova Taglia">
                <input type="number" id="quantita-taglia" placeholder="Quantità">
                <button onclick="aggiungiTaglia('<%= ((ProdottoBean) prod).getIDProdotto() %>')">Aggiungi Taglia</button>
            </div>

            <button id="delete-product-button" onclick="eliminaProdotto('<%= ((ProdottoBean) prod).getIDProdotto() %>')">Elimina prodotto</button>
            <button id="make-unavailable-button" onclick="rendiIndisponibileProdotto('<%= ((ProdottoBean) prod).getIDProdotto() %>')" style="<%= disponibilita ? "display: inline;" : "display: none;" %>">Rendi indisponibile</button>
            <button id="make-available-button" onclick="rendiDisponibileProdotto('<%= ((ProdottoBean) prod).getIDProdotto() %>')" style="<%= !disponibilita ? "display: inline;" : "display: none;" %>">Rendi disponibile</button>
        </div>
        <% } else { %>
        <div class="select-container">
            <select name="taglie" id="taglia-select" required>
                <option value="" disabled selected>--Seleziona una taglia--</option>
                <%
                    for (TagliaProdottoBean taglia : taglieProd) {
                %>
                <option value="<%= taglia.getTaglia() %>"><%= taglia.getTaglia() %></option>
                <%
                    }
                %>
            </select>
        </div>
        <br><br>
        <button id="add-to-cart-button">Aggiungi al carrello</button>
        <% } %>
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
        document.getElementById('cart-count').innerText = carrello.length;
    }
</script>

<%@ include file="footer.jsp"%>
</body>
</html>
