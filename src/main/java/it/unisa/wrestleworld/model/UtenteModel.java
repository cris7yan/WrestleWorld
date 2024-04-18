package it.unisa.wrestleworld.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtenteModel implements UtenteDAO {
    private static final String TABLE_UTENTE = "Utente";
    private static DataSource dataSource;
    private static Logger logger = Logger.getLogger(UtenteModel.class.getName());
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
     * funzione che permette di salvare un nuovo utente nel database
     * utilizzata per la fase di registrazione
     * @param utente
     * @throws SQLException
     */
    @Override
    public void doSave(UtenteBean utente) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " + TABLE_UTENTE + " (Email, Nome, Cognome, Password, DataNascita, Tipo) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, utente.getEmail());
            ps.setString(2, utente.getNome());
            ps.setString(3, utente.getCognome());
            ps.setString(4, utente.getPassword());
            ps.setString(5, utente.getDataNascita());
            ps.setString(6, utente.getTipoUtente());

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
     * funzione che si occupa di ricercare un utente all'interno del database mediante l'email e la password
     * funzione utilizzata per effettuare il login
     * @param email
     * @param password
     * @return l'utente corrispondente all'email e password passati come parametro
     * @throws SQLException
     */
    @Override
    public UtenteBean doRetrieveByEmailPassword(String email, String password) throws SQLException {
        UtenteBean utente = new UtenteBean();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT * FROM " + TABLE_UTENTE + " WHERE Email = ? AND Password = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                utente.setEmail(rs.getString("Email"));
                utente.setPassword(rs.getString("Password"));
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));
                utente.setDataNascita(rs.getString("DataNascita"));
                utente.setTipoUtente(rs.getString("Tipo"));
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
        if(utente.getEmail() == null) {
            return null;
        } else {
            return utente;
        }
    }

    /**
     * funzione che verifica se un email è già presente nel database
     * @param email
     * @return
     * @throws SQLException
     */
    @Override
    public boolean verificaEmailEsistente(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean ris = false;    // memorizziamo il risultato della ricerca

        String query = "SELECT * FROM " + TABLE_UTENTE + " WHERE Email = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                ris = true;
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
        return ris;
    }
}
