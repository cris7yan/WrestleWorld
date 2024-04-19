package it.unisa.wrestleworld.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ProdottoDAO {

    // void doSave (ProdottoBean prodotto) throws SQLException;
    // boolean doDelete(String ID_Prodotto) throws SQLException;
    // ProdottoBean doRetrieveByKey(String ID_Prodotto) throws SQLException;
    List<ProdottoBean> doRetrieveAll() throws SQLException;
    // Collection<ProdottoBean> doRetrieveAllByOrder(String order) throws SQLException;
    // Collection<ProdottoBean> doRetrieveAllSuggest(String stringPart) throws SQLException, IOException;

}
