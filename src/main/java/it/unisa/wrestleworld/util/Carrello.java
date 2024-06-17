package it.unisa.wrestleworld.util;

import it.unisa.wrestleworld.model.ProdottoBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrello implements Serializable {
    List<ProdottoBean> carrelloUtente;

    public Carrello() {
        carrelloUtente = new ArrayList<>();
    }

    public Carrello(List<ProdottoBean> carrello) {
        this.carrelloUtente = carrello;
    }

    public List<ProdottoBean> getCarrello() {
        return carrelloUtente;
    }

    public void setCarrello(List<ProdottoBean> carrello) {
        this.carrelloUtente = carrello;
    }

    public void addProdottoCarrello(ProdottoBean prodotto) {
        carrelloUtente.add(prodotto);
    }

    public void removeProdottoCarrello(ProdottoBean prodotto) {
        carrelloUtente.remove(prodotto);
    }
}
