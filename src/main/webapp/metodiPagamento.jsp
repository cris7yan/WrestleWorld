<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.MetodoPagamentoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
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
    List<MetodoPagamentoBean> metodi = (List<MetodoPagamentoBean>) request.getAttribute("metodiPagamento");
    if(metodi == null) {
        response.sendRedirect("./UtenteControl?action=visualizzaMetodiPagamento");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/metodiPagamento.css" type="text/css" rel="stylesheet">
    <link href="css/modificaDati.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Metodi di Pagamento</title>
    <script>
        function formatIBAN(input) {
            input.value = input.value.replace(/\s+/g, '').replace(/(\d{4})/g, '$1 ').trim();
        }
    </script>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div class="form-wrapper" id="add-payment-method">
    <form action="UtenteControl?action=aggiungiMetodoPagamento" method="post" class="form">
        <div class="form-title">
            <span><h2>Inserisci i dati del tuo metodo di pagamento</h2></span>
        </div>

        <div class="input-container">
            <label for="intestatario">Intestatario:</label>
            <input class="input-intestatario" name="intestatario" type="text" placeholder="Nome e Cognome">
            <span> </span>
        </div>

        <div class="input-container">
            <label for="numeroCarta">Numero carta:</label>
            <input class="input-numeroCarta" name="numeroCarta" type="text" placeholder="Numero carta" maxlength="19" oninput="formatIBAN(this)">
            <span> </span>
        </div>

        <div class="input-container">
            <label for="dataScadenza">Data di scadenza:</label>
            <input class="input-dataScadenza" name="dataScadenza" type="date" placeholder="Data Scadenza">
            <span> </span>
        </div>

        <button type="submit" class="custom-btn btn"><span>Conferma</span></button>
    </form>
</div>

<div class="metodi-container" id="view-payment-method">
    <div class="form-title">
        <span><h1>Metodi di pagamento selezionati</h1></span>
    </div>
    <div class="form-title2">
        <span>Qui troverai tutti i metodi di pagamento che hai precedentemente salvato.<br>Se un metodo di pagamento non è più utilizzato puoi anche eliminarlo.</span>
    </div>

    <%
        if(metodi != null && !metodi.isEmpty()) {
            for(MetodoPagamentoBean metodo : metodi) {
                if (metodo != null) {
                    int meseScadenza = metodo.getDataScadenza().getMonth() + 1;
                    int annoScadenza = metodo.getDataScadenza().getYear() + 1900;
    %>
    <div class="metodoPagamento" id="metodo-<%= metodo.getIdMetodoPagamento() %>">
        <strong>Numero carta:</strong> <%= metodo.getNumeroCarta() %> <br>
        <strong>Intestatario:</strong> <%= metodo.getIntestatario() %> <br>
        <strong>Data Scadenza:</strong> <%= annoScadenza %>/<%= meseScadenza %> <br><br>
        <button type="button" class="custom-btn btn" onclick="rimuoviMetodoPagamento(<%= metodo.getIdMetodoPagamento() %>)"><span>Rimuovi metodo</span></button>
    </div>
    <% } } } %>

</div>

<script>
    function rimuoviMetodoPagamento(idMetodo) {
        if (confirm("Sei sicuro di voler rimuovere questo metodo di pagamento?")) {
            $.ajax({
                url: "UtenteControl?action=rimuoviMetodoPagamento",
                type: "POST",
                data: { ID_Pagamento: idMetodo },
                success: function(response) {
                    // Rimuove l'elemento dal DOM
                    document.getElementById("metodo-" + idMetodo).remove();
                    alert("Metodo di pagamento rimosso con successo.");
                },
                error: function() {
                    alert("Si è verificato un errore durante la rimozione del metodo di pagamento.");
                }
            });
        }
    }
</script>

<%@ include file="footer.jsp"%>
</body>
</html>
