package it.unisa.wrestleworld.model;

import java.io.Serializable;

public class ProdottoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // Variabili Prodotto
    private int IDProdotto;
    private String nome;
    private String descrizione;
    private String materiale;
    private String marca;
    private String modello;
    private float prezzo;
    private boolean disponibilita;

    public ProdottoBean () { }

    // Metodi Get
    public int getIDProdotto () {
        return this.IDProdotto;
    }

    public String getNomeProdotto () {
        return this.nome;
    }

    public String getDescrizioneProdotto () {
        return this.descrizione;
    }

    public String getMaterialeProdotto () {
        return this.materiale;
    }

    public String getMarcaProdotto () {
        return this.marca;
    }

    public String getModelloProdotto () {
        return this.modello;
    }

    public float getPrezzoProdotto () {
        return this.prezzo;
    }

    public boolean getDisponibilitaProdotto () {
        return this.disponibilita;
    }

    // Metodi Set
    public void setIDProdotto (int idProdotto) {
        this.IDProdotto = idProdotto;
    }

    public void setNomeProdotto (String nome) {
        this.nome = nome;
    }

    public void setDescrizioneProdotto (String descrizione) {
        this.descrizione = descrizione;
    }

    public void setMaterialeProdotto (String materiale) {
        this.materiale = materiale;
    }

    public void setMarcaProdotto (String marca) {
        this.marca = marca;
    }

    public void setModelloProdotto (String modello) {
        this.modello = modello;
    }

    public void setPrezzoProdotto (float prezzo) {
        this.prezzo = prezzo;
    }

    public void setDisponibilitaProdotto (boolean disp) {
        this.disponibilita = disp;
    }

}