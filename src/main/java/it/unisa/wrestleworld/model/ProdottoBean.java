package it.unisa.wrestleworld.model;

import java.io.Serializable;
import java.util.List;

public class ProdottoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // Variabili Prodotto
    private int idProdotto;
    private String nome;
    private String descrizione;
    private String materiale;
    private String marca;
    private String modello;
    private float prezzo;
    private float prezzoOfferta;
    private boolean disponibilita;
    private List<String> img;
    private List<TagliaProdottoBean> taglieProdotto;

    public ProdottoBean () {
        // Costruttore
    }

    // Metodi Get
    public int getIDProdotto () {
        return this.idProdotto;
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

    public float getPrezzoOffertaProdotto () {
        return this.prezzoOfferta;
    }

    public boolean getDisponibilitaProdotto () {
        return this.disponibilita;
    }

    public List<String> getImmaginiProdotto () {
        return this.img;
    }

    public List<TagliaProdottoBean> getTaglieProdotto() {
        return this.taglieProdotto;
    }

    // Metodi Set
    public void setIDProdotto (int idProdotto) {
        this.idProdotto = idProdotto;
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

    public void setPrezzoOffertaProdotto (float prezzoOfferta) {
        this.prezzoOfferta = prezzoOfferta;
    }

    public void setDisponibilitaProdotto (boolean disp) {
        this.disponibilita = disp;
    }

    public void setImmaginiProdotto (String img) {
        this.img.add(img);
    }

    public void setTaglieProdotto (List<TagliaProdottoBean> taglie) {
        this.taglieProdotto = taglie;
    }

    public void addTagliaProdotto(TagliaProdottoBean tagliaProdotto) {
        this.taglieProdotto.add(tagliaProdotto);
    }

    public void removeTagliaProdotto(TagliaProdottoBean tagliaProdotto) {
        this.taglieProdotto.remove(tagliaProdotto);
    }

}
