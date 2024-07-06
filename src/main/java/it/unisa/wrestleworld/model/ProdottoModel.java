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
    private static final String TABLE_COMPOSIZIONE_ORDINE = "ComposizioneOrdine";
    private static final String TABLE_IMMAGINE = "Immagine";
    private static final String TABLE_APPARTENENZA = "Appartenenza";

    private static final String IDPROD_PARAM = "ID_Prodotto";
    private static final String NOME_PARAM = "Nome";
    private static final String DESCRIZIONE_PARAM = "Descrizione";
    private static final String PREZZO_PARAM = "Prezzo";
    private static final String PREZZO_OFFERTA_PARAM = "Prezzo_Offerta";
    private static final String MATERIALE_PARAM = "Materiale";
    private static final String MARCA_PARAM = "Marca";
    private static final String MODELLO_PARAM = "Modello";
    private static final String DISPONIBILITA_PARAM = "Disponibilita";

    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String WHERE_IDPROD = " WHERE ID_Prodotto = ?";

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
     * funzione che restituisce tutti i prodotti nel database
     * @return prodotti
     * @throws SQLException
     */
    public synchronized List<ProdottoBean> doRetrieveAll() throws SQLException {
        List<ProdottoBean> prodotti = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = SELECT_ALL_FROM + TABLE_PRODOTTO;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdottoBean prod = new ProdottoBean();

                prod.setIDProdotto(rs.getInt(IDPROD_PARAM));
                prod.setNomeProdotto(rs.getString(NOME_PARAM));
                prod.setDescrizioneProdotto(rs.getString(DESCRIZIONE_PARAM));
                prod.setMaterialeProdotto(rs.getString(MATERIALE_PARAM));
                prod.setMarcaProdotto(rs.getString(MARCA_PARAM));
                prod.setModelloProdotto(rs.getString(MODELLO_PARAM));
                prod.setPrezzoProdotto(rs.getFloat(PREZZO_PARAM));
                prod.setPrezzoOffertaProdotto(rs.getFloat(PREZZO_OFFERTA_PARAM));
                prod.setDisponibilitaProdotto(rs.getBoolean(DISPONIBILITA_PARAM));

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


    /**
     * funzione che restituisce i prodotti best sellers
     * @return bestSellers
     * @throws SQLException
     */
    public synchronized List<ProdottoBean> doRetrieveBestSellers() throws SQLException {
        List<ProdottoBean> bestSellers = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT P.ID_Prodotto, P.Nome, P.Descrizione, P.Prezzo, P.Prezzo_Offerta, SUM(Co.Quantita) as Tot FROM " + TABLE_PRODOTTO
                + " P JOIN " + TABLE_COMPOSIZIONE_ORDINE + " Co ON P.ID_Prodotto = Co.ID_Prodotto "
                + "GROUP BY P.ID_Prodotto "
                + "ORDER BY Tot DESC LIMIT 3";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdottoBean prod = new ProdottoBean();

                prod.setIDProdotto(rs.getInt(IDPROD_PARAM));
                prod.setNomeProdotto(rs.getString(NOME_PARAM));
                prod.setDescrizioneProdotto(rs.getString(DESCRIZIONE_PARAM));
                prod.setPrezzoProdotto(rs.getFloat(PREZZO_PARAM));
                prod.setPrezzoOffertaProdotto(rs.getFloat(PREZZO_OFFERTA_PARAM));

                bestSellers.add(prod);
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
        return bestSellers;
    }


    /**
     * funzione che restituisce tutte le immagini di ogni prodotto
     * @param prod
     * @return imgProd
     * @throws SQLException
     */
    public synchronized List<String> doRetrieveAllImages(ProdottoBean prod) throws SQLException {
        List<String> imgProd = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT NomeImg FROM " + TABLE_IMMAGINE + WHERE_IDPROD;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, prod.getIDProdotto());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String img = rs.getString("NomeImg");
                imgProd.add(img);
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
        return imgProd;
    }


    /**
     * funzione che restituisce un prodotto in base al suo ID
     * @param id
     * @return
     * @throws SQLException
     */
    public synchronized ProdottoBean doRetrieveByID (int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        ProdottoBean prod = new ProdottoBean();

        String query = SELECT_ALL_FROM + TABLE_PRODOTTO + WHERE_IDPROD;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                prod.setIDProdotto(rs.getInt(IDPROD_PARAM));
                prod.setNomeProdotto(rs.getString(NOME_PARAM));
                prod.setDescrizioneProdotto(rs.getString(DESCRIZIONE_PARAM));
                prod.setPrezzoProdotto(rs.getFloat(PREZZO_PARAM));
                prod.setPrezzoOffertaProdotto(rs.getFloat(PREZZO_OFFERTA_PARAM));
                prod.setMarcaProdotto(rs.getString(MARCA_PARAM));
                prod.setModelloProdotto(rs.getString(MODELLO_PARAM));
                prod.setMaterialeProdotto(rs.getString(MATERIALE_PARAM));
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
        return prod;
    }


    /**
     * funzione che controlla se un prodotto è disponibile
     * @param id
     * @return
     * @throws SQLException
     */
    public synchronized boolean checkProductAvailability (int id) throws SQLException {
        boolean disp = false;
        Connection conn = null;
        PreparedStatement ps = null;
        String query = "SELECT Disponibilita FROM " + TABLE_PRODOTTO + WHERE_IDPROD;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                disp = rs.getBoolean(DISPONIBILITA_PARAM);
                if(disp) {
                    return disp;
                }
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
        return disp;
    }


    /**
     * funzione che restituisce un prodotto in base al nome, o a parte del nome
     * @param nome
     * @return
     * @throws SQLException
     */
    public synchronized ProdottoBean doRetrieveByName (String nome) throws SQLException {
        ProdottoBean prod = new ProdottoBean();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = SELECT_ALL_FROM + TABLE_PRODOTTO + " WHERE Nome LIKE ? AND Disponibilita = 1";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, nome);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                prod.setIDProdotto(rs.getInt(IDPROD_PARAM));
                prod.setNomeProdotto(rs.getString(NOME_PARAM));
                prod.setDescrizioneProdotto(rs.getString(DESCRIZIONE_PARAM));
                prod.setPrezzoProdotto(rs.getFloat(PREZZO_PARAM));
                prod.setPrezzoOffertaProdotto(rs.getFloat(PREZZO_OFFERTA_PARAM));
                prod.setMarcaProdotto(rs.getString(MARCA_PARAM));
                prod.setModelloProdotto(rs.getString(MODELLO_PARAM));
                prod.setMaterialeProdotto(rs.getString(MATERIALE_PARAM));
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
        return prod;
    }


    /**
     * funzione che restituisce un insieme di nomi di prodotti in base al suggerimento per la ricerca
     * @param suggest suggerimento per la ricerca
     * @return
     * @throws SQLException
     */
    public synchronized List<String> doRetrieveBySuggest (String suggest) throws SQLException {
        List<String> suggerimenti = new ArrayList<>();
        suggest = "%"+suggest+"%";

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT Nome FROM " + TABLE_PRODOTTO + " WHERE Nome LIKE ? AND Disponibilita = 1";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, suggest);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                suggerimenti.add(rs.getString(NOME_PARAM));
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
        return suggerimenti;
    }


    /**
     * funzione che restituisce tutti i prodotti che appartengono ad una determinata categoria
     * @param category
     * @return
     * @throws SQLException
     */
    public synchronized List<ProdottoBean> doRetrieveByCategory (String category) throws SQLException {
        List<ProdottoBean> categoryProd = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = " SELECT p.* FROM " + TABLE_PRODOTTO + " p JOIN " + TABLE_APPARTENENZA + " a ON p.ID_Prodotto = a.ID_Prodotto "
                + "JOIN Categoria c ON a.NomeCategoria = c.NomeCategoria" + " WHERE c.NomeCategoria = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, category);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdottoBean prod = new ProdottoBean();
                prod.setIDProdotto(rs.getInt(IDPROD_PARAM));
                prod.setNomeProdotto(rs.getString(NOME_PARAM));
                prod.setDescrizioneProdotto(rs.getString(DESCRIZIONE_PARAM));
                prod.setPrezzoProdotto(rs.getFloat(PREZZO_PARAM));
                prod.setPrezzoOffertaProdotto(rs.getFloat(PREZZO_OFFERTA_PARAM));
                prod.setMarcaProdotto(rs.getString(MARCA_PARAM));
                prod.setModelloProdotto(rs.getString(MODELLO_PARAM));
                prod.setMaterialeProdotto(rs.getString(MATERIALE_PARAM));
                categoryProd.add(prod);
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
        return categoryProd;
    }


    /**
     * funzione che restituisce i migliori prodotti in offerta
     * @return
     * @throws SQLException
     */
    public synchronized List<ProdottoBean> doRetrieveBestOnOffer() throws SQLException {
        List<ProdottoBean> bestOnOffer = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT ID_Prodotto, Nome, Descrizione, Prezzo, " +
                "CASE WHEN Prezzo_Offerta > 0 AND Prezzo_Offerta < Prezzo THEN Prezzo_Offerta ELSE Prezzo END AS PrezzoEffettivo, Disponibilita " +
                "FROM " + TABLE_PRODOTTO + " WHERE Prezzo_Offerta > 0 AND Prezzo_Offerta < Prezzo " +
                "ORDER BY Prezzo - Prezzo_Offerta DESC LIMIT 10";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdottoBean prod = new ProdottoBean();

                prod.setIDProdotto(rs.getInt("ID_Prodotto"));
                prod.setNomeProdotto(rs.getString("Nome"));
                prod.setDescrizioneProdotto(rs.getString("Descrizione"));
                prod.setPrezzoProdotto(rs.getFloat("Prezzo")); // Prezzo originale
                prod.setPrezzoOffertaProdotto(rs.getFloat("PrezzoEffettivo")); // Prezzo scontato = Prezzo - Prezzo_Offerta

                bestOnOffer.add(prod);
            }
            for (ProdottoBean prodotto : bestOnOffer) {
                System.out.println("Prodotto: " + prodotto.getNomeProdotto());
                System.out.println("Prezzo Originale: " + prodotto.getPrezzoProdotto());
                System.out.println("Prezzo Offerta: " + prodotto.getPrezzoOffertaProdotto());
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
        return bestOnOffer;
    }

}
