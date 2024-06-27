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
%>

<%
    synchronized (session) {
        session = request.getSession();
        emailUtente = (String) session.getAttribute("email");
        nome = (String) session.getAttribute("nome");
        cognome = (String) session.getAttribute("cognome");
        dataNascita = (Date) session.getAttribute("dataNascita");
        tipoUtente = (String) session.getAttribute("tipo");
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/login.css" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <title>WrestleWorld | Login</title>
</head>
<body>

    <%
        if(emailUtente != null) {
            response.sendRedirect("./profiloUtente.jsp");
        }
    %>

    <div class="title-container">
        <h2>Accedi </h2>
        <h6>oppure </h6>
        <h2>Registrati</h2>
    </div>
    <div class="container" id="container">
        <div class="form-container sign-up-container" id="registrazione">
            <form action="UtenteControl?action=registrazione" method="post">
                <h1>Crea il tuo account da Superstar</h1>
                <br>
                <span>Registrati anche tu nel roster di WrestleWorld e diventa anche tu una Superstar!</span>
                <input type="email" name="email" placeholder="E-mail" required/>
                <input type="password" name="password" placeholder="Password" required/>
                <input type="text" name="nome" placeholder="Nome" required/>
                <input type="text" name="cognome" placeholder="Cognome" required/>
                <input type="date" name="dataNascita" placeholder="Data di nascita" required/>
                <button type="submit">Registrati</button>
            </form>
        </div>
        <div class="form-container sign-in-container">
            <form action="UtenteControl?action=login" method="post" id="login">
                <h1>Log in</h1>
                <br>
                <span>Usa il tuo account</span>
                <input type="email" name="email" placeholder="E-mail" required/>
                <input type="password" name="password" placeholder="Password" required/>
                <button type="submit">Accedi</button>
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
    <br><br>

    <span>
        <p><a href="index.jsp">Home</a></p>
    </span>

    <!-- script per la verifica durante la fase di registrazione -->
    <script>
        // Attende che il contenuto del DOM sia completamente caricato prima di eseguire il codice
        document.addEventListener("DOMContentLoaded", function() {
            // Seleziona il form con il nome "registrazione"
            var form = document.querySelector("form[action='UtenteControl?action=registrazione']"); // Seleziona il form della registrazione

            // Aggiunge un evento di ascolto per l'evento "submit" del form
            form.addEventListener("submit", function(event) {
                event.preventDefault(); // Previene il submit del form e l'invio della richiesta HTTP

                // Recupera i valori degli input "email", "password" e "dataNascita" all'interno del form
                var email = form.email.value;
                var password = form.password.value;
                var nome = form.nome.value;
                var cognome = form.cognome.value;
                var data = form.dataNascita.value;

                var passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;   // Espressione regolare per la password
                var nameRegex = /^[A-Za-z\s]+$/;    // Espressione regolare per nome e cognome (nessun carattere speciale)
                var dataDiOggi = new Date(); // Data di oggi
                var dataLimite = new Date(); // Data limite per l'età
                dataLimite.setFullYear(dataDiOggi.getFullYear() - 16); // Calcola la data limite per i 16 anni

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
                                alert("L'email inserita risulta già registrata");
                                form.email.focus(); // Focalizza il campo email
                                return false; // Interrompe la funzione
                            }
                        },
                        // Gestisce eventuali errori durante la richiesta AJAX
                        error: function() {
                            alert("Si è verificato un errore durante la fase di registrazione");
                            return false; // Interrompe la funzione
                        }
                    });
                }

                // Verifica del nome e cognome tramite espressione regolare
                if (!nameRegex.test(nome)) {
                    alert("Il campo Nome non può contenere caratteri speciali o numeri");
                    form.nome.focus(); // Focalizza il campo nome
                    return false; // Interrompe la funzione
                }

                if (!nameRegex.test(cognome)) {
                    alert("Il campo Cognome non può contenere caratteri speciali o numeri");
                    form.cognome.focus(); // Focalizza il campo cognome
                    return false; // Interrompe la funzione
                }

                // Verifica della password tramite espressione regolare
                if (!passwordRegex.test(password)) {
                    alert("Il campo Password è errato, deve contenere almeno 8 caratteri, di cui almeno un carattere speciale, una lettera maiuscola, una lettera minuscola ed un numero");
                    form.password.focus(); // Focalizza il campo password
                    return false; // Interrompe la funzione
                }

                // Verifica della data di nascita
                if (data !== "") {
                    var dataNascita = new Date(data);
                    // Controlla se la data di nascita è valida
                    if (dataNascita > dataDiOggi) {
                        alert("Data inserita non valida. Inserirne una valida.");
                        form.dataNascita.focus(); // Focalizza il campo data di nascita
                        return false; // Interrompe la funzione
                    } else if (dataNascita > dataLimite) {
                        alert("Per registrarti devi avere almeno 16 anni");
                        form.dataNascita.focus(); // Focalizza il campo data di nascita
                        return false; // Interrompe la funzione
                    }
                }

                // Se tutte le verifiche passano, invia il form
                form.submit();
            });
        });
    </script>

    <!-- script per la gestione del reinderizzamento dopo che si verifica un errore -->
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            // Controlla se c'è un parametro di errore nella query string
            var urlParams = new URLSearchParams(window.location.search);
            if (urlParams.has('error') && urlParams.get('error') === 'registration') {
                // Attiva il pannello di registrazione
                document.getElementById('container').classList.add("right-panel-active");
            }

            // Event listener per i bottoni di passaggio tra login e registrazione
            document.getElementById('signUp').addEventListener('click', function() {
                document.getElementById('container').classList.add("right-panel-active");
            });

            document.getElementById('signIn').addEventListener('click', function() {
                document.getElementById('container').classList.remove("right-panel-active");
            });
        });
    </script>

    <!-- script per la verifica durante la fase di login -->
    <script>
        // Attende che il contenuto del DOM sia completamente caricato prima di eseguire il codice
        document.addEventListener("DOMContentLoaded", function() {
            var form = document.querySelector("form[action='UtenteControl?action=login']"); // Seleziona il form del login

            // Aggiunge un evento di ascolto per l'evento "submit" del form
            form.addEventListener("submit", function(event) {
                event.preventDefault();  // Previene il submit del form e l'invio della richiesta HTTP

                // Recupera i valori degli input all'interno del form
                var email = form.querySelector("[name='email']").value;
                var password = form.querySelector("[name='password']").value;

                console.log("Email: " + email);  // Debug: controlla se il valore è corretto
                console.log("Password: " + password);  // Debug: controlla se il valore è corretto

                // Verifica dell'utente tramite richiesta AJAX
                if (email !== "" && password !== "") {
                    $.ajax ({
                        url: "UtenteControl?action=verificaUtenteRegistrato",
                        type: "POST",
                        data: { email: email, password: password },
                        success: function (response) {
                            console.log("Server Response: " + response);  // Debug: controlla la risposta del server
                            if(response.trim() === "Utente non esistente") {
                                alert("Credenziali inserite non valide");
                            } else {
                                form.submit();  // Invia il form dopo la verifica
                            }
                        },
                        error: function () {
                            alert("Si è verificato un errore durante la verifica dell'email.");
                        }
                    });
                }
            });
        });
    </script>

    <script src="js/login.js" type="text/javascript"></script>
</body>
</html>
