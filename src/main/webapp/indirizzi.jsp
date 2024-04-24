<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.IndirizzoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" language="java" %>

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
    <meta charset="ISO-8859-1">
    <link href="css/indirizzi.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Indirizzi</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

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

</body>
</html>
