<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.util.Carrello" %>
<%@ page contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" language="java" %>

<%!
    String emailUtente = "";
    Carrello carrello = null;
%>

<%
    synchronized (session) {
        session = request.getSession();
        if(session.getAttribute("email") != null) {
              emailUtente = (String) session.getAttribute("email");
        }
        carrello = (Carrello) session.getAttribute("carrello");
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="ISO-8859-1">
    <link href="css/navbar.css" rel="stylesheet" type="text/css">
    <title>WrestleWorld</title>
</head>
<body>
    <nav>

        <div class="navbar-content">
            <div class="logo">
                <a href="./index.jsp"> <img src="img/logo/WrestleWorldTitle.png" alt="Logo"></a>
            </div>

            <div class="profilo">
                <a href="login.jsp">
                    <svg viewBox="0 0 32 32" enable-background="new 0 0 32 32" id="Stock_cut" version="1.1" xml:space="preserve" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" fill="#000000"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <desc></desc> <g> <circle cx="16" cy="16" fill="none" r="15" stroke="#000000" stroke-linejoin="round" stroke-miterlimit="10" stroke-width="2"></circle> <path d="M26,27L26,27 c0-5.523-4.477-10-10-10h0c-5.523,0-10,4.477-10,10v0" fill="none" stroke="#000000" stroke-linejoin="round" stroke-miterlimit="10" stroke-width="2"></path> <circle cx="16" cy="11" fill="none" r="6" stroke="#000000" stroke-linejoin="round" stroke-miterlimit="10" stroke-width="2"></circle> </g> </g></svg>
                </a>
            </div>

            <%
                if(emailUtente != null) {
            %>
            <div class="logout">
                <a href="UtenteControl?action=logout">
                    <svg height="200px" width="200px" version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512" xml:space="preserve" fill="#000000"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path style="fill:#FF9F99;" d="M256,499.2c64.956,0,126.029-25.293,171.964-71.228c94.814-94.822,94.814-249.114,0-343.936 C382.029,38.101,320.956,12.8,256,12.8S129.971,38.101,84.036,84.036c-94.814,94.822-94.814,249.114,0,343.936 C129.971,473.899,191.044,499.2,256,499.2z"></path> <path style="fill:#573A32;" d="M256,0C114.62,0,0,114.62,0,256s114.62,256,256,256c141.389,0,256-114.62,256-256S397.389,0,256,0z M256,486.4C128.956,486.4,25.6,383.044,25.6,256c0-58.923,22.426-112.58,58.94-153.361L409.361,427.46 C368.58,463.974,314.923,486.4,256,486.4z M427.46,409.361L102.639,84.54C143.428,48.026,197.077,25.6,256,25.6 c127.044,0,230.4,103.356,230.4,230.4C486.4,314.923,463.983,368.58,427.46,409.361z"></path> </g></svg>
                </a>
            </div>
            <%
                }
            %>

            <div class="carrello">
                <a href="carrello.jsp">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M6.29977 5H21L19 12H7.37671M20 16H8L6 3H3M9 20C9 20.5523 8.55228 21 8 21C7.44772 21 7 20.5523 7 20C7 19.4477 7.44772 19 8 19C8.55228 19 9 19.4477 9 20ZM20 20C20 20.5523 19.5523 21 19 21C18.4477 21 18 20.5523 18 20C18 19.4477 18.4477 19 19 19C19.5523 19 20 19.4477 20 20Z" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> </g></svg>
                </a>
            </div>
        </div>

    </nav>
</body>
</html>
