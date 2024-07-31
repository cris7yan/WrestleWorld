function aggiungiQuantita(idProdotto, taglia) {
    const quantita = document.getElementById('quantita-' + taglia).value;

    if (quantita === "" || quantita <= 0) {
        alert("Inserisci una quantità valida.");
        return;
    }

    $.ajax({
        url: 'AdminControl',
        type: 'POST',
        data: {
            action: 'incrementaQuantitaProdotto',
            IDProd: idProdotto,
            taglia: taglia,
            quantita: quantita
        },
        success: function(response) {
            alert(response.message);
            location.reload();
        },
        error: function(xhr, status, error) {
            alert("Errore durante l'aggiornamento della quantità: " + xhr.responseText);
        }
    });
}

function eliminaProdotto(idProdotto) {
    if (!confirm("Sei sicuro di voler eliminare questo prodotto?")) {
        return;
    }

    $.ajax({
        url: 'AdminControl',
        type: 'POST',
        data: {
            action: 'eliminaProdotto',
            IDProd: idProdotto
        },
        success: function (response) {
            alert(response.message);
            window.location.href = 'catalogo.jsp';
        },
        error: function (xhr, status, error) {
            alert("Errore durante l'eliminazione del prodotto: " + xhr.responseText);
        }
    });
}

function rendiIndisponibileProdotto(idProdotto) {
    if (!confirm("Sei sicuro di voler rendere indisponibile questo prodotto?")) {
        return;
    }

    $.ajax({
        url: 'AdminControl',
        type: 'POST',
        data: {
            action: 'rendiIndisponibileProdotto',
            IDProd: idProdotto
        },
        success: function(response) {
            alert(response.message);
            location.reload();
        },
        error: function(xhr, status, error) {
            alert("Errore durante l'operazione: " + xhr.responseText);
        }
    });
}