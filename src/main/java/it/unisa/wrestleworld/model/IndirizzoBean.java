package it.unisa.wrestleworld.model;

import java.io.Serializable;

public class IndirizzoBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idIndirizzo;
    private String via;
    private String citta;
    private String provincia;
    private String cap;
    private String nomeCompleto;
    private UtenteBean utente;

    // Costruttore
    public IndirizzoBean () {
        idIndirizzo = 0;
        via = "";
        citta = "";
        provincia = "";
        cap = "";
        nomeCompleto = "";
        utente = null;
    }

    // Metodi Get
    public int getIdIndirizzo () {
        return this.idIndirizzo;
    }

    public String getViaIndirizzo () {
        return this.via;
    }

    public String getCittaIndirizzo () {
        return this.citta;
    }

    public String getProvinciaIndirizzo () {
        return this.provincia;
    }

    public String getCAPIndirizzo () {
        return this.cap;
    }

    public String getNomeCompletoIndirizzo () {
        return this.nomeCompleto;
    }

    public UtenteBean getUtenteIndirizzo () {
        return this.utente;
    }


    // Metodi Set
    public void setIdIndirizzo (int id) {
        this.idIndirizzo = id;
    }

    public void setViaIndirizzo (String via) {
        this.via = via;
    }

    public void setCittaIndirizzo (String citta) {
        this.citta = citta;
    }

    public void setProvinciaIndirizzo (String prov) {
        this.provincia = prov;
    }

    public void setCAPIndirizzo (String cap) {
        this.cap = cap;
    }

    public void setNomeCompletoIndirizzo (String nome) {
        this.nomeCompleto = nome;
    }

    public void setUtenteIndirizzo (UtenteBean utente) {
        this.utente = utente;
    }

}
