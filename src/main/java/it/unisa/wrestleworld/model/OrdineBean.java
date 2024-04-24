package it.unisa.wrestleworld.model;

import java.io.Serializable;
import java.sql.Date;

public class OrdineBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idOrdine;
    private Date dataOrdine;
    private float prezzoTotale;
    private UtenteBean utente;

    public OrdineBean () {
        // Costruttore
    }

    // Metodi Get
    public int getIdOrdine () {
        return this.idOrdine;
    }

    public Date getDataOrdine () {
        return this.dataOrdine;
    }

    public float getPrezzoTotaleOrdine () {
        return this.prezzoTotale;
    }

    public UtenteBean getUtenteOrdine () {
        return this.utente;
    }

    // Metodi Set
    public void setIdOrdine (int id) {
        this.idOrdine = id;
    }

    public void setDataOrdine (Date data) {
        this.dataOrdine = data;
    }

    public void setPrezzoTotaleOrdine (float prezzo) {
        this.prezzoTotale = prezzo;
    }

    public void setUtenteOrdine (UtenteBean utente) {
        this.utente = utente;
    }

}
