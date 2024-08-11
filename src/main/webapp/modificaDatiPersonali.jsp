<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
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

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/modificaDati.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Indirizzi</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div class="form-wrapper">
    <form action="UtenteControl?action=modificaDati" method="post" class="form" id="modificaDatiform">
        <div class="form-title">
            <span><h1>Modifica i tuoi dati Superstar!</h1></span>
        </div>
        <div class="form-title2">
            <span>WrestleWorld Superstar</span>
        </div>

        <div class="input-container">
            <label for="nome">Nome:</label>
            <input name="nome" type="text" placeholder="Nome" class="input-nome" value="<%=nome%>">
            <span> </span>
        </div>

        <div class="input-container">
            <label for="cognome">Cognome:</label>
            <input name="cognome" type="text" placeholder="Cognome" class="input-cognome" value="<%=cognome%>">
            <span> </span>
        </div>

        <div class="input-container">
            <label for="dataNascita">Data di nascita:</label>
            <input name="dataNascita" type="date" placeholder="Data di Nascita" class="input-dataNascita" value="<%=dataNascita%>">
            <span> </span>
        </div>

        <button type="submit" class="custom-btn btn"><span>Conferma modifiche</span></button>
    </form>
</div>

<!-- script per la verifica durante la fase di registrazione -->
<script>
    // Attende che il contenuto del DOM sia completamente caricato prima di eseguire il codice
    document.addEventListener("DOMContentLoaded", function() {
        // Seleziona il form con il nome "registrazione"
        var form = document.querySelector("form[action='UtenteControl?action=modificaDati']"); // Seleziona il form della registrazione

        // Aggiunge un evento di ascolto per l'evento "submit" del form
        form.addEventListener("submit", function(event) {
            event.preventDefault(); // Previene il submit del form e l'invio della richiesta HTTP

            // Recupera i valori degli input "nome", "cognome" e "dataNascita" all'interno del form
            var nome = form.nome.value;
            var cognome = form.cognome.value;
            var data = form.dataNascita.value;

            var nameRegex = /^[A-Za-z\s]+$/;    // Espressione regolare per nome e cognome (nessun carattere speciale)
            var dataDiOggi = new Date(); // Data di oggi
            var dataLimite = new Date(); // Data limite per l'età
            dataLimite.setFullYear(dataDiOggi.getFullYear() - 16); // Calcola la data limite per i 16 anni

            // Verifica del nome e cognome tramite espressione regolare
            if (!nameRegex.test(nome)) {
                alert("Il campo Nome non può contenere caratteri speciali o numeri");
                form.nome.focus(); // Focalizza il campo nome
                return false; // Interrompe la funzione
            }

            if (!nameRegex.test(cognome)) {
                alert("Il campo Cognome non può contenere caratteri speciali o numeri");
                form.cognome.focus(); // Focalizza il campo cognome
                return false; // Interrompe la funzione
            }

            // Verifica della data di nascita
            if (data !== "") {
                var dataNascita = new Date(data);
                // Controlla se la data di nascita è valida
                if (dataNascita > dataDiOggi) {
                    alert("Data inserita non valida. Inserirne una valida.");
                    form.dataNascita.focus(); // Focalizza il campo data di nascita
                    return false; // Interrompe la funzione
                } else if (dataNascita > dataLimite) {
                    alert("Per registrarti devi avere almeno 16 anni");
                    form.dataNascita.focus(); // Focalizza il campo data di nascita
                    return false; // Interrompe la funzione
                }
            }

            // Se tutte le verifiche passano, invia il form
            form.submit();
        });
    });
</script>

<%@ include file="footer.jsp"%>
</body>
</html>
