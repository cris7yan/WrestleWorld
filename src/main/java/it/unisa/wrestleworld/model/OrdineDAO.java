package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.util.List;

public interface OrdineDAO {

    List<OrdineBean> doRetrieveAllByEmail (String email) throws SQLException;
    List<ProdottoBean> doRetrieveOrdineByID (int id) throws SQLException;

}
