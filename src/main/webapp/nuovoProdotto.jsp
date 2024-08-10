<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.wrestleworld.model.CategoriaBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="page500.jsp" %>

<%
    session = request.getSession();
    String tipoUtente = (String) session.getAttribute("tipo");
    if (tipoUtente == null || tipoUtente.equals("Utente")) {
        response.sendRedirect("page403.jsp");
        return;
    }
%>

<%
    Map<String, List<CategoriaBean>> categoriePerTipo = (Map<String, List<CategoriaBean>>) request.getAttribute("categoriePerTipo");
    if(categoriePerTipo == null) {
        response.sendRedirect("./CategoriaControl?action=visualizzaCategoriePerTipo");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/nuovoProdotto.css" rel="stylesheet" type="text/css">
    <link href="css/navbar.css" rel="stylesheet" type="text/css">
    <title>WrestleWorld | Aggiunta nuovo prodotto</title>
</head>
<body>
<header id="header" class="fixed-header">
    <a href="./index.jsp"><img src="img/logo/WrestleWorldTitleremove.png" class="logo" alt="Error logo"></a>
    <div>
        <ul id="navbar">
            <li><a class="active" href="./catalogo.jsp">Catalogo</a></li>
            <li id="userLogin"><a class="active" href="./login.jsp">My Account</a></li>
            <li><a href="UtenteControl?action=logout"><img src="img/sitoweb/logout.png" alt="Icon Error"></a></li>
        </ul>
    </div>
</header>

<h1>Crea Nuovo Prodotto</h1>
<form action="AdminControl?action=creaNuovoProdotto" method="post" enctype="multipart/form-data">
    <label for="nome">Nome Prodotto:</label>
    <input type="text" id="nome" name="nome" placeholder="Inserisci il nome del prodotto..." required><br><br>

    <label for="descrizione">Descrizione:</label>
    <textarea id="descrizione" name="descrizione" placeholder="Inserisci la descrizione del prodotto..." required></textarea><br><br>

    <label for="materiale">Materiale:</label>
    <input type="text" id="materiale" name="materiale" placeholder="Inserisci il materiale del prodotto..." required><br><br>

    <label for="marca">Marca:</label>
    <input type="text" id="marca" name="marca" placeholder="Inserisci la marca del prodotto..." required><br><br>

    <label for="modello">Modello:</label>
    <input type="text" id="modello" name="modello" placeholder="Inserisci il modello del prodotto..." required><br><br>

    <label for="prezzo">Prezzo:</label>
    <input type="number" step="0.01" id="prezzo" placeholder="Inserisci il prezzo..." name="prezzo" required><br><br>

    <label for="prezzo_offerta">Prezzo Offerta:</label>
    <input type="number" step="0.01" id="prezzo_offerta" name="prezzo_offerta" placeholder="Inserisci il prezzo d'offerta..."><br><br>

    <label for="disponibilita">Disponibilità prodotto:</label>
    <select id="disponibilita" name="disponibilita" required>
        <option value="true">Prodotto disponibile</option>
        <option value="false">Prodotto non disponibile</option>
    </select><br><br>

    <label for="immagini">Immagini del Prodotto:</label>
    <input type="file" id="immagini" name="immagini" multiple required><br><br>

    <label for="anteprimaImmagini">Immagini selezionate:</label>
    <div id="anteprimaImmagini"></div><br><br>

    <label for="taglie">Taglie e Quantità:</label>
    <div id="taglieContainer">
        <input type="text" name="taglie" placeholder="Inserisci la taglia" required>
        <input type="number" name="quantita" placeholder="Inserisci la quantità" required><br>
    </div>
    <button type="button" class="custom-btn btn" onclick="addTaglia()">Aggiungi Taglia</button><br><br>

    <h3>Seleziona le categorie:</h3>
    <!-- Sesso -->
    <div class="section">
        <h4>Sesso:</h4>
        <div class="options">
            <% if (categoriePerTipo.get("Sesso") != null) {
                for (CategoriaBean cat : categoriePerTipo.get("Sesso")) { %>
            <label>
                <input type="radio" name="sesso" value="<%= cat.getNome() %>">
                <%= cat.getNome() %>
            </label>
            <% } } %>
        </div>
    </div>

    <!-- Superstar -->
    <div class="section">
        <h4>Superstar:</h4>
        <div class="options">
            <% if (categoriePerTipo.get("Superstar") != null) {
                for (CategoriaBean cat : categoriePerTipo.get("Superstar")) { %>
            <label>
                <input type="checkbox" name="superstar" value="<%= cat.getNome() %>">
                <%= cat.getNome() %>
            </label>
            <% } } %>
        </div>
    </div>

    <!-- Premium Live Event -->
    <div class="section">
        <h4>Premium Live Event:</h4>
        <div class="options">
            <% if (categoriePerTipo.get("Premium Live Event") != null) {
                for (CategoriaBean cat : categoriePerTipo.get("Premium Live Event")) { %>
            <label>
                <input type="radio" name="ple" value="<%= cat.getNome() %>">
                <%= cat.getNome() %>
            </label>
            <% } } %>
        </div>
    </div>

    <!-- Title Belts -->
    <div class="section">
        <h4>Title Belts:</h4>
        <div class="options">
            <% if (categoriePerTipo.get("Title Belts") != null) {
                for (CategoriaBean cat : categoriePerTipo.get("Title Belts")) { %>
            <label>
                <input type="radio" name="title_belts" value="<%= cat.getNome() %>">
                <%= cat.getNome() %>
            </label>
            <% } } %>
        </div>
    </div>

    <!-- Abbigliamento -->
    <div class="section">
        <h4>Abbigliamento:</h4>
        <div class="options">
            <% if (categoriePerTipo.get("Abbigliamento") != null) {
                for (CategoriaBean cat : categoriePerTipo.get("Abbigliamento")) { %>
            <label>
                <input type="radio" name="abbigliamento" value="<%= cat.getNome() %>">
                <%= cat.getNome() %>
            </label>
            <% } } %>
        </div>
    </div>

    <!-- Accessori -->
    <div class="section">
        <h4>Accessori:</h4>
        <div class="options">
            <% if (categoriePerTipo.get("Accessori") != null) {
                for (CategoriaBean cat : categoriePerTipo.get("Accessori")) { %>
            <label>
                <input type="radio" name="accessori" value="<%= cat.getNome() %>">
                <%= cat.getNome() %>
            </label>
            <% } } %>
        </div>
    </div>

    <!-- Oggetti da Collezione -->
    <div class="section">
        <h4>Oggetti da Collezione:</h4>
        <div class="options">
            <% if (categoriePerTipo.get("Oggetti da collezione") != null) {
                for (CategoriaBean cat : categoriePerTipo.get("Oggetti da collezione")) { %>
            <label>
                <input type="checkbox" name="oggetti_da_collezione" value="<%= cat.getNome() %>">
                <%= cat.getNome() %>
            </label>
            <% } } %>
        </div>
    </div>

    <button type="submit" class="custom-btn btn">Conferma aggiunta prodotto</button>
    <button type="reset" id="reset-form" class="custom-btn btn">Resetta i valori selezionati</button>
</form><br>

<span class="centered-link">
    <p><a href="./profiloUtente.jsp">Torna indietro</a></p>
</span><br><br><br><br>

<script>
    document.getElementById('reset-form').addEventListener('click', function() {
        var container = document.getElementById('taglieContainer');
        // Rimuove tutte le taglie aggiunte
        while (container.firstChild) {
            container.removeChild(container.firstChild);
        }
        // Aggiungi una taglia di default
        var defaultTaglia = document.createElement('div');
        defaultTaglia.innerHTML = '<input type="text" name="taglie" placeholder="Taglia" required> <input type="number" name="quantita" placeholder="Quantità" required><br>';
        container.appendChild(defaultTaglia);
    });

    function addTaglia() {
        var container = document.getElementById('taglieContainer');
        var newTaglia = document.createElement('div');
        newTaglia.innerHTML = '<input type="text" name="taglie" placeholder="Taglia" required> <input type="number" name="quantita" placeholder="Quantità" required><br>';
        container.appendChild(newTaglia);
    }

    document.getElementById('immagini').addEventListener('change', function(event) {
        var anteprimaImmagini = document.getElementById('anteprimaImmagini');
        anteprimaImmagini.innerHTML = ''; // Svuota il contenitore delle anteprime

        // Itera su tutti i file selezionati
        for (var i = 0; i < event.target.files.length; i++) {
            var file = event.target.files[i];

            // Solo immagini
            if (file.type.startsWith('image/')) {
                var reader = new FileReader();

                reader.onload = function(e) {
                    var img = document.createElement('img');
                    img.src = e.target.result;
                    img.style.maxWidth = '150px'; // Limita la larghezza dell'immagine
                    img.style.marginRight = '10px'; // Spazio tra le immagini
                    img.style.marginBottom = '10px'; // Spazio sotto le immagini
                    anteprimaImmagini.appendChild(img);
                }

                reader.readAsDataURL(file); // Legge il file come URL di dati
            }
        }
    });
</script>

</body>
</html>
