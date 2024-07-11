package it.unisa.wrestleworld.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineModel implements OrdineDAO {
    private static DataSource dataSource;
    private static Logger logger = Logger.getLogger(OrdineModel.class.getName());

    private static final String TABLE_ORDINE = "Ordine";
    private static final String TABLE_COMPOSIZIONE_ORDINE = "ComposizioneOrdine";

    private static final String IDORDINE_PARAM = "ID_Ordine";

    static final ProdottoDAO prodModel = new ProdottoModel();

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
     * funzione che salva un nuovo ordine nel database
     * @param ordine
     * @throws SQLException
     */
    public synchronized void doSave (Date data, float prezzoTotale, String emailUtente) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " + TABLE_ORDINE + " (Data_ordine, Totale, EmailUtente)" + " VALUES (?,?,?)";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setDate(1, data);
            ps.setFloat(2, prezzoTotale);
            ps.setString(3, emailUtente);

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


    /**
     * funzione che si occupa di andare ad aggiornare il database con i dati dei prodotti che comprendono un determinato prodotto
     * @param IdOrdine
     * @param IdProdotto
     * @param quantita
     * @param prezzoUnitario
     * @throws SQLException
     */
    public synchronized void doUpdateComprendeOrdine (int IdOrdine, int IdProdotto, int quantita, float prezzoUnitario) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " + TABLE_COMPOSIZIONE_ORDINE + " (ID_Ordine, ID_Prodotto, Quantita, Prezzo)" + " VALUES (?,?,?,?)";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, IdOrdine);
            ps.setInt(2, IdProdotto);
            ps.setInt(3, quantita);
            ps.setFloat(4, prezzoUnitario);

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

                ordine.setIdOrdine(rs.getInt(IDORDINE_PARAM));
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


    /**
     * funzione che ritorna la lista di prodotti di un determinato ordine
     * @param id
     * @return
     * @throws SQLException
     */
    public synchronized List<ProdottoBean> doRetrieveOrdineByID (int id) throws SQLException {
        List<ProdottoBean> listaProdottiOrdine = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT ID_Ordine FROM " + TABLE_COMPOSIZIONE_ORDINE + " WHERE ID_Ordine = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                listaProdottiOrdine.add(prodModel.doRetrieveByID(rs.getInt(IDORDINE_PARAM)));
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
        return listaProdottiOrdine;
    }


    /**
     * funzione che recupera l'id dell'ultimo ordine effettuato
     * @return
     * @throws SQLException
     */
    public synchronized int doRetrieveLastOrdineID () throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        int idLastOrdine = 0;
        String query = "SELECT MAX(ID_Ordine) AS Max FROM " + TABLE_ORDINE;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idLastOrdine = rs.getInt("Max");
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
        return idLastOrdine;
    }

}
