package it.unisa.wrestleworld.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaModel implements CategoriaDAO {
    private static Logger logger = Logger.getLogger(CategoriaModel.class.getName());
    private static DataSource dataSource;

    private static final String TABLE_CATEGORIA = "Categoria";
    private static final String TIPO_CATEGORIA_ATTRIBUTE = "TipoCategoria";
    private static final String NOME_CATEGORIA_ATTRIBUTE = "NomeCategoria";
    private static final String NOME_IMG_ATTRIBUTE ="NomeImgCategoria";

    private static final String SELECT_ALL = "SELECT * FROM ";

    private static final String MSG_ERROR_PS = "Errore durante la chiusura del PreparedStatement";
    private static final String MSG_ERROR_CONN = "Errore durante la chiusura della connessione";


    // approccio per ottenere risorse dal database
    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            dataSource = (DataSource) envCtx.lookup("jdbc/wrestleworld");
        } catch (NamingException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che restituisce tutte le categorie nel database
     * @return prodotti
     * @throws SQLException
     */
    public synchronized List<CategoriaBean> doRetrieveAll() throws SQLException {
        List<CategoriaBean> categorie = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = SELECT_ALL + TABLE_CATEGORIA;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoriaBean cat = new CategoriaBean();

                cat.setTipo(rs.getString(TIPO_CATEGORIA_ATTRIBUTE));
                cat.setNome(rs.getString(NOME_CATEGORIA_ATTRIBUTE));
                cat.setImg(rs.getString(NOME_IMG_ATTRIBUTE));

                categorie.add(cat);
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            // chiusura PreparedStatement e Connection
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, MSG_ERROR_PS, e);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, MSG_ERROR_CONN, e);
            }
        }
        return categorie;
    }


    /**
     * funzione che restituisce tutte le categorie Superstar nel database
     * @return prodotti
     * @throws SQLException
     */
    public synchronized List<CategoriaBean> doRetrieveAllSuperstar() throws SQLException {
        List<CategoriaBean> superstar = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = SELECT_ALL + TABLE_CATEGORIA + " WHERE TipoCategoria = 'Superstar'";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoriaBean cat = new CategoriaBean();

                cat.setTipo(rs.getString(TIPO_CATEGORIA_ATTRIBUTE));
                cat.setNome(rs.getString(NOME_CATEGORIA_ATTRIBUTE));
                cat.setImg(rs.getString(NOME_IMG_ATTRIBUTE));

                superstar.add(cat);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            // chiusura PreparedStatement e Connection
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, MSG_ERROR_PS, e);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, MSG_ERROR_CONN, e);
            }
        }
        return superstar;
    }


    /**
     * funzione che restituisce tutte le categorie PLE nel database
     * @return prodotti
     * @throws SQLException
     */
    public synchronized List<CategoriaBean> doRetrieveAllPLE() throws SQLException {
        List<CategoriaBean> ple = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = SELECT_ALL + TABLE_CATEGORIA + " WHERE TipoCategoria = 'Premium Live Event'";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoriaBean cat = new CategoriaBean();

                cat.setTipo(rs.getString(TIPO_CATEGORIA_ATTRIBUTE));
                cat.setNome(rs.getString(NOME_CATEGORIA_ATTRIBUTE));
                cat.setImg(rs.getString(NOME_IMG_ATTRIBUTE));

                ple.add(cat);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            // chiusura PreparedStatement e Connection
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, MSG_ERROR_PS, e);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, MSG_ERROR_CONN, e);
            }
        }
        return ple;
    }

    // Metodi per la gestione dell'admin

    public synchronized void doSaveCategory (CategoriaBean categoria) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " + TABLE_CATEGORIA + " (TipoCategoria, NomeCategoria, NomeImgCategoria) " +
                "VALUES (?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, categoria.getTipo());
            ps.setString(2, categoria.getNome());
            ps.setString(3, categoria.getImg());

            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            // chiusura PreparedStatement e Connection
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, MSG_ERROR_PS, e);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, MSG_ERROR_CONN, e);
            }
        }
    }

}
