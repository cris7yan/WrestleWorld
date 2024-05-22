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

public class MetodoPagamentoModel implements MetodoPagamentoDAO {
    private static DataSource dataSource;
    private static Logger logger = Logger.getLogger(MetodoPagamentoModel.class.getName());
    private static final String TABLE_METODO_PAGAMENTO = "MetodoPagamento";

    private static final String ID_METODO_PAGAMENTO_PARAM = "ID_Pagamento";
    private static final String NUMERO_CARTA_PARAM = "NumeroCarta";
    private static final String INTESTATARIO_PARAM = "Intestatario";
    private static final String DATA_SCADENZA_PARAM = "DataScadenza";

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
     * funzione che gestisce la memorizzazione di un nuovo metodo di pagamento da parte di un utente
     * @param metodoPagamento
     * @param utente
     * @throws SQLException
     */
    public synchronized void doSave(MetodoPagamentoBean metodoPagamento, UtenteBean utente) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " + TABLE_METODO_PAGAMENTO + " (NumeroCarta, Intestatario, DataScadenza, EmailUtente) " +
                "VALUES (?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            UtenteModel utModel = new UtenteModel();    // preleviamo l'email dall'istanza dell'utente passato come parametro
            metodoPagamento.setUtente(utModel.doRetrieveByEmail(utente.getEmail()));    // memorizziamo l'email dell'utente nel metodo di pagamento

            ps.setString(1, metodoPagamento.getNumeroCarta());
            ps.setString(2, metodoPagamento.getIntestatario());
            ps.setDate(3, metodoPagamento.getDataScadenza());
            ps.setString(4, metodoPagamento.getUtente().getEmail());
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
     * funzione che restituisce l'elenco di tutti i metodi di pagamento di un determinato utente
     * @param email
     * @return
     * @throws SQLException
     */
    public synchronized List<MetodoPagamentoBean> doRetrieveAllByUtente (String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        List<MetodoPagamentoBean> metodiPagamento = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_METODO_PAGAMENTO + " WHERE EmailUtente = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MetodoPagamentoBean metodoPagamento = new MetodoPagamentoBean();

                metodoPagamento.setIdMetodoPagamento(rs.getInt(ID_METODO_PAGAMENTO_PARAM));
                metodoPagamento.setNumeroCarta(rs.getString(NUMERO_CARTA_PARAM));
                metodoPagamento.setIntestatario(rs.getString(INTESTATARIO_PARAM));
                metodoPagamento.setDataScadenza(rs.getDate(DATA_SCADENZA_PARAM));

                metodiPagamento.add(metodoPagamento);
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
        return metodiPagamento;
    }


    /**
     * funzione che restituisce un metodo di pagamento in base all'ID passato come parametro
     * @param id
     * @return
     * @throws SQLException
     */
    public synchronized MetodoPagamentoBean doRetrieveByID (int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        MetodoPagamentoBean metodoPagamento = new MetodoPagamentoBean();

        String query = "SELECT * FROM " + TABLE_METODO_PAGAMENTO + " WHERE ID_Pagamento = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UtenteModel utModel = new UtenteModel();

                metodoPagamento.setIdMetodoPagamento(rs.getInt(ID_METODO_PAGAMENTO_PARAM));
                metodoPagamento.setNumeroCarta(rs.getString(NUMERO_CARTA_PARAM));
                metodoPagamento.setIntestatario(rs.getString(INTESTATARIO_PARAM));
                metodoPagamento.setDataScadenza(rs.getDate(DATA_SCADENZA_PARAM));
                metodoPagamento.setUtente(utModel.doRetrieveByEmail(rs.getString("EmailUtente")));
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
        return metodoPagamento;
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

        List<Integer> idMetodiPagamento = new ArrayList<>();

        String query = "SELECT ID_Pagamento FROM " + TABLE_METODO_PAGAMENTO + " WHERE EmailUtente = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(ID_METODO_PAGAMENTO_PARAM);
                idMetodiPagamento.add(id);
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
        return idMetodiPagamento;
    }


    /**
     * funzione che gestisce la cancellazione di un metodo di pagamento
     * @param id ID_Pagamento del metodo di pagamento
     * @throws SQLException
     */
    public synchronized void doDelete (int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "DELETE FROM " + TABLE_METODO_PAGAMENTO + " WHERE ID_Pagamento = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ps.executeUpdate();
            conn.commit();
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
