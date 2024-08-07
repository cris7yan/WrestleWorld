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
    private static final String TABLE_PRODOTTO = "Prodotto";

    private static final String IDORDINE_PARAM = "ID_Ordine";
    private static final String DATA_ORDINE_PARAM = "Data_ordine";
    private static final String TOTALE_ORDINE_PARAM = "Totale";

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
    public synchronized void doUpdateComprendeOrdine (int idOrdine, int idProdotto, String taglia, int quantita, float prezzoUnitario) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO ComposizioneOrdine (ID_Ordine, ID_Prodotto, Taglia, Quantita, Prezzo) VALUES (?,?,?,?,?)";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, idOrdine);
            ps.setInt(2, idProdotto);
            ps.setString(3, taglia);
            ps.setInt(4, quantita);
            ps.setFloat(5, prezzoUnitario);

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
     * funzione che restituisce un determinato ordine
     * @param id
     * @return
     * @throws SQLException
     */
    public synchronized OrdineBean doRetrieveOrdineByOrderId(int idOrdine) throws SQLException {
        OrdineBean ordine = null;

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT o.*, u.Email, u.Nome, u.Cognome FROM " + TABLE_ORDINE + " o " +
                "JOIN Utente u ON o.EmailUtente = u.Email " +
                "WHERE o.ID_Ordine = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, idOrdine);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ordine = new OrdineBean();
                ordine.setIdOrdine(rs.getInt(IDORDINE_PARAM));
                ordine.setDataOrdine(rs.getDate(DATA_ORDINE_PARAM));
                ordine.setPrezzoTotaleOrdine(rs.getFloat(TOTALE_ORDINE_PARAM));

                UtenteBean utente = new UtenteBean();
                utente.setEmail(rs.getString("Email"));
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));

                ordine.setUtenteOrdine(utente);
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
        return ordine;
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
                ordine.setDataOrdine(rs.getDate(DATA_ORDINE_PARAM));
                ordine.setPrezzoTotaleOrdine(rs.getFloat(TOTALE_ORDINE_PARAM));

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
    public synchronized List<ProdottoBean> doRetrieveOrdineByID(int id) throws SQLException {
        List<ProdottoBean> listaProdottiOrdine = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT p.*, c.Prezzo AS PrezzoOrdine, c.Quantita, c.Taglia " +
                "FROM " + TABLE_COMPOSIZIONE_ORDINE + " c " +
                "JOIN " + TABLE_PRODOTTO + " p ON c.ID_Prodotto = p.ID_Prodotto " +
                "WHERE c.ID_Ordine = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdottoBean prodotto = new ProdottoBean();

                prodotto.setIDProdotto(rs.getInt("ID_Prodotto"));
                prodotto.setNomeProdotto(rs.getString("Nome"));
                prodotto.setDescrizioneProdotto(rs.getString("Descrizione"));
                prodotto.setMaterialeProdotto(rs.getString("Materiale"));
                prodotto.setMarcaProdotto(rs.getString("Marca"));
                prodotto.setModelloProdotto(rs.getString("Modello"));
                prodotto.setPrezzoProdotto(rs.getFloat("PrezzoOrdine"));
                prodotto.setQuantitaCarrello(rs.getInt("Quantita"));
                prodotto.setTagliaSelezionata(rs.getString("Taglia"));

                listaProdottiOrdine.add(prodotto);
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

    // Metodi per la gestione dell'admin

    /**
     * funzione che recupera tutti gli ordini effettuati
     * @return
     * @throws SQLException
     */
    public synchronized List<OrdineBean> doRetrieveAllOrdini() throws SQLException {
        List<OrdineBean> ordini = new ArrayList<>();
        String query = "SELECT o.ID_Ordine, o.Data_ordine, o.Totale, u.Email, u.Nome, u.Cognome " +
                "FROM " + TABLE_ORDINE + " o " +
                "JOIN Utente u ON o.EmailUtente = u.Email";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(rs.getInt(IDORDINE_PARAM));
                ordine.setDataOrdine(rs.getDate(DATA_ORDINE_PARAM));
                ordine.setPrezzoTotaleOrdine(rs.getFloat(TOTALE_ORDINE_PARAM));

                UtenteBean utente = new UtenteBean();
                utente.setEmail(rs.getString("Email"));
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));
                ordine.setUtenteOrdine(utente);

                ordini.add(ordine);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }

        return ordini;
    }

}
