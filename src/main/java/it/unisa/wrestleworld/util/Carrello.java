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

    public void aggiungiProdottoCarrello(ProdottoBean prodotto, String taglia) {
        int numProd = getQuantitaProdottoInCarrello(prodotto, taglia);
        boolean presente = numProd > 0;

        int nuovaQuantita = numProd + 1;

        if (verificaQuantitaDisponibile(prodotto, taglia, nuovaQuantita)) {
            if (presente) {
                aumentaQuantitaProdotto(prodotto, taglia);
            } else {
                aggiungiNuovoProdottoAlCarrello(prodotto, taglia);
            }
        }
    }

    private int getQuantitaProdottoInCarrello(ProdottoBean prodotto, String taglia) {
        for (ProdottoBean prod : this.carrelloUtente) {
            if (prod.getIDProdotto() == prodotto.getIDProdotto() && prod.getTagliaSelezionata().equals(taglia)) {
                return prod.getQuantitaCarrello();
            }
        }
        return 0;
    }

    private void aumentaQuantitaProdotto(ProdottoBean prodotto, String taglia) {
        for (ProdottoBean prod : this.carrelloUtente) {
            if (prod.getIDProdotto() == prodotto.getIDProdotto() && prod.getTagliaSelezionata().equals(taglia)) {
                prod.aumentaQuantitaCarrello();
                break;
            }
        }
    }

    private void aggiungiNuovoProdottoAlCarrello(ProdottoBean prodotto, String taglia) {
        prodotto.setQuantitaCarrello(1);
        prodotto.setTagliaSelezionata(taglia);
        this.carrelloUtente.add(prodotto);
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

    public float getPrezzoCarrello() {
        float prezzoTotale = 0;

        for (ProdottoBean prod : this.carrelloUtente) {
            float prezzoProdotto;
            if (prod.getPrezzoOffertaProdotto() > 0 && prod.getPrezzoOffertaProdotto() <= prod.getPrezzoProdotto()) {
                prezzoProdotto = prod.getPrezzoOffertaProdotto();
            } else {
                prezzoProdotto = prod.getPrezzoProdotto();
            }
            prezzoTotale += prezzoProdotto * prod.getQuantitaCarrello();
        }

        return prezzoTotale;
    }


}
