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
    List<ProdottoBean> doRetrieveBestOnOffer () throws SQLException;
    void doDecreaseProductQuantity (int id, String taglia, int quantity) throws SQLException;
    List<ProdottoBean> getProdottiOrdinatiPerCategoria() throws SQLException;

    void doSaveProduct (ProdottoBean prod, List<String> immagini, List<TagliaProdottoBean> taglie, List<CategoriaBean> categorie) throws SQLException;
    void doDeleteProduct (int id) throws SQLException;
    void addQuantityProduct (int id, String taglia, int quantity) throws SQLException;
    void makeProductUnavailable (int id) throws SQLException;
    void makeProductAvailable (int id) throws SQLException;
    void doUpdateProductPrice (int id, float newPrice) throws SQLException;
    void doUpdateProductOfferPrice (int id, float newOfferPrice) throws SQLException;
    void doAddProductCategory(int idProdotto, String nomeCategoria) throws SQLException;

}
