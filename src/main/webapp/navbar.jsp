<%--
  Created by IntelliJ IDEA.
  User: cristyanesposito
--%>
<%@ page import="it.unisa.wrestleworld.util.Carrello" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%!
    String emailUtente = "";
    String nome = "";
    String cognome = "";
    Date dataNascita = null;
    String tipoUtente = "";
    Carrello carrello = null;
%>

<%
    synchronized (session) {
        session = request.getSession();
        emailUtente = (String) session.getAttribute("email");
        nome = (String) session.getAttribute("nome");
        cognome = (String) session.getAttribute("cognome");
        dataNascita = (Date) session.getAttribute("dataNascita");
        tipoUtente = (String) session.getAttribute("tipo");
        carrello = (Carrello) session.getAttribute("carrello");
    }
%>

<!DOCTYPE html>
<html lang="it" xml:lang="it">
<head>
    <meta charset="UTF-8">
    <link href="css/navbar.css" rel="stylesheet" type="text/css">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js" integrity="sha384-UG8ao2jwOWB7/oDdObZc6ItJmwUkR/PfMyt9Qs5AwX7PsnYn1CRKCTWyncPTWvaS" crossorigin="anonymous"></script>
    <title>WrestleWorld</title>
</head>
<body>

<header id="header" class="fixed-header">
    <a href="./index.jsp"><img src="img/logo/WrestleWorldTitleremove.png" class="logo" alt="Error logo"></a>

    <div>
        <ul id="navbar">

            <!-- Barra di ricerca -->
            <form action="ProdottoControl?action=ricerca" method="post">
                <div class="search-box">
                    <input class="search-input" type="search" name="ricerca" placeholder="Cerca il tuo prodotto" list="suggerimentiProdotti">
                    <div id="suggerimentiProdotti" class="suggerimentiProdotti"></div>
                    <button type="submit" class="search-button">
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                            <g id="SVGRepo_iconCarrier">
                                <path opacity="0.1" fill-rule="evenodd" clip-rule="evenodd" d="M12 21C16.9706 21 21 16.9706 21 12C21 7.02944 16.9706 3 12 3C7.02944 3 3 7.02944 3 12C3 16.9706 7.02944 21 12 21ZM11.5 7.75C9.42893 7.75 7.75 9.42893 7.75 11.5C7.75 13.5711 9.42893 15.25 11.5 15.25C13.5711 15.25 15.25 13.5711 15.25 11.5C15.25 9.42893 13.5711 7.75 11.5 7.75Z" fill="#323232"></path>
                                <path d="M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="#323232" stroke-width="2"></path>
                                <path d="M14 14L16 16" stroke="#323232" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
                                <path d="M15 11.5C15 13.433 13.433 15 11.5 15C9.567 15 8 13.433 8 11.5C8 9.567 9.567 8 11.5 8C13.433 8 15 9.567 15 11.5Z" stroke="#323232" stroke-width="2"></path>
                            </g>
                        </svg>
                    </button>
                </div>
            </form>

            <li>
                <a class="active" href="./catalogo.jsp">Catalogo</a>
            </li>

            <li id="userLogin">
                <a class="active" href="./login.jsp">My Account</a>
            </li>

            <%
                if(emailUtente == null) {
            %>
            <li>
                <a href="./login.jsp"><img src="img/sitoweb/cart.png" alt="Icon Error"></a>
            </li>
            <%
            }
            else {
                if(tipoUtente.equals("Admin")) {
            %>
            <li>
                <a href="./admin.jsp">UTENTI</a>
            </li>
            <%
            }
            else {
            %>
            <li>
                <a href="./carrello.jsp"><img src="img/sitoweb/cart.png" alt="Icon Error"></a>
            </li>
            <%
                }
            %>
            <li>
                <a href="UtenteControl?action=logout"><img src="img/sitoweb/logout.png" alt="Icon Error"></a>
            </li>
            <%
                }
            %>
        </ul>
    </div>

</header>

<div class="dropdownmenu fixed-nav">
    <ul>
        <li><a href="CategoriaControl?action=visualizzaSuperstar">Superstar</a></li>

        <li><a href="#">Title Belts</a>
            <ul id="submenu">
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Title Belts Replica">Title Belts</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Side Plates">Side Plates</a></li>
            </ul>
        </li>

        <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=T-Shirt">T-Shirts</a></li>

        <li><a href="#">Abbigliamento</a>
            <ul id="submenu">
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=T-Shirt">T-Shirts</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Felpe con cappuccio e Felpe">Felpe con cappuccio e Felpe</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Pantaloncini">Pantaloncini</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Canotte">Canotte</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Giacche">Giacche</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Cappelli">Cappelli</a></li>
            </ul>
        </li>

        <li><a href="#">Accessori</a>
            <ul id="submenu">
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Cover Phone">Cover Phone</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Zaini e borse">Zaini e borse</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Orologi">Orologi</a></li>
            </ul>
        </li>

        <li><a href="#">Collezionabili</a>
            <ul id="submenu">
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Figure">Figure</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Firmato">Firmati</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Foto">Foto</a></li>
                <li><a href="ProdottoControl?action=visualizzaProdottiCategoria&categoria=Memorabilia">Memorabilia</a></li>
            </ul>
        </li>

        <li><a href="CategoriaControl?action=visualizzaPremiumLiveEvent#ple-section">Premium Live Event</a></li>
    </ul>
</div>


<script>
    $(document).ready(function () {
        var lastSuggestions = [];   // memorizza gli ultimi suggerimenti ricevuti dalla chiamata AJAX

        $('.search-input').keyup(function () {
            var ricerca = $(this).val();    // Recupera il valore dell'input di ricerca
            if (ricerca !== '') {
                $.ajax({
                    url: 'ProdottoControl?action=suggerimentiRicerca',
                    method: 'POST',
                    data: { ricerca: ricerca },
                    dataType: 'json',
                    success: function (response) {
                        lastSuggestions = response.suggerimenti;    // Memorizza i suggerimenti ricevuti

                        // Costruisce l'HTML per visualizzare i suggerimenti
                        var sugg = '';
                        for (var i = 0; i < lastSuggestions.length; i++) {
                            sugg += '<div>' + lastSuggestions[i] + '</div>';
                        }
                        $('#suggerimentiProdotti').html(sugg).show();   // Inserisce gli elementi dei suggerimenti nell'elemento con id 'suggerimentiProdotti' e li mostra

                        $('#suggerimentiProdotti div').click(function () {
                            $('.search-input').val($(this).text());     // Imposta il valore dell'input di ricerca con il testo del suggerimento cliccato
                            $('#suggerimentiProdotti').hide();      // Nasconde l'elenco dei suggerimenti
                        });
                    }
                });
            } else {
                // Se l'input di ricerca è vuoto, nasconde l'elenco dei suggerimenti e resetta lastSuggestions
                $('#suggerimentiProdotti').html('').hide();
                lastSuggestions = [];
            }
        });

        // Gestisce il click al di fuori della casella di ricerca per nascondere l'elenco dei suggerimenti
        $(document).click(function (event) {
            if (!$(event.target).closest('.search-box').length) {
                $('#suggerimentiProdotti').hide();
            }
        });

        // Controlla l'invio del form per verificare se il termine di ricerca è valido
        $('form').submit(function (event) {
            var ricerca = $('.search-input').val();

            // Verifica se il valore di ricerca non è presente negli ultimi suggerimenti ricevuti
            if (lastSuggestions.indexOf(ricerca) === -1) {
                alert('Nessun prodotto trovato con il parametro di ricerca inserito.');
                event.preventDefault();     // Impedisce l'invio del form
            }
        });
    });
</script>

</body>
</html>
