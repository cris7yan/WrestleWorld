package it.unisa.wrestleworld.model;

import java.sql.SQLException;
import java.util.List;

public interface CategoriaDAO {

    List<CategoriaBean> doRetrieveAll() throws SQLException;
    List<CategoriaBean> doRetrieveAllSuperstar() throws SQLException;
    List<CategoriaBean> doRetrieveAllPLE() throws SQLException;

}
