<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page errorPage="page500.jsp" %>

<%
    session = request.getSession();
    String tipoUtente = (String) session.getAttribute("tipo");
    if (tipoUtente == null || tipoUtente.equals("Admin")) {
        response.sendRedirect("page403.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/modificaDati.css" type="text/css" rel="stylesheet">
    <title>WrestleWorld | Modifica Dati di Accesso</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div>
    <form action="UtenteControl?action=modificaDatiAccesso" method="post" class="form">

        <div class="form-title">
            <span>Modifica i tuoi dati di accesso</span>
        </div>

        <div class="input-container">
            <input name="email" type="email" placeholder="Nuova Email" class="input-email" value="<%=session.getAttribute("email")%>">
            <span> </span>
        </div>

        <div class="input-container">
            <input name="password" type="password" placeholder="Nuova Password" class="input-password">
            <span> </span>
        </div>

        <button type="submit" class="submitButton">
            <span>Conferma modifiche</span>
        </button>

    </form>
</div>

<!-- script per la verifica durante la modifica dei dati di accesso -->
<script>
    document.addEventListener("DOMContentLoaded", function() {
        var form = document.querySelector("form[action='UtenteControl?action=modificaDatiAccesso']");

        form.addEventListener("submit", function(event) {
            event.preventDefault();

            var email = form.email.value;
            var password = form.password.value;

            var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            var passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

            // Verifica dell'email tramite richiesta AJAX
            if (email !== "") {
                $.ajax({
                    url: "UtenteControl?action=verificaEmail",
                    type: "POST",
                    data: { email: email },
                    async: false,  // Esegue la richiesta in modo sincrono
                    success: function(response) {
                        // Controlla se la risposta del server indica che l'email esiste già
                        if (response === "Email verificata") {
                            alert("L'email non è stata modificata.");
                            form.email.focus(); // Focalizza il campo email
                            return false; // Interrompe la funzione
                        }
                    },
                    // Gestisce eventuali errori durante la richiesta AJAX
                    error: function() {
                        alert("Si è verificato un errore durante la verifica dell'email");
                        return false; // Interrompe la funzione
                    }
                });
            }

            // Verifica della password tramite espressione regolare
            if (!passwordRegex.test(password)) {
                alert("Il campo Password è errato, deve contenere almeno 8 caratteri, di cui almeno un carattere speciale, una lettera maiuscola, una lettera minuscola ed un numero");
                form.password.focus(); // Focalizza il campo password
                return false; // Interrompe la funzione
            }

            // Se tutte le verifiche passano, invia il form
            form.submit();
        });
    });
</script>

<%@ include file="footer.jsp"%>
</body>
</html>
