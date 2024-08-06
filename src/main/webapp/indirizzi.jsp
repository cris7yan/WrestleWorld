<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.IndirizzoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page errorPage="pageError.jsp" %>

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
<style>
    /* Aggiunta per evitare sovrapposizione con la navbar */
    body {
        margin-top: 70px; /* Altezza della navbar + margine */
        padding-top: 10px; /* Spaziatura sopra il contenuto del body */
    }
</style>
<body>
<%@ include file="navbar.jsp"%>

<div class="form-container">

    <div class="indirizzi-container">
        <h1>Indirizzi personali</h1>

        <%
            if(indirizzi != null && !indirizzi.isEmpty()) {
                Iterator<?> indIt = indirizzi.iterator();
                while (indIt.hasNext()) {
                    IndirizzoBean indirizzo = (IndirizzoBean) indIt.next();
        %>

        <%
            if (indirizzo != null) {
        %>

        <div class="indirizzo">
            Via: <%= indirizzo.getViaIndirizzo() %> <br>
            Città: <%= indirizzo.getCittaIndirizzo() %> <br>
            Provincia: <%= indirizzo.getProvinciaIndirizzo() %> <br>
            CAP: <%= indirizzo.getCAPIndirizzo() %> <br>
            Nome: <%= indirizzo.getNomeCompletoIndirizzo() %> <br><br>
            <a href="UtenteControl?action=rimuoviIndirizzo&ID_Indirizzo=<%=indirizzo.getIdIndirizzo()%>" class="removeAddress-link">Rimuovi Indirizzo</a>
        </div>

        <%
                    }

                }

            }
        %>
    </div>

    <form action="UtenteControl?action=aggiungiIndirizzo" method="post" class="form">
        <div class="form-title">
            <span>Nuovo indirizzo</span>
        </div>

        <div class="input-container">
            <input class="input-nomeCompleto" name="nomeCompleto" type="text" placeholder="Nome e Cognome">
            <span> </span>
        </div>

        <div class="input-container">
            <input class="input-via" name="via" type="text" placeholder="Via">
            <span> </span>
        </div>

        <div class="input-container">
            <input class="input-citta" name="citta" type="text" placeholder="Città">
            <span> </span>
        </div>

        <div class="input-container">
            <input class="input-provincia" name="provincia" type="text" placeholder="Provincia">
            <span> </span>
        </div>

        <div class="input-container">
            <input class="input-cap" name="cap" type="text" placeholder="CAP">
            <span> </span>
        </div>

        <button type="submit" class="submitButton">
            <span class="sign text">Conferma</span>
        </button>

    </form>

</div>

<%@ include file="footer.jsp"%>
</body>
</html>
