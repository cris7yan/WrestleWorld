<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
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

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/nuovoProdotto.css" rel="stylesheet" type="text/css">
    <link href="css/navbar.css" rel="stylesheet" type="text/css">
    <title>WrestleWorld | Aggiunta nuova categoria</title>
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

<h1>Crea Nuova Categoria</h1>
<form action="AdminControl?action=creaNuovaCategoria" method="post" enctype="multipart/form-data">
    <label for="tipo_categoria">Tipo Categoria:</label>
    <select id="tipo_categoria" name="tipo_categoria" required>
        <option value="Sesso">Sesso</option>
        <option value="Superstar">Superstar</option>
        <option value="Premium Live Event">Premium Live Event</option>
        <option value="Title Belts">Title Belts</option>
        <option value="Abbigliamento">Abbigliamento</option>
        <option value="Accessori">Accessori</option>
        <option value="Oggetti da Collezione">Oggetti da Collezione</option>
    </select><br><br>

    <label for="nome_categoria">Nome Categoria:</label>
    <input type="text" id="nome_categoria" name="nome_categoria" placeholder="Inserisci il nome della categoria..." required><br><br>

    <label for="immagine_categoria">Immagine Categoria:</label>
    <input type="file" id="immagine_categoria" name="immagine_categoria"><br><br>

    <label for="anteprimaImmagini">Immagini selezionate:</label>
    <div id="anteprimaImmagini"></div><br><br>

    <button type="submit" class="custom-btn btn">Conferma</button>
</form><br>

<span class="centered-link">
    <p><a href="./profiloUtente.jsp">Torna indietro</a></p>
</span><br><br><br><br>

<script>
    document.getElementById('immagine_categoria').addEventListener('change', function(event) {
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
