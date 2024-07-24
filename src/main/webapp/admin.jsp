<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.UtenteBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%
    List<UtenteBean> utenti = (List<UtenteBean>) request.getAttribute("utenti");
    if (utenti == null) {
        response.sendRedirect("AdminControl?action=visualizzaUtenti");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <title>WrestleWorld | Admin</title>
    <style>
        .user-box {
            border: 1px solid #ccc;
            padding: 15px;
            margin: 15px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .user-box h2 {
            margin-top: 0;
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>

<div class="container">
    <h1>Lista Utenti</h1>
    <div class="user-list">
        <%
            for (UtenteBean utente : utenti) {
        %>
        <div class="user-box">
            <h2><%= utente.getNome() %> <%= utente.getCognome() %></h2>
            <p><strong>Email:</strong> <%= utente.getEmail() %></p>
            <p><strong>Data di Nascita:</strong> <%= utente.getDataNascita() %></p>
            <p><strong>Tipo Utente:</strong> <%= utente.getTipoUtente() %></p>
        </div>
        <%
            }
        %>
    </div>
</div>

</body>
</html>
