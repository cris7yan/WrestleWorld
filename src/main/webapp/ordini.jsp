<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.model.OrdineBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%
    List<OrdineBean> ordini = (List<OrdineBean>) request.getAttribute("ordini");
    if(ordini == null) {
      response.sendRedirect("./OrdineControl?action=visualizzaOrdini");
      return;
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/ordini.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Ordini</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div>
    <div class="ordini-container">
        <h1>I tuoi ordini</h1>

        <%
            if(ordini != null) {
                Iterator<?> ordIt = ordini.iterator();
                while (ordIt.hasNext()) {
                    OrdineBean ordine = (OrdineBean) ordIt.next();
        %>

        <%
            if(ordine != null) {
        %>

        <div class="ordine">
            ID Ordine: <%= ordine.getIdOrdine() %> <br>
            Data: <%= ordine.getDataOrdine() %> <br>
            Totale: <%= ordine.getPrezzoTotaleOrdine() %> <br>
        </div>

        <%
                    }

                }

            }
        %>

    </div>
</div>

<%@ include file="footer.jsp"%>
</body>
</html>
