package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.util.List;

public interface ProdottoDAO {
    List<ProdottoBean> doRetrieveAll() throws SQLException;

}
