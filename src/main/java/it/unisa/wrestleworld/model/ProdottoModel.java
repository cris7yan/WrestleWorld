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

public class ProdottoModel implements ProdottoDAO {
    private static Logger logger = Logger.getLogger(ProdottoModel.class.getName());
    private static DataSource dataSource;

    private static final String TABLE_PRODOTTO = "Prodotto";
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

    public synchronized List<ProdottoBean> doRetrieveAll() throws SQLException {
        List<ProdottoBean> prodotti = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT * FROM " + TABLE_PRODOTTO + " WHERE ID_Prodotto BETWEEN 1 AND 10";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdottoBean prod = new ProdottoBean();

                prod.setIDProdotto(rs.getInt("ID_Prodotto"));
                prod.setNomeProdotto(rs.getString("Nome"));
                prod.setDescrizioneProdotto(rs.getString("Descrizione"));
                prod.setMaterialeProdotto(rs.getString("Materiale"));
                prod.setMarcaProdotto(rs.getString("Marca"));
                prod.setModelloProdotto(rs.getString("Modello"));
                prod.setPrezzoProdotto(rs.getFloat("Prezzo"));
                prod.setDisponibilitaProdotto(rs.getBoolean("Disponibilita"));

                prodotti.add(prod);
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
        return prodotti;
    }
}
