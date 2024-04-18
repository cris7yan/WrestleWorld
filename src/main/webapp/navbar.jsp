<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <title>WrestleWorld</title>
</head>
<body>

<header>
    <div>
        <a href="index.jsp"><img src="img/logo/WrestleWorldLogo.png" alt="logo" class="logo"></a>
    </div>

        <% if(session.getAttribute("UtenteLoggato") == null && session.getAttribute("AdminLoggato") == null) { %>

        <div id="icona">
            <a href="login.jsp">
                <img src="https://www.svgrepo.com/show/451412/user-key.svg" width="28" height="28" title="login_icon" alt="login icon"><br>
            </a>
        </div>
        <%
            }
        %>

</header>

</body>
</html>
