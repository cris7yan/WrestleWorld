package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.sql.Date;

public interface UtenteDAO {

    void doSave (UtenteBean utente) throws SQLException;
    UtenteBean doRetrieveByEmailPassword (String email, String password) throws SQLException;
    UtenteBean doRetrieveByEmail (String email) throws SQLException;
    boolean verificaEmailEsistente (String email) throws SQLException;
    void doUpdateData (String nome, String cognome, Date data, String email) throws SQLException;
    void doUpdateEmail (String email, String newEmail) throws SQLException;
    void doUpdatePassword (String email, String newPassword) throws SQLException;

}
