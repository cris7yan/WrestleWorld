<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.wrestleworld.model.CategoriaBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
    <title>WrestleWorld | Aggiunta nuovo prodotto</title>
</head>
<body>

<h1>Crea Nuovo Prodotto</h1>
<form action="AdminControl?action=creaNuovoProdotto" method="post" enctype="multipart/form-data">
    <label for="nome">Nome Prodotto:</label>
    <input type="text" id="nome" name="nome" required><br><br>

    <label for="descrizione">Descrizione:</label>
    <textarea id="descrizione" name="descrizione" required></textarea><br><br>

    <label for="materiale">Materiale:</label>
    <input type="text" id="materiale" name="materiale" required><br><br>

    <label for="marca">Marca:</label>
    <input type="text" id="marca" name="marca" required><br><br>

    <label for="modello">Modello:</label>
    <input type="text" id="modello" name="modello" required><br><br>

    <label for="prezzo">Prezzo:</label>
    <input type="number" step="0.01" id="prezzo" name="prezzo" required><br><br>

    <label for="prezzo_offerta">Prezzo Offerta:</label>
    <input type="number" step="0.01" id="prezzo_offerta" name="prezzo_offerta"><br><br>

    <label for="disponibilita">Disponibilità:</label>
    <select id="disponibilita" name="disponibilita" required>
        <option value="true">Disponibile</option>
        <option value="false">Non Disponibile</option>
    </select><br><br>

    <label for="immagini">Immagini del Prodotto:</label>
    <input type="file" id="immagini" name="immagini" multiple required><br><br>

    <label for="taglie">Taglie e Quantità:</label><br>
    <div id="taglieContainer">
        <input type="text" name="taglie" placeholder="Taglia" required>
        <input type="number" name="quantita" placeholder="Quantità" required><br>
    </div>
    <button type="button" onclick="addTaglia()">Aggiungi Taglia</button><br><br>

    <label for="categorie">Categorie:</label><br>

    <!-- Sesso -->
    <div>
        <label>Sesso:</label><br>
        <% if (categoriePerTipo.get("Sesso") != null) {
            for (CategoriaBean cat : categoriePerTipo.get("Sesso")) { %>
        <input type="radio" id="sesso_<%= cat.getNome() %>" name="sesso" value="<%= cat.getNome() %>">
        <label for="sesso_<%= cat.getNome() %>"><%= cat.getNome() %></label><br>
        <% } } %>
    </div><br>

    <!-- Superstar -->
    <div>
        <label>Superstar:</label><br>
        <% if (categoriePerTipo.get("Superstar") != null) {
            for (CategoriaBean cat : categoriePerTipo.get("Superstar")) { %>
        <input type="checkbox" id="superstar_<%= cat.getNome() %>" name="superstar" value="<%= cat.getNome() %>">
        <label for="superstar_<%= cat.getNome() %>"><%= cat.getNome() %></label><br>
        <% } } %>
    </div><br>

    <!-- Premium Live Event -->
    <div>
        <label>Premium Live Event:</label><br>
        <% if (categoriePerTipo.get("Premium Live Event") != null) {
            for (CategoriaBean cat : categoriePerTipo.get("Premium Live Event")) { %>
        <input type="radio" id="ple_<%= cat.getNome() %>" name="ple" value="<%= cat.getNome() %>">
        <label for="ple_<%= cat.getNome() %>"><%= cat.getNome() %></label><br>
        <% } } %>
    </div><br>

    <!-- Title Belts -->
    <div>
        <label>Title Belts:</label><br>
        <% if (categoriePerTipo.get("Title Belts") != null) {
            for (CategoriaBean cat : categoriePerTipo.get("Title Belts")) { %>
        <input type="radio" id="title_belts_<%= cat.getNome() %>" name="title_belts" value="<%= cat.getNome() %>">
        <label for="title_belts_<%= cat.getNome() %>"><%= cat.getNome() %></label><br>
        <% } } %>
    </div><br>

    <!-- Abbigliamento -->
    <div>
        <label>Abbigliamento:</label><br>
        <% if (categoriePerTipo.get("Abbigliamento") != null) {
            for (CategoriaBean cat : categoriePerTipo.get("Abbigliamento")) { %>
        <input type="radio" id="abbigliamento_<%= cat.getNome() %>" name="abbigliamento" value="<%= cat.getNome() %>">
        <label for="abbigliamento_<%= cat.getNome() %>"><%= cat.getNome() %></label><br>
        <% } } %>
    </div><br>

    <!-- Accessori -->
    <div>
        <label>Accessori:</label><br>
        <% if (categoriePerTipo.get("Accessori") != null) {
            for (CategoriaBean cat : categoriePerTipo.get("Accessori")) { %>
        <input type="radio" id="accessori_<%= cat.getNome() %>" name="accessori" value="<%= cat.getNome() %>">
        <label for="accessori_<%= cat.getNome() %>"><%= cat.getNome() %></label><br>
        <% } } %>
    </div><br>

    <!-- Oggetti da Collezione -->
    <div>
        <label>Oggetti da Collezione:</label><br>
        <% if (categoriePerTipo.get("Oggetti da collezione") != null) {
            for (CategoriaBean cat : categoriePerTipo.get("Oggetti da Collezione")) { %>
        <input type="checkbox" id="oggetti_da_collezione_<%= cat.getNome() %>" name="oggetti_da_collezione" value="<%= cat.getNome() %>">
        <label for="oggetti_da_collezione_<%= cat.getNome() %>"><%= cat.getNome() %></label><br>
        <% } } %>
    </div><br>

    <button type="submit">Conferma</button>
</form>

<script>
    function addTaglia() {
        var container = document.getElementById('taglieContainer');
        var newTaglia = document.createElement('div');
        newTaglia.innerHTML = '<input type="text" name="taglie" placeholder="Taglia" required> <input type="number" name="quantita" placeholder="Quantità" required><br>';
        container.appendChild(newTaglia);
    }
</script>

</body>
</html>
