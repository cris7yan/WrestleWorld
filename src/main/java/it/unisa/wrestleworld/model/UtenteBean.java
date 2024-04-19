package it.unisa.wrestleworld.model;

import java.io.Serializable;

public class UtenteBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String email;
    private String nome;
    private String cognome;
    private String password;
    private String dataNascita;
    private String tipoUtente;

    public UtenteBean () {
        // Costruttore
    }

    // Metodi Get
    public String getEmail () {
        return this.email;
    }

    public String getNome () {
        return this.nome;
    }

    public String getCognome () {
        return this.cognome;
    }

    public String getPassword () {
        return this.password;
    }

    public String getDataNascita () {
        return this.dataNascita;
    }

    public String getTipoUtente () {
        return this.tipoUtente;
    }

    
    // Metodi Set
    public void setEmail (String email) {
        this.email = email;
    }

    public void setNome (String nome) {
        this.nome = nome;
    }

    public void setCognome (String cognome) {
        this.cognome = cognome;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public void setDataNascita (String data) {
        this.dataNascita = data;
    }

    public void setTipoUtente (String tipo) {
        this.tipoUtente = tipo;
    }

}