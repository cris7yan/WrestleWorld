package it.unisa.wrestleworld.util;

import it.unisa.wrestleworld.model.ProdottoBean;
import it.unisa.wrestleworld.model.TagliaProdottoBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrello implements Serializable {
    private List<ProdottoBean> carrelloUtente;

    public Carrello () {
        carrelloUtente = new ArrayList<>();
    }

    public Carrello (List<ProdottoBean> carrello) {
        this.carrelloUtente = carrello;
    }

    public List<ProdottoBean> getCarrello () {
        return carrelloUtente;
    }

    public void setCarrello (List<ProdottoBean> carrello) {
        this.carrelloUtente = carrello;
    }

    // Metodi per la gestione dei prodotti nel carrello

    private boolean verificaQuantitaDisponibile (ProdottoBean prodotto, String taglia, int quantitaRichiesta) {
        for (TagliaProdottoBean tagliaProdotto : prodotto.getTaglieProdotto()) {
            if (tagliaProdotto.getTaglia().equals(taglia)) {
                return tagliaProdotto.getQuantita() >= quantitaRichiesta;
            }
        }
        return false;
    }

    public void aggiungiProdottoCarrello (ProdottoBean prodotto, String taglia) {
        int numProd = 0;    // numero di quantità del prodotto in carrello
        boolean presente = false;   // boolean che verifica se un prodotto è già presente nella lista oppure no

        for (ProdottoBean prod : this.carrelloUtente) {
            if(prod.getIDProdotto() == prodotto.getIDProdotto() && prod.getTagliaSelezionata().equals(taglia)) {
                numProd = prod.getQuantitaCarrello();
                presente = true;
            }
        }

        int nuovaQuantita = numProd + 1;

        if (verificaQuantitaDisponibile(prodotto, taglia, nuovaQuantita)) {
            if (presente) {
                for (ProdottoBean prod : this.carrelloUtente) {
                    if (prod.getIDProdotto() == prodotto.getIDProdotto() && prod.getTagliaSelezionata().equals(taglia)) {
                        prod.aumentaQuantitaCarrello();
                    }
                }
            } else {
                prodotto.setQuantitaCarrello(1);
                prodotto.setTagliaSelezionata(taglia);
                this.carrelloUtente.add(prodotto);
            }
        } else {
            // Gestisci il caso in cui la quantità richiesta non sia disponibile
            System.out.println("Quantità richiesta non disponibile per il prodotto " + prodotto.getNomeProdotto() + " nella taglia " + taglia);
        }
    }

    public void rimuoviProdottoCarrello (int idProdotto, String taglia) {
        for (ProdottoBean prod : this.carrelloUtente) {
            boolean is = prod.getIDProdotto() == idProdotto && prod.getTagliaSelezionata().equals(taglia);

            if (is) {
                if(prod.getQuantitaCarrello() == 1) {
                    this.carrelloUtente.remove(prod);
                } else {
                    prod.decrementaQuantitaCarrello();
                }
                break;
            }
        }
    }

    public void eliminaProdottoCarrello (int idProdotto) {
        for (ProdottoBean prod : this.carrelloUtente) {
            boolean is = prod.getIDProdotto() == idProdotto;

            if (is) {
                this.carrelloUtente.remove(prod);
                break;
            }
        }
    }

    public void svuotaCarrello () {
        carrelloUtente.clear();
    }

    public float getPrezzoCarrello () {
        float prezzoTotale = 0;
        float prezzoProd = 0;

        for (ProdottoBean prod : this.carrelloUtente) {
            if(prod.getPrezzoOffertaProdotto() > 0 && prod.getPrezzoOffertaProdotto() <= prod.getPrezzoProdotto()) {
                prezzoProd = prod.getPrezzoOffertaProdotto();
            } else {
                prezzoProd = prod.getPrezzoProdotto();
            }
            prezzoTotale += prezzoProd;
        }
        return prezzoTotale;
    }

}
