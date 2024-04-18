package it.unisa.wrestleworld.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class UtenteModel implements UtenteDAO {

    private static DataSource ds;
    private static final String TABLE_NAME_UTENTE = "Utente";

    static {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) (ctx).lookup("java:comp/env");

            ds = (DataSource) envCtx.lookup("jdbc/wrestleworld");
        } catch (NamingException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    /**
     * Metodo per salvare un nuovo utente all'interno del database
     * @param utente
     */
    public synchronized void doSave (UtenteBean utente) throws SQLException {

        System.out.println("Eseguo la save");
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " + TABLE_NAME_UTENTE + " (Email, Nome, Cognome, Password, DataNascita, Tipo) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, utente.getEmail());
            ps.setString(2, utente.getNome());
            ps.setString(3, utente.getCognome());
            ps.setString(4, utente.getPassword());
            ps.setString(5, utente.getDataNascita());
            ps.setString(6, utente.getTipoUtente());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if(ps != null) {
                    ps.close();
                }
            } finally {
                if(conn != null) {
                    conn.close();
                }
            }
        }
    }

    /**
     * funzione che cancella i dati dell'utente dal database
     * @param emailUtente
     */
    public synchronized boolean doDelete (String emailUtente) throws SQLException {

        System.out.println("Effettuo la delete");
        Connection conn = null;
        PreparedStatement ps = null;

        int result = 0;

        String query = "DELETE FROM " + TABLE_NAME_UTENTE + " WHERE Email = ?";

        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, emailUtente);
            System.out.println(query + emailUtente);

            result = ps.executeUpdate();

            // conferma le modifiche effettuate nel database
            conn.commit();
        } finally {
            try {
                if(ps != null) {
                    ps.close();
                }
            } finally {
                if(conn != null) {
                    conn.close();
                }
            }
        }

        return (result != 0);
    }

    /**
     * funzione che restituisce un  utente recuperato dal database tramite l'email
     * @param email
      */
    public synchronized UtenteBean doRetriveByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        UtenteBean utente = new UtenteBean();

        String query = "SELECT * FROM " + TABLE_NAME_UTENTE + " WHERE Email = ?";

        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // vengono estratti i valori dal db ed inseriti nel bean utente
                utente.setEmail(rs.getString("Email"));
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));
                utente.setPassword(rs.getString("Password"));
                utente.setDataNascita(rs.getString("DataNascita"));
                utente.setTipoUtente(rs.getString("Tipo"));
            }

        } finally {
            try {
                if(ps != null) {
                    ps.close();
                }
            } finally {
                if(conn != null) {
                    conn.close();
                }
            }
        }
        return utente;
    }

    /**
     * funzione che restituisce tutti gli utenti contenuti nel database mediante un ordine
     * @param order (ASC o DESC)
     */
    public synchronized Collection<UtenteBean> doRetriveAllUtentiByOrder (String order) throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;

        Collection<UtenteBean> utenti = new LinkedList<UtenteBean>();

        String query = "SELECT * FROM " + TABLE_NAME_UTENTE;

        if(order != null && !order.equals("")) {
            query += " ORDER BY " + order;
        }

        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UtenteBean utente = new UtenteBean();

                // vengono estratti i valori dal db ed inseriti nel bean utente
                utente.setEmail(rs.getString("Email"));
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));
                utente.setPassword(rs.getString("Password"));
                utente.setDataNascita(rs.getString("DataNascita"));
                utente.setTipoUtente(rs.getString("Tipo"));

                // viene aggiunto l'utente alla lista
                utenti.add(utente);
            }
        } finally {
            try {
                if(ps != null) {
                    ps.close();
                }
            } finally {
                if(conn != null) {
                    conn.close();
                }
            }
        }
        return utenti;
    }

    // funzione che restituisce tutti gli utenti contenuti nel database
    public synchronized Collection<UtenteBean> doRetriveAllUtenti() throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;

        Collection<UtenteBean> utenti = new LinkedList<UtenteBean>();

        String query = "SELECT * FROM " + TABLE_NAME_UTENTE;

        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UtenteBean utente = new UtenteBean();

                // vengono estratti i valori dal db ed inseriti nel bean utente
                utente.setEmail(rs.getString("Email"));
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));
                utente.setPassword(rs.getString("Password"));
                utente.setDataNascita(rs.getString("DataNascita"));
                utente.setTipoUtente(rs.getString("Tipo"));

                // viene aggiunto l'utente alla lista
                utenti.add(utente);
            }

        } finally {
            try {
                if(ps != null) {
                    ps.close();
                }
            } finally {
                if(conn != null) {
                    conn.close();
                }
            }
        }
        return utenti;
    }

    /**
     * funzione che effettua la modifica dei dati di un utente nel database, modificandone la email
     * @param email -> mail attuale dell'utente
     * @param  newEmail -> nuova mail che dovrà essere inserita
     */
    public synchronized void doUpdateEmail(String email, String newEmail) throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "UPDATE " + TABLE_NAME_UTENTE + " SET Email = ? WHERE Email = ?";

        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, newEmail);
            ps.setString(2, email);

            ps.executeUpdate();

            // conferma le modifiche effettuate nel database
            conn.commit();
        } finally {
            try {
                if(ps != null) {
                    ps.close();
                }
            } finally {
                if(conn != null) {
                    conn.close();
                }
            }
        }
    }

    /**
     * funzione che effettua la modifica dei dati di un utente nel database, modificandone la email
     * @param email -> mail attuale dell'utente
     * @param newPassword -> nuova password che dovrà essere inserita
     */
    public void doUpdatePassword(String email, String newPassword) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "UPDATE " + TABLE_NAME_UTENTE + " SET Password = ? WHERE Email = ?";

        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, newPassword);
            ps.setString(2, email);

            ps.executeUpdate();

            // conferma le modifiche effettuate nel database
            conn.commit();

        } finally {
            try {
                if(ps != null) {
                    ps.close();
                }
            } finally {
                if(conn != null) {
                    conn.close();
                }
            }
        }
    }

}
