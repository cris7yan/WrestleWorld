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
    <title>WrestleWorld | Aggiunta nuova categoria</title>
</head>
<body>

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
    <input type="text" id="nome_categoria" name="nome_categoria" required><br><br>

    <label for="immagine_categoria">Immagine Categoria:</label>
    <input type="file" id="immagine_categoria" name="immagine_categoria"><br><br>

    <button type="submit">Conferma</button>
</form>

<span>
    <p><a href="./profiloUtente.jsp"><- Torna alla pagina profilo</a></p>
</span>

</body>
</html>
