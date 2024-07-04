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

public class IndirizzoModel implements IndirizzoDAO {
    private static DataSource dataSource;
    private static Logger logger = Logger.getLogger(IndirizzoModel.class.getName());
    private static final String TABLE_INDIRIZZO = "Indirizzo";

    private static final String ID_INDIRIZZO_PARAM = "ID_Indirizzo";

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
     * funzione che gestisce la memorizzazione di un nuovo indirizzo da parte di un utente
     * @param indirizzo indirizzo che viene memorizzato
     * @param utente utente che salva un nuovo indirizzo
     * @throws SQLException
     */
    public synchronized void doSave (IndirizzoBean indirizzo, UtenteBean utente) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " + TABLE_INDIRIZZO + " (Via, Citta, Provincia, CAP, NomeCompleto, EmailUtente) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            UtenteModel utModel = new UtenteModel();           // preleviamo l'email dall'istanza dell'utente passato come parametro
            indirizzo.setUtenteIndirizzo(utModel.doRetrieveByEmail(utente.getEmail()));     // memorizziamo l'email dell'utente nell'indirizzo

            ps.setString(1, indirizzo.getViaIndirizzo());
            ps.setString(2, indirizzo.getCittaIndirizzo());
            ps.setString(3, indirizzo.getProvinciaIndirizzo());
            ps.setString(4, indirizzo.getCAPIndirizzo());
            ps.setString(5, indirizzo.getNomeCompletoIndirizzo());
            ps.setString(6, indirizzo.getUtenteIndirizzo().getEmail());
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
     * funzione che restituisce un indirizzo in base all'ID passato come parametro
     * @param id
     * @return
     * @throws SQLException
     */
    public synchronized IndirizzoBean doRetrieveByID (int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        IndirizzoBean indirizzo = new IndirizzoBean();

        String query = "SELECT * FROM " + TABLE_INDIRIZZO + " WHERE ID_Indirizzo = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UtenteModel model = new UtenteModel();

                indirizzo.setIdIndirizzo(rs.getInt(ID_INDIRIZZO_PARAM));
                indirizzo.setViaIndirizzo(rs.getString("Via"));
                indirizzo.setCittaIndirizzo(rs.getString("Citta"));
                indirizzo.setProvinciaIndirizzo(rs.getString("Provincia"));
                indirizzo.setCAPIndirizzo(rs.getString("CAP"));
                indirizzo.setNomeCompletoIndirizzo(rs.getString("NomeCompleto"));
                indirizzo.setUtenteIndirizzo(model.doRetrieveByEmail(rs.getString("EmailUtente")));
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
        return indirizzo;
    }


    /**
     * funzione che restituisce l'elenco di tutti gli indirizzi di un determinato utente
     * @param email
     * @return indirizzi
     * @throws SQLException
     */
    public synchronized List<IndirizzoBean> doRetrieveAllByUtente (String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        List<IndirizzoBean> indirizzi = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_INDIRIZZO + " WHERE EmailUtente = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                IndirizzoBean indirizzo = new IndirizzoBean();

                indirizzo.setIdIndirizzo(rs.getInt(ID_INDIRIZZO_PARAM));
                indirizzo.setViaIndirizzo(rs.getString("Via"));
                indirizzo.setCittaIndirizzo(rs.getString("Citta"));
                indirizzo.setProvinciaIndirizzo(rs.getString("Provincia"));
                indirizzo.setCAPIndirizzo(rs.getString("CAP"));
                indirizzo.setNomeCompletoIndirizzo(rs.getString("NomeCompleto"));

                indirizzi.add(indirizzo);
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
        return indirizzi;
    }


    /**
     * funzione che restituisce la lista di tutti gli ID degli indirizzi di un determinato utente
     * @param email
     * @return
     * @throws SQLException
     */
    public synchronized List<Integer> doRetrieveAllID (String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        List<Integer> idIndirizzi = new ArrayList<>();

        String query = "SELECT ID_Indirizzo FROM " + TABLE_INDIRIZZO + " WHERE EmailUtente = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(ID_INDIRIZZO_PARAM);
                idIndirizzi.add(id);
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
        return idIndirizzi;
    }


    /**
     * funzione che gestisce la cancellazione di un indirizzo
     * @param id ID_Indirizzo dell'indirizzo da eliminare
     * @throws SQLException
     */
    public synchronized void doDelete (int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "DELETE FROM " + TABLE_INDIRIZZO + " WHERE ID_Indirizzo = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ps.executeUpdate();
            conn.commit();      // confermiamo le modifiche al database
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
