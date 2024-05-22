package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.util.List;

public interface MetodoPagamentoDAO {

    void doSave(MetodoPagamentoBean metodoPagamento, UtenteBean utente) throws SQLException;
    List<MetodoPagamentoBean> doRetrieveAllByUtente (String email) throws SQLException;
    MetodoPagamentoBean doRetrieveByID (int id) throws SQLException;
    List<Integer> doRetrieveAllID (String email) throws SQLException;
    void doDelete (int id) throws SQLException;

}
