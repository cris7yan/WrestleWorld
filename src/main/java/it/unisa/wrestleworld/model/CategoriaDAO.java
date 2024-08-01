package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CategoriaDAO {

    List<CategoriaBean> doRetrieveAll() throws SQLException;
    List<CategoriaBean> doRetrieveAllSuperstar() throws SQLException;
    List<CategoriaBean> doRetrieveAllPLE() throws SQLException;
    Map<String, List<CategoriaBean>> doRetrieveAllGroupedByType() throws SQLException;

    void doSaveCategory (CategoriaBean categoria) throws SQLException;

}
