package it.unisa.wrestleworld.model;

import java.io.Serializable;
import java.sql.SQLException;
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
    private List<CategoriaBean> categorie;

    // Variabili per la gestione del carrello
    private int quantitaCarrello;
    private String tagliaCarrello;

    private static ProdottoModel prodModel = new ProdottoModel();

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

    public List<CategoriaBean> getCategorieProdotto() {  // Aggiungi questo metodo
        return this.categorie;
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

    public void setCategorieProdotto (List<CategoriaBean> categorie) {  // Aggiungi questo metodo
        this.categorie = categorie;
    }

    // Altri metodi
    public void addTagliaProdotto (TagliaProdottoBean tagliaProdotto) {
        this.taglieProdotto.add(tagliaProdotto);
    }

    public void removeTagliaProdotto (TagliaProdottoBean tagliaProdotto) {
        this.taglieProdotto.remove(tagliaProdotto);
    }

    public void addCategoria (CategoriaBean categoria) {
        this.categorie.add(categoria);
    }

    public void removeCategoria (CategoriaBean categoria) {
        this.categorie.remove(categoria);
    }

    public int getQuantitaPerTaglia (String taglia) {
        for (TagliaProdottoBean tagliaProdotto : this.taglieProdotto) {
            if (tagliaProdotto.getTaglia().equals(taglia)) {
                return tagliaProdotto.getQuantita();
            }
        }
        return -1; // indica che la taglia non è stata trovata
    }

    // Metodi per la gestione del carrello
    public int getQuantitaCarrello () {
        return this.quantitaCarrello;
    }

    public void setQuantitaCarrello (int quantitaCarrello) {
        this.quantitaCarrello = quantitaCarrello;
    }

    public void aumentaQuantitaCarrello () {
        this.quantitaCarrello++;
    }

    public void decrementaQuantitaCarrello () {
        this.quantitaCarrello--;
    }

    public void aumentaQuantitaCarrello (int quantita) {
        this.quantitaCarrello += quantita;
    }

    public void decrementaQuantitaCarrello (int quantita) {
        this.quantitaCarrello -= quantita;
    }

    public String getTagliaSelezionata() {
        return this.tagliaCarrello;
    }

    public void setTagliaSelezionata(String tagliaSelezionata) {
        this.tagliaCarrello = tagliaSelezionata;
    }

    // Metodi per la gestione dei filtri
    public float getPrezzoVenditaProdotto () {
        if(this.prezzoOfferta > 0 && this.prezzoOfferta < this.prezzo){
            return this.prezzoOfferta;
        } else
            return this.prezzo;
    }

    public String getCategoriaProdotto() {
        try {
            return prodModel.getTipoCategoria(this.getIDProdotto());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSessoProdotto () {
        try {
            return prodModel.getSessoProdotto(this.getIDProdotto());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean prodottoFirmato () {
        try {
            return prodModel.isFirmato(this.getIDProdotto());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
