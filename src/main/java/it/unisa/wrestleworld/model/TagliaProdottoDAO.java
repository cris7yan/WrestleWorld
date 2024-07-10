package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.util.List;

public interface TagliaProdottoDAO {

    List<TagliaProdottoBean> doRetrieveAllSizeByProduct (ProdottoBean prod) throws SQLException;

}
