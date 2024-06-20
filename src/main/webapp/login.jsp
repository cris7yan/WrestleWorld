<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%!
    String emailUtente = "";
    String nome = "";
    String cognome = "";
    Date dataNascita = null;
    String tipoUtente = "";
    String result = "";
%>

<%
    synchronized (session) {
        session = request.getSession();
        emailUtente = (String) session.getAttribute("email");
        nome = (String) session.getAttribute("nome");
        cognome = (String) session.getAttribute("cognome");
        dataNascita = (Date) session.getAttribute("dataNascita");
        tipoUtente = (String) session.getAttribute("tipo");
        result = (String) request.getAttribute("result");
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/login.css" rel="stylesheet" type="text/css">
    <title>WrestleWorld | Login</title>
</head>
<body>

    <%
        if(emailUtente != null) {
            response.sendRedirect("./profiloUtente.jsp");
        }
    %>

    <br><br><br><br><br><br>
    <h2>Accedi al tuo profilo</h2>
    <h6>oppure</h6>
    <h2>Registrati se non ne hai uno</h2>
    <div class="container" id="container">
        <div class="form-container sign-up-container">
            <form action="UtenteControl?action=registrazione" method="post">
                <h1>Crea il tuo account da Superstar</h1>
                <br>
                <span>Registrati anche tu nel roster di WrestleWorld e diventa anche tu una Superstar!</span>
                <input type="email" name="email" placeholder="E-mail" />
                <input type="password" name="password" placeholder="Password" />
                <input type="text" name="nome" placeholder="Nome" />
                <input type="text" name="cognome" placeholder="Cognome" />
                <input type="date" name="dataNascita" placeholder="Data di nascita" />
                <button>Registrati</button>
            </form>
        </div>
        <div class="form-container sign-in-container">
            <form action="UtenteControl?action=login" method="post">
                <h1>Log in</h1>
                <br>
                <span>Usa il tuo account</span>
                <input type="email" name="email" placeholder="E-mail" />
                <input type="password" name="password" placeholder="Password" />
                <button>Accedi</button>
            </form>
        </div>
        <div class="overlay-container">
            <div class="overlay">
                <div class="overlay-panel overlay-left">
                    <h1>Ben Tornato!</h1>
                    <p>Per rimanere in contatto con noi effettua il login<br>con i tuoi dati personali</p>
                    <button class="ghost" id="signIn">Log in</button>
                </div>
                <div class="overlay-panel overlay-right">
                    <h1>Ciao, Amico!</h1>
                    <p>Se non sei già registrato, inserisci i tuoi dati personali<br>ed entra a far parte del roster di WrestleWorld</p>
                    <button class="ghost" id="signUp">Registrati</button>
                </div>
            </div>
        </div>
    </div>
    <br><br><br><br>

    <span>
        <p><a href="index.jsp">Home</a></p>
    </span>

    <%
        if(result != null) {
    %>
    <script>
        alert("<%=result%>");
    </script>
    <%
        }
    %>
    <script src="js/login.js" type="text/javascript"></script>

</body>
</html>
