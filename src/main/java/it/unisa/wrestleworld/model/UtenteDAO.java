package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.util.Collection;

public interface UtenteDAO {

    void doSave (UtenteBean utente) throws SQLException;
    boolean doDelete (String emailUtente) throws SQLException;
    UtenteBean doRetriveByEmail (String email) throws SQLException;
    void doUpdateEmail (String email, String newEmail) throws SQLException;
    void doUpdatePassword (String email, String newPassword) throws SQLException;
    Collection<UtenteBean> doRetriveAllUtentiByOrder (String order) throws SQLException;
    Collection<UtenteBean> doRetriveAllUtenti () throws SQLException;

}
