package it.unisa.wrestleworld.util;

import it.unisa.wrestleworld.model.ProdottoBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrello implements Serializable {
    private List<ProdottoBean> carrelloUtente;

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

    public void removeProdottoCarrello(int idProd) {
        for(ProdottoBean prod : carrelloUtente) {
            if(prod.getIDProdotto() == idProd) {
                this.carrelloUtente.remove(prod);
                break;
            }
        }
    }

    public void svuotaCarrello () {
        carrelloUtente.clear();
    }
}
