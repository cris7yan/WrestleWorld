package it.unisa.wrestleworld.model;

import java.io.Serializable;
import java.sql.Date;

public class MetodoPagamentoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idMetodoPagamento;
    private String numeroCarta;
    private String intestatario;
    private Date scadenza;
    private UtenteBean utente;

    public MetodoPagamentoBean() {
        idMetodoPagamento = 0;
        numeroCarta = "";
        intestatario = "";
        scadenza = null;
        utente = null;
    }

    // Metodi Get
    public int getIdMetodoPagamento() {
        return this.idMetodoPagamento;
    }

    public String getNumeroCarta() {
        return this.numeroCarta;
    }

    public String getIntestatario() {
        return this.intestatario;
    }

    public Date getDataScadenza () {
        return this.scadenza;
    }

    public UtenteBean getUtente() {
        return this.utente;
    }

    // Metodi Set
    public void setIdMetodoPagamento (int idMetodoPagamento) {
        this.idMetodoPagamento = idMetodoPagamento;
    }

    public void setNumeroCarta (String numeroCarta) {
        this.numeroCarta = numeroCarta;
    }

    public void setIntestatario (String intestatario) {
        this.intestatario = intestatario;
    }

    public void setDataScadenza (Date scadenza) {
        this.scadenza = scadenza;
    }

    public void setUtente (UtenteBean utente) {
        this.utente = utente;
    }

}
