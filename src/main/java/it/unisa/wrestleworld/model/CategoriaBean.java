package it.unisa.wrestleworld.model;

import java.io.Serializable;

public class CategoriaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tipo;
    private String nome;
    private String img;

    public CategoriaBean() {
        // Costruttore
    }

    // Metodi Get
    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getImg() {
        return img;
    }

    // Metodi Set
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
