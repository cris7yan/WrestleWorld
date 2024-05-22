package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.util.List;

public interface ProdottoDAO {
    List<ProdottoBean> doRetrieveAll() throws SQLException;
    List<ProdottoBean> doRetrieveBestSellers() throws SQLException;
    List<String> doRetrieveAllImages(ProdottoBean prod) throws SQLException;
    ProdottoBean doRetrieveByID (int id) throws SQLException;

}
