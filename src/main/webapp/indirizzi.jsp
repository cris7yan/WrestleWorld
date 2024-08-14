<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.IndirizzoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
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
    List<IndirizzoBean> indirizzi = (List<IndirizzoBean>) request.getAttribute("indirizzi");
    if(indirizzi == null) {
        response.sendRedirect("./UtenteControl?action=visualizzaIndirizzi");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/indirizzi.css" type="text/css" rel="stylesheet">
    <link href="css/modificaDati.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Indirizzi</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div class="form-wrapper" id="add-indirizzi">
    <form action="UtenteControl?action=aggiungiIndirizzo" method="post" class="form">
        <div class="form-title">
            <span><h2>Inserisci i dati del tuo indirizzo</h2></span>
        </div>

        <div class="input-container">
            <label for="nomeCompleto">Destinatario:</label>
            <input class="input-nomeCompleto" name="nomeCompleto" type="text" placeholder="Nome e Cognome">
            <span> </span>
        </div>

        <div class="input-container">
            <label for="via">Via:</label>
            <input class="input-via" name="via" type="text" placeholder="Via">
            <span> </span>
        </div>

        <div class="input-container">
            <label for="citta">Città:</label>
            <input class="input-citta" name="citta" type="text" placeholder="Città">
            <span> </span>
        </div>

        <div class="input-container">
            <label for="provincia">Provincia:</label>
            <input class="input-provincia" name="provincia" type="text" placeholder="Provincia">
            <span> </span>
        </div>

        <div class="input-container">
            <label for="cap">CAP:</label>
            <input class="input-cap" name="cap" type="text" placeholder="CAP">
            <span> </span>
        </div>

        <button type="submit" class="custom-btn btn"><span>Conferma</span></button>
    </form>
</div>

<div class="indirizzi-container" id="view-indirizzi">
    <div class="form-title">
        <span><h1>Indirizzi personali selezionati</h1></span>
    </div>
    <div class="form-title2">
        <span>Qui troverai tutti gli indirizzi che hai precedentemente salvato.<br>Se un indirizzo non è più utilizzato puoi anche eliminarlo.</span>
    </div>

    <%
        if(indirizzi != null && !indirizzi.isEmpty()) {
            for(IndirizzoBean indirizzo : indirizzi) {
                if (indirizzo != null) {
    %>
    <div class="indirizzo" id="indirizzo-<%= indirizzo.getIdIndirizzo() %>">
        <strong>Via:</strong> <%= indirizzo.getViaIndirizzo() %> <br>
        <strong>Città:</strong> <%= indirizzo.getCittaIndirizzo() %> <br>
        <strong>Provincia:</strong> <%= indirizzo.getProvinciaIndirizzo() %> <br>
        <strong>CAP:</strong> <%= indirizzo.getCAPIndirizzo() %> <br>
        <strong>Destinatario:</strong> <%= indirizzo.getNomeCompletoIndirizzo() %> <br><br>
        <button type="button" class="custom-btn btn" onclick="rimuoviIndirizzo(<%= indirizzo.getIdIndirizzo() %>)"><span>Rimuovi Indirizzo</span></button>
    </div>
    <% } } } %>
</div>

<script>
    function rimuoviIndirizzo(idIndirizzo) {
        if (confirm("Sei sicuro di voler rimuovere questo indirizzo?")) {
            $.ajax({
                url: "UtenteControl?action=rimuoviIndirizzo",
                type: "POST",
                data: { ID_Indirizzo: idIndirizzo },
                success: function(response) {
                    // Rimuove l'elemento dal DOM
                    document.getElementById("indirizzo-" + idIndirizzo).remove();
                    alert("Indirizzo rimosso con successo.");
                },
                error: function() {
                    alert("Si è verificato un errore durante la rimozione dell'indirizzo.");
                }
            });
        }
    }
</script>

<%@ include file="footer.jsp"%>
</body>
</html>
