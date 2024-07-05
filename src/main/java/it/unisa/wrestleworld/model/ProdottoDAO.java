package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.util.List;

public interface ProdottoDAO {

    List<ProdottoBean> doRetrieveAll() throws SQLException;
    List<ProdottoBean> doRetrieveBestSellers() throws SQLException;
    List<String> doRetrieveAllImages(ProdottoBean prod) throws SQLException;
    ProdottoBean doRetrieveByID (int id) throws SQLException;
    boolean checkProductAvailability (int id) throws SQLException;
    ProdottoBean doRetrieveByName (String nome) throws SQLException;
    List<String> doRetrieveBySuggest (String suggest) throws SQLException;
    List<ProdottoBean> doRetrieveByCategory (String category) throws SQLException;

}
