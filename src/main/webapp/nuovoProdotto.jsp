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
    <%
        if (categoriePerTipo != null) {
            for (Map.Entry<String, List<CategoriaBean>> entry : categoriePerTipo.entrySet()) {
                String tipo = entry.getKey();
                List<CategoriaBean> categorie = entry.getValue();
    %>
    <div>
        <label><%= tipo %>:</label><br>
        <%
            if ("Superstar".equals(tipo) || "Oggetti da collezione".equals(tipo)) {
                for (CategoriaBean cat : categorie) {
        %>
        <input type="checkbox" id="<%= cat.getNome() %>" name="categorie" value="<%= cat.getNome() %>">
        <label for="<%= cat.getNome() %>"><%= cat.getNome() %></label><br>
        <%
            }
        } else {
            for (CategoriaBean cat : categorie) {
        %>
        <input type="radio" id="<%= cat.getNome() %>" name="categorie" value="<%= cat.getNome() %>">
        <label for="<%= cat.getNome() %>"><%= cat.getNome() %></label><br>
        <%
                }
            }
        %>
    </div><br>
    <%
        }
    } else {
    %>
    <p>Nessuna categoria disponibile.</p>
    <%
        }
    %>

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
