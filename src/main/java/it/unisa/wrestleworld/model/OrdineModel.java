package it.unisa.wrestleworld.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineModel implements OrdineDAO {
    private static DataSource dataSource;
    private static Logger logger = Logger.getLogger(OrdineModel.class.getName());
    private static final String TABLE_ORDINE = "Ordine";
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
     * funzione che recupera tutti gli ordini di un utente
     * @param email
     * @return
     * @throws SQLException
     */
    public synchronized List<OrdineBean> doRetrieveAllByEmail (String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        List<OrdineBean> ordini = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_ORDINE + " WHERE EmailUtente = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();

                ordine.setIdOrdine(rs.getInt("ID_Ordine"));
                ordine.setDataOrdine(rs.getDate("Data_ordine"));
                ordine.setPrezzoTotaleOrdine(rs.getFloat("Totale"));

                ordini.add(ordine);
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
        return ordini;
    }
}
