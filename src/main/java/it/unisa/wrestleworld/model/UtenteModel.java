package it.unisa.wrestleworld.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtenteModel implements UtenteDAO {
    private static DataSource dataSource;
    private static Logger logger = Logger.getLogger(UtenteModel.class.getName());
    private static final String TABLE_UTENTE = "Utente";
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
    public synchronized void doSave(UtenteBean utente) throws SQLException {
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
            ps.setDate(5, utente.getDataNascita());
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
    public synchronized UtenteBean doRetrieveByEmailPassword(String email, String password) throws SQLException {
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
                utente.setDataNascita(Date.valueOf(rs.getString("DataNascita")));
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
     * funzione che si occupa di ricercare un utente all'interno del database mediante l'email
     * @param email
     * @return l'utente corrispondente all'email passata come parametro
     * @throws SQLException
     */
    public synchronized UtenteBean doRetrieveByEmail (String email) throws SQLException {
        UtenteBean utente = new UtenteBean();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT * FROM " + TABLE_UTENTE + " WHERE Email = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                utente.setEmail(rs.getString("Email"));
                utente.setPassword(rs.getString("Password"));
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));
                utente.setDataNascita(Date.valueOf(rs.getString("DataNascita")));
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
    public synchronized boolean verificaEmailEsistente(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT * FROM " + TABLE_UTENTE + " WHERE Email = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if(rs.getString("Email").equalsIgnoreCase(email))
                    return true;
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
        return false;
    }


    /**
     * funzione che gestisce la modifica dei dati personali di un utente
     * @param utenteBean
     * @throws SQLException
     */
    public synchronized void doUpdateData (String nome, String cognome, Date data, String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "UPDATE " + TABLE_UTENTE + " SET Nome = ?, Cognome = ?, DataNascita = ? WHERE Email = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setDate(3, data);
            ps.setString(4, email);

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
     * funzione che gestisce la modifica dell'email di un utente
     * @param email email dell'utente da modificare
     * @param newEmail la nuova email che deve essere salvata
     * @throws SQLException
     */
    public synchronized void doUpdateEmail (String email, String newEmail) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "UPDATE " + TABLE_UTENTE + " SET Email = ? WHERE Email = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, newEmail);
            ps.setString(2, email);
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
     * funzione che gestisce la modifica della password di un utente
     * @param email email dell'utente da modificare
     * @param newPassword la nuova password che deve essere salvata
     * @throws SQLException
     */
    public synchronized void doUpdatePassword (String email, String newPassword) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "UPDATE " + TABLE_UTENTE + " SET Password = ? WHERE Email = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, newPassword);
            ps.setString(2, email);
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
