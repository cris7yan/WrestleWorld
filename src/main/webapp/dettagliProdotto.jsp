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
<%@ page errorPage="page500.jsp" %>

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
    <!-- Carosello di immagini -->
    <section id="product-carousel" class="carousel">
        <div class="slider-wrapper">
            <div class="slider">
                <% if (prod instanceof ProdottoBean) {
                    for (String img : imgProd) { %>
                <div class="slide">
                    <img src="img/prodotti/<%= img %>" alt="IMG Error">
                </div>
                <% } } %>
            </div>
            <div class="slider-nav">
                <button class="slider-nav-btn" onclick="prevSlide()">&#10094;</button>
                <button class="slider-nav-btn" onclick="nextSlide()">&#10095;</button>
            </div>
        </div>
    </section>
    <script src="js/gestioneCaroselloImmagini.js"></script>

    <!-- Dettagli prodotto -->
    <div class="product-details">
        <h2 class="product-name"><%= ((ProdottoBean) prod).getNomeProdotto() %></h2>
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
        <% } else {
            prezzoDaUsare = prezzoOriginale;
        %>
        <p class="product-price">
            <span class="euro"><%= euroOriginale %></span><span class="decimal">,<%= String.format("%02d", centesimiOriginale) %></span>&euro;
        </p>
        <% } %>

        <input type="hidden" id="prezzo-da-usare" value="<%= prezzoDaUsare %>">

        <% if ("Admin".equals(tipoUtente)) { %>
        <div class="admin-price-update-container">
            <button class="custom-btn btn" onclick="mostraModificaPrezzo()">Modifica Prezzo</button>
            <div id="modifica-prezzo" class="input-container" style="display:none;">
                <input type="number" id="nuovo-prezzo" placeholder="Nuovo Prezzo">
                <button class="custom-btn btn" onclick="modificaPrezzo('<%= ((ProdottoBean) prod).getIDProdotto() %>')">Aggiorna Prezzo</button>
            </div>
        </div>

        <div class="admin-price-update-container">
            <button class="custom-btn btn" onclick="mostraModificaPrezzoOfferta()">Modifica Prezzo Offerta</button>
            <div id="modifica-prezzo-offerta" class="input-container" style="display:none;">
                <input type="number" id="nuovo-prezzo-offerta" placeholder="Nuovo Prezzo Offerta">
                <button class="custom-btn btn" onclick="modificaPrezzoOfferta('<%= ((ProdottoBean) prod).getIDProdotto() %>')">Aggiorna Prezzo Offerta</button>
            </div>
        </div>
        <% } %>

        <% boolean disponibilita = ((ProdottoBean) prod).getDisponibilitaProdotto();
            if (!disponibilita) { %>
        <p class="out-of-stock">Questo prodotto non è attualmente disponibile.</p>
        <% } %>
        <h3>Taglie disponibili:</h3>
        <% if ("Admin".equals(tipoUtente)) { %>
        <div class="admin-quantita-container">
            <table>
                <tr>
                    <th>Taglia</th>
                    <th>Quantità</th>
                    <th>Aggiungi quantità</th>
                </tr>
                <% for (TagliaProdottoBean taglia : taglieProd) { %>
                <tr>
                    <td><%= taglia.getTaglia() %></td>
                    <td><%= taglia.getQuantita() %></td>
                    <td>
                        <button class="custom-btn-2 btn" onclick="aggiungiQuantita('<%= ((ProdottoBean) prod).getIDProdotto() %>', '<%= taglia.getTaglia() %>')">Aggiungi quantità</button>
                        <input type="number" id="quantita-<%= taglia.getTaglia() %>" class="input-quantity" placeholder="Quantità">
                    </td>
                </tr>
                <% } %>
            </table><br>

            <div class="admin-add-size-container">
                <button class="custom-btn-3 btn" onclick="mostraAggiungiTaglia()">Aggiungi una nuova taglia</button>
            </div>
            <div id="aggiungi-taglia" style="display:none;">
                <input type="text" id="nuova-taglia" class="input-nuova-taglia" placeholder="Nuova Taglia">
                <input type="number" id="quantita-taglia" class="input-nuova-quantita" placeholder="Quantità">
                <button class="custom-btn btn" onclick="aggiungiTaglia('<%= ((ProdottoBean) prod).getIDProdotto() %>')">Aggiungi Taglia</button>
            </div>
        </div>
        <% } else { %>
        <div class="select-container">
            <select name="taglie" id="taglia-select" required>
                <option value="" disabled selected>-- Seleziona una taglia --</option>
                <%
                    for (TagliaProdottoBean taglia : taglieProd) {
                %>
                <option value="<%= taglia.getTaglia() %>"><%= taglia.getTaglia() %> - Quantità: <%= taglia.getQuantita()%></option>
                <%
                    }
                %>
            </select>
        </div>
        <br><br>
        <button class="custom-btn btn" id="add-to-cart-button">Aggiungi al carrello</button>
        <% } %>

        <!-- Descrizione del prodotto -->
        <div class="product-description-container">
            <h3>Descrizione:</h3>
            <p class="product-description"><i><%= ((ProdottoBean) prod).getDescrizioneProdotto() %></i></p>
        </div>

        <!-- Dettagli del prodotto -->
        <div class="product-details-container">
            <h3 onclick="toggleDetails()">Dettagli: &#9662;</h3>
            <div id="product-details-content" style="display: none;">
                <p class="product-marca"><strong>Marca:</strong> <span class="product-detail-value"><%= ((ProdottoBean) prod).getMarcaProdotto() %></span></p>
                <p class="product-modello"><strong>Modello:</strong> <span class="product-detail-value"><%= ((ProdottoBean) prod).getModelloProdotto() %></span></p>
                <p class="product-materiale"><strong>Materiale:</strong> <span class="product-detail-value"><%= ((ProdottoBean) prod).getMaterialeProdotto() %></span></p>
            </div>
        </div>
        <br><br><br>

        <% if ("Admin".equals(tipoUtente)) { %>
        <div class="admin-actions">
            <button class="custom-btn btn" onclick="mostraAggiungiCategoria()">Aggiungi Categoria</button>
            <div id="aggiungi-categoria" style="display:none;">
                <h3>Aggiungi una nuova appartenenza</h3>
                <input type="text" id="nuova-categoria" class="input-nuova-categoria" placeholder="Nome Categoria">
                <button class="custom-btn btn" onclick="aggiungiAppartenenza('<%= ((ProdottoBean) prod).getIDProdotto() %>')">Aggiungi Appartenenza</button>
            </div><br><br>

            <button class="custom-btn btn" id="delete-product-button" onclick="eliminaProdotto('<%= ((ProdottoBean) prod).getIDProdotto() %>')">Elimina prodotto</button>
            <button class="custom-btn btn" id="make-unavailable-button" onclick="rendiIndisponibileProdotto('<%= ((ProdottoBean) prod).getIDProdotto() %>')" style="<%= disponibilita ? "display: inline;" : "display: none;" %>">Rendi indisponibile</button>
            <button class="custom-btn btn" id="make-available-button" onclick="rendiDisponibileProdotto('<%= ((ProdottoBean) prod).getIDProdotto() %>')" style="<%= !disponibilita ? "display: inline;" : "display: none;" %>">Rendi disponibile</button>
        </div>
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

    function toggleDetails() {
        var detailsContent = document.getElementById('product-details-content');
        if (detailsContent.style.display === 'none') {
            detailsContent.style.display = 'block';
        } else {
            detailsContent.style.display = 'none';
        }
    }

    function mostraModificaPrezzo() {
        var divPrezzo = document.getElementById('modifica-prezzo');
        if (divPrezzo.style.display === 'none') {
            divPrezzo.style.display = 'block';
        } else {
            divPrezzo.style.display = 'none';
        }
    }

    function mostraModificaPrezzoOfferta() {
        var divPrezzoOfferta = document.getElementById('modifica-prezzo-offerta');
        if (divPrezzoOfferta.style.display === 'none') {
            divPrezzoOfferta.style.display = 'block';
        } else {
            divPrezzoOfferta.style.display = 'none';
        }
    }

    function mostraAggiungiTaglia() {
        var divTaglia = document.getElementById('aggiungi-taglia');
        if (divTaglia.style.display === 'none') {
            divTaglia.style.display = 'block';
        } else {
            divTaglia.style.display = 'none';
        }
    }

    function mostraAggiungiCategoria() {
        var divCategoria = document.getElementById('aggiungi-categoria');
        if (divCategoria.style.display === 'none') {
            divCategoria.style.display = 'block';
        } else {
            divCategoria.style.display = 'none';
        }
    }
</script>

<%@ include file="footer.jsp"%>
</body>
</html>
