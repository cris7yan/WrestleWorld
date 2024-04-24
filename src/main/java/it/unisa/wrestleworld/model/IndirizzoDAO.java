package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.util.List;

public interface IndirizzoDAO {

    void doSave (IndirizzoBean indirizzo, UtenteBean utente) throws SQLException;
    IndirizzoBean doRetrieveByID (int id) throws SQLException;
    List<IndirizzoBean> doRetrieveAllByUtente (String email) throws SQLException;
    List<Integer> doRetrieveAllID (String email) throws SQLException;
    void doDelete (int id) throws SQLException;

}
