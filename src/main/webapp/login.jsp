<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <title>WrestleWorld</title>
</head>
<body>

    <form id="loginForm" class="loginForm" action="login" method="post">
        <input type="hidden" name="action" value="login">
            <h1>Accedi</h1>

        <div class="content">
            <div class="input-field">
                <img class="icon" src="https://www.svgrepo.com/show/508196/user-circle.svg" width="28" height="28" title="username-icon" alt="search icon">
                <input type="text" placeholder="Email" id="email" name="email">
            </div>

            <div class="input-field">
                <img class="icon" src="https://www.svgrepo.com/show/340797/password.svg" width="28" height="28" title="password-icon" alt="search icon">
                <input type="password" placeholder="Password" id="password" name="password">
            </div>
        </div>

        <div class="action">
            <div class="action1">
                <button type="submit" id="loginButton">Accedi</button>
            </div>
        </div>
    </form>

</body>
</html>
