package it.unisa.wrestleworld.model;

import java.sql.SQLException;

public interface UtenteDAO {

    void doSave (UtenteBean utente) throws SQLException;
    UtenteBean doRetrieveByEmailPassword (String email, String password) throws SQLException;
    boolean verificaEmailEsistente (String email) throws SQLException;
}
