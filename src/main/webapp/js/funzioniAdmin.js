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

function rendiDisponibileProdotto(idProdotto) {
    if (!confirm("Sei sicuro di voler rendere disponibile questo prodotto?")) {
        return;
    }

    $.ajax({
        url: 'AdminControl',
        type: 'POST',
        data: {
            action: 'rendiDisponibileProdotto',
            IDProd: idProdotto
        },
        success: function(response) {
            alert(response.message);
            location.reload();  // Ricarica la pagina per aggiornare la visualizzazione dei pulsanti
        },
        error: function(xhr, status, error) {
            alert("Errore durante l'operazione: " + xhr.responseText);
        }
    });
}

function aggiungiTaglia(idProdotto) {
    const taglia = document.getElementById('nuova-taglia').value;
    const quantita = document.getElementById('quantita-taglia').value;

    if (!taglia || quantita === "" || quantita <= 0) {
        alert("Inserisci una taglia e una quantità valide.");
        return;
    }

    $.ajax({
        url: 'AdminControl',
        type: 'POST',
        data: {
            action: 'aggiungiTagliaProdotto',
            IDProd: idProdotto,
            taglia: taglia,
            quantita: quantita
        },
        success: function(response) {
            alert(response.message);
            location.reload();
        },
        error: function(xhr, status, error) {
            alert("Errore durante l'aggiunta della taglia: " + xhr.responseText);
        }
    });
}

function modificaPrezzo(idProdotto) {
    const nuovoPrezzo = document.getElementById('nuovo-prezzo').value;

    if (nuovoPrezzo === "" || nuovoPrezzo <= 0) {
        alert("Inserisci un prezzo valido.");
        return;
    }

    $.ajax({
        url: 'AdminControl',
        type: 'POST',
        data: {
            action: 'modificaPrezzo',
            IDProd: idProdotto,
            prezzo: nuovoPrezzo
        },
        success: function(response) {
            alert(response.message);
            location.reload();
        },
        error: function(xhr, status, error) {
            alert("Errore durante l'aggiornamento del prezzo: " + xhr.responseText);
        }
    });
}

function modificaPrezzoOfferta(idProdotto) {
    const nuovoPrezzoOfferta = document.getElementById('nuovo-prezzo-offerta').value;

    if (nuovoPrezzoOfferta === "" || nuovoPrezzoOfferta < 0) {
        alert("Inserisci un prezzo offerta valido.");
        return;
    }

    $.ajax({
        url: 'AdminControl',
        type: 'POST',
        data: {
            action: 'modificaPrezzoOfferta',
            IDProd: idProdotto,
            prezzoOfferta: nuovoPrezzoOfferta
        },
        success: function(response) {
            alert(response.message);
            location.reload();
        },
        error: function(xhr, status, error) {
            alert("Errore durante l'aggiornamento del prezzo offerta: " + xhr.responseText);
        }
    });
}

function aggiungiAppartenenza(idProdotto) {
    const categoria = document.getElementById('nuova-categoria').value;

    if (!categoria) {
        alert("Inserisci una categoria valida.");
        return;
    }

    $.ajax({
        url: 'AdminControl',
        type: 'POST',
        data: {
            action: 'aggiungiAppartenenzaProdotto',
            IDProd: idProdotto,
            categoria: categoria
        },
        success: function(response) {
            alert(response.message);
            location.reload();
        },
        error: function(xhr, status, error) {
            alert("Errore durante l'aggiunta dell'appartenenza: " + xhr.responseText);
        }
    });
}
