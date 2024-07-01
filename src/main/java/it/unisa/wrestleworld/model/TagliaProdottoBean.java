package it.unisa.wrestleworld.model;

import java.io.Serializable;

public class TagliaProdottoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idTagliaProdotto;
    private int idProdotto;
    private String taglia;
    private int quantita;

    public TagliaProdottoBean () { }

    // Metodi Get
    public int getIdTagliaProdotto() {
        return this.idTagliaProdotto;
    }

    public int getIdProdotto() {
        return this.idProdotto;
    }

    public String getTaglia() {
        return this.taglia;
    }

    public int getQuantita() {
        return this.quantita;
    }

    // Metodi Set
    public void setIdTagliaProdotto(int idTagliaProdotto) {
        this.idTagliaProdotto = idTagliaProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }


    public void setTaglia(String taglia) {
        this.taglia = taglia;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

}
