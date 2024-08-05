package it.unisa.wrestleworld.model;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface OrdineDAO {

    void doSave (Date data, float prezzoTotale, String emailUtente) throws SQLException;
    void doUpdateComprendeOrdine (int idOrdine, int idProdotto, String taglia, int quantita, float prezzoUnitario) throws SQLException;
    List<OrdineBean> doRetrieveAllByEmail (String email) throws SQLException;
    List<ProdottoBean> doRetrieveOrdineByID (int id) throws SQLException;
    int doRetrieveLastOrdineID () throws SQLException;
    OrdineBean doRetrieveOrdineById (int idOrdine) throws SQLException;

    List<OrdineBean> doRetrieveAllOrdini () throws SQLException;
}
