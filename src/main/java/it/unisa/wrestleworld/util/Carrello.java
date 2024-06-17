package it.unisa.wrestleworld.util;

import it.unisa.wrestleworld.model.ProdottoBean;
import java.util.ArrayList;
import java.util.List;

public class Carrello {
    List<ProdottoBean> carrello;

    public Carrello() {
        carrello = new ArrayList<>();
    }

    public Carrello(List<ProdottoBean> carrello) {
        this.carrello = carrello;
    }

    public List<ProdottoBean> getCarrello() {
        return carrello;
    }

    public void setCarrello(List<ProdottoBean> carrello) {
        this.carrello = carrello;
    }

    public void addProdottoCarrello(ProdottoBean prodotto) {
        carrello.add(prodotto);
    }

    public void removeProdottoCarrello(ProdottoBean prodotto) {
        carrello.remove(prodotto);
    }
}
