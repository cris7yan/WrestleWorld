package it.unisa.wrestleworld.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProdottoModel implements ProdottoDAO {
    private static Logger logger = Logger.getLogger(ProdottoModel.class.getName());
    private static DataSource dataSource;

    private static final String TABLE_PRODOTTO = "Prodotto";
    private static final String TABLE_COMPOSIZIONE_ORDINE = "ComposizioneOrdine";
    private static final String TABLE_IMMAGINE = "Immagine";
    private static final String TABLE_APPARTENENZA = "Appartenenza";
    private static final String TABLE_TAGLIAPRODOTTO = "TagliaProdotto";

    private static final String IDPROD_PARAM = "ID_Prodotto";
    private static final String NOME_PARAM = "Nome";
    private static final String DESCRIZIONE_PARAM = "Descrizione";
    private static final String PREZZO_PARAM = "Prezzo";
    private static final String PREZZO_OFFERTA_PARAM = "Prezzo_Offerta";
    private static final String MATERIALE_PARAM = "Materiale";
    private static final String MARCA_PARAM = "Marca";
    private static final String MODELLO_PARAM = "Modello";
    private static final String DISPONIBILITA_PARAM = "Disponibilita";
    private static final String ID_TAGLIAPROD_PARAM = "ID_TagliaProdotto";
    private static final String TAGLIA_PARAM = "Taglia";
    private static final String QUANTITA_PARAM = "Quantita";

    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String WHERE_IDPROD = " WHERE ID_Prodotto = ?";
    private static final String UPDATE = "UPDATE ";
    private static final String INSERT_INTO = "INSERT INTO ";

    private static final String MSG_ERROR_PS = "Errore durante la chiusura del PreparedStatement";
    private static final String MSG_ERROR_CONN = "Errore durante la chiusura della connessione";
    private static final String MSG_ERROR_RS = " Errore durante la chiusura della ResultSet";


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

        String queryProdotto = SELECT_ALL_FROM + TABLE_PRODOTTO;
        String queryTaglia = SELECT_ALL_FROM + TABLE_TAGLIAPRODOTTO + WHERE_IDPROD;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psProd = conn.prepareStatement(queryProdotto);
             PreparedStatement psTaglia = conn.prepareStatement(queryTaglia)) {

            ResultSet rs = psProd.executeQuery();
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

                psTaglia.setInt(1, prod.getIDProdotto());

                try (ResultSet rsTaglia = psTaglia.executeQuery()) {
                    List<TagliaProdottoBean> taglieProdotto = new ArrayList<>();
                    while (rsTaglia.next()) {
                        TagliaProdottoBean taglia = new TagliaProdottoBean();

                        taglia.setIdProdotto(rsTaglia.getInt(ID_TAGLIAPROD_PARAM));
                        taglia.setIdProdotto(rsTaglia.getInt(IDPROD_PARAM));
                        taglia.setTaglia(rsTaglia.getString(TAGLIA_PARAM));
                        taglia.setQuantita(rsTaglia.getInt(QUANTITA_PARAM));

                        taglieProdotto.add(taglia);
                    }
                    prod.setTaglieProdotto(taglieProdotto);
                }

                prodotti.add(prod);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
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

        String query = "SELECT P.ID_Prodotto, P.Nome, P.Descrizione, P.Prezzo, P.Prezzo_Offerta, P.Disponibilita, SUM(Co.Quantita) as Tot FROM "
                + TABLE_PRODOTTO + " P JOIN " + TABLE_COMPOSIZIONE_ORDINE + " Co ON P.ID_Prodotto = Co.ID_Prodotto "
                + "GROUP BY P.ID_Prodotto "
                + "ORDER BY Tot DESC LIMIT 3";
        String queryTaglia = SELECT_ALL_FROM + TABLE_TAGLIAPRODOTTO + WHERE_IDPROD;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psProd = conn.prepareStatement(query);
             PreparedStatement psTaglia = conn.prepareStatement(queryTaglia)) {

            ResultSet rs = psProd.executeQuery();
            while (rs.next()) {
                ProdottoBean prod = new ProdottoBean();

                prod.setIDProdotto(rs.getInt(IDPROD_PARAM));
                prod.setNomeProdotto(rs.getString(NOME_PARAM));
                prod.setDescrizioneProdotto(rs.getString(DESCRIZIONE_PARAM));
                prod.setPrezzoProdotto(rs.getFloat(PREZZO_PARAM));
                prod.setPrezzoOffertaProdotto(rs.getFloat(PREZZO_OFFERTA_PARAM));
                prod.setDisponibilitaProdotto(rs.getBoolean(DISPONIBILITA_PARAM));

                psTaglia.setInt(1, prod.getIDProdotto());

                try (ResultSet rsTaglia = psTaglia.executeQuery()) {
                    List<TagliaProdottoBean> taglieProdotto = new ArrayList<>();
                    while (rsTaglia.next()) {
                        TagliaProdottoBean taglia = new TagliaProdottoBean();

                        taglia.setIdProdotto(rsTaglia.getInt(ID_TAGLIAPROD_PARAM));
                        taglia.setIdProdotto(rsTaglia.getInt(IDPROD_PARAM));
                        taglia.setTaglia(rsTaglia.getString(TAGLIA_PARAM));
                        taglia.setQuantita(rsTaglia.getInt(QUANTITA_PARAM));

                        taglieProdotto.add(taglia);
                    }
                    prod.setTaglieProdotto(taglieProdotto);
                }

                bestSellers.add(prod);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
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
    public synchronized ProdottoBean doRetrieveByID(int id) throws SQLException {
        ProdottoBean prod = new ProdottoBean();

        String query = SELECT_ALL_FROM + TABLE_PRODOTTO + WHERE_IDPROD;
        String queryTaglia = SELECT_ALL_FROM + TABLE_TAGLIAPRODOTTO + WHERE_IDPROD;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psProd = conn.prepareStatement(query);
             PreparedStatement psTaglia = conn.prepareStatement(queryTaglia)) {

            psProd.setInt(1, id);
            ResultSet rs = psProd.executeQuery();

            while (rs.next()) {
                prod.setIDProdotto(rs.getInt(IDPROD_PARAM));
                prod.setNomeProdotto(rs.getString(NOME_PARAM));
                prod.setDescrizioneProdotto(rs.getString(DESCRIZIONE_PARAM));
                prod.setPrezzoProdotto(rs.getFloat(PREZZO_PARAM));
                prod.setPrezzoOffertaProdotto(rs.getFloat(PREZZO_OFFERTA_PARAM));
                prod.setMarcaProdotto(rs.getString(MARCA_PARAM));
                prod.setModelloProdotto(rs.getString(MODELLO_PARAM));
                prod.setMaterialeProdotto(rs.getString(MATERIALE_PARAM));
                prod.setDisponibilitaProdotto(rs.getBoolean(DISPONIBILITA_PARAM));

                psTaglia.setInt(1, prod.getIDProdotto());

                try (ResultSet rsTaglia = psTaglia.executeQuery()) {
                    List<TagliaProdottoBean> taglieProdotto = new ArrayList<>();
                    while (rsTaglia.next()) {
                        TagliaProdottoBean taglia = new TagliaProdottoBean();

                        taglia.setIdProdotto(rsTaglia.getInt(ID_TAGLIAPROD_PARAM));
                        taglia.setIdProdotto(rsTaglia.getInt(IDPROD_PARAM));
                        taglia.setTaglia(rsTaglia.getString(TAGLIA_PARAM));
                        taglia.setQuantita(rsTaglia.getInt(QUANTITA_PARAM));

                        taglieProdotto.add(taglia);
                    }
                    prod.setTaglieProdotto(taglieProdotto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
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
    public synchronized ProdottoBean doRetrieveByName(String nome) throws SQLException {
        ProdottoBean prod = new ProdottoBean();

        String query = SELECT_ALL_FROM + TABLE_PRODOTTO + " WHERE Nome LIKE ? AND Disponibilita = 1";
        String queryTaglia = SELECT_ALL_FROM + TABLE_TAGLIAPRODOTTO + WHERE_IDPROD;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psProd = conn.prepareStatement(query);
             PreparedStatement psTaglia = conn.prepareStatement(queryTaglia)) {

            psProd.setString(1, nome);
            ResultSet rs = psProd.executeQuery();

            while (rs.next()) {
                prod.setIDProdotto(rs.getInt(IDPROD_PARAM));
                prod.setNomeProdotto(rs.getString(NOME_PARAM));
                prod.setDescrizioneProdotto(rs.getString(DESCRIZIONE_PARAM));
                prod.setPrezzoProdotto(rs.getFloat(PREZZO_PARAM));
                prod.setPrezzoOffertaProdotto(rs.getFloat(PREZZO_OFFERTA_PARAM));
                prod.setMarcaProdotto(rs.getString(MARCA_PARAM));
                prod.setModelloProdotto(rs.getString(MODELLO_PARAM));
                prod.setMaterialeProdotto(rs.getString(MATERIALE_PARAM));
                prod.setDisponibilitaProdotto(rs.getBoolean(DISPONIBILITA_PARAM));

                psTaglia.setInt(1, prod.getIDProdotto());

                try (ResultSet rsTaglia = psTaglia.executeQuery()) {
                    List<TagliaProdottoBean> taglieProdotto = new ArrayList<>();
                    while (rsTaglia.next()) {
                        TagliaProdottoBean taglia = new TagliaProdottoBean();

                        taglia.setIdProdotto(rsTaglia.getInt(ID_TAGLIAPROD_PARAM));
                        taglia.setIdProdotto(rsTaglia.getInt(IDPROD_PARAM));
                        taglia.setTaglia(rsTaglia.getString(TAGLIA_PARAM));
                        taglia.setQuantita(rsTaglia.getInt(QUANTITA_PARAM));

                        taglieProdotto.add(taglia);
                    }
                    prod.setTaglieProdotto(taglieProdotto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
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
    public synchronized List<ProdottoBean> doRetrieveByCategory(String category) throws SQLException {
        List<ProdottoBean> categoryProd = new ArrayList<>();

        String query = "SELECT p.* FROM " + TABLE_PRODOTTO + " p JOIN " + TABLE_APPARTENENZA + " a ON p.ID_Prodotto = a.ID_Prodotto "
                + "JOIN Categoria c ON a.NomeCategoria = c.NomeCategoria WHERE c.NomeCategoria = ?";
        String queryTaglia = SELECT_ALL_FROM + TABLE_TAGLIAPRODOTTO + WHERE_IDPROD;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psProd = conn.prepareStatement(query);
             PreparedStatement psTaglia = conn.prepareStatement(queryTaglia)) {

            psProd.setString(1, category);
            ResultSet rs = psProd.executeQuery();

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
                prod.setDisponibilitaProdotto(rs.getBoolean(DISPONIBILITA_PARAM));

                psTaglia.setInt(1, prod.getIDProdotto());

                try (ResultSet rsTaglia = psTaglia.executeQuery()) {
                    List<TagliaProdottoBean> taglieProdotto = new ArrayList<>();
                    while (rsTaglia.next()) {
                        TagliaProdottoBean taglia = new TagliaProdottoBean();

                        taglia.setIdProdotto(rsTaglia.getInt(ID_TAGLIAPROD_PARAM));
                        taglia.setIdProdotto(rsTaglia.getInt(IDPROD_PARAM));
                        taglia.setTaglia(rsTaglia.getString(TAGLIA_PARAM));
                        taglia.setQuantita(rsTaglia.getInt(QUANTITA_PARAM));

                        taglieProdotto.add(taglia);
                    }
                    prod.setTaglieProdotto(taglieProdotto);
                }

                categoryProd.add(prod);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
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

        String query = "SELECT ID_Prodotto, Nome, Descrizione, Prezzo, " +
                "CASE WHEN Prezzo_Offerta > 0 AND Prezzo_Offerta < Prezzo THEN Prezzo_Offerta ELSE Prezzo END AS PrezzoEffettivo, Disponibilita " +
                "FROM " + TABLE_PRODOTTO + " WHERE Prezzo_Offerta > 0 AND Prezzo_Offerta < Prezzo " +
                "ORDER BY Prezzo - Prezzo_Offerta DESC LIMIT 10";
        String queryTaglia = SELECT_ALL_FROM + TABLE_TAGLIAPRODOTTO + WHERE_IDPROD;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psProd = conn.prepareStatement(query);
             PreparedStatement psTaglia = conn.prepareStatement(queryTaglia)) {

            ResultSet rs = psProd.executeQuery();

            while (rs.next()) {
                ProdottoBean prod = new ProdottoBean();

                prod.setIDProdotto(rs.getInt(IDPROD_PARAM));
                prod.setNomeProdotto(rs.getString(NOME_PARAM));
                prod.setDescrizioneProdotto(rs.getString(DESCRIZIONE_PARAM));
                prod.setPrezzoProdotto(rs.getFloat(PREZZO_PARAM)); // Prezzo originale
                prod.setPrezzoOffertaProdotto(rs.getFloat("PrezzoEffettivo"));
                prod.setDisponibilitaProdotto(rs.getBoolean(DISPONIBILITA_PARAM));

                psTaglia.setInt(1, prod.getIDProdotto());

                try (ResultSet rsTaglia = psTaglia.executeQuery()) {
                    List<TagliaProdottoBean> taglieProdotto = new ArrayList<>();
                    while (rsTaglia.next()) {
                        TagliaProdottoBean taglia = new TagliaProdottoBean();

                        taglia.setIdProdotto(rsTaglia.getInt(ID_TAGLIAPROD_PARAM));
                        taglia.setIdProdotto(rsTaglia.getInt(IDPROD_PARAM));
                        taglia.setTaglia(rsTaglia.getString(TAGLIA_PARAM));
                        taglia.setQuantita(rsTaglia.getInt(QUANTITA_PARAM));

                        taglieProdotto.add(taglia);
                    }
                    prod.setTaglieProdotto(taglieProdotto);
                }

                bestOnOffer.add(prod);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }

        return bestOnOffer;
    }


    /**
     * funzione che aggiorna diminuendo la quantità dei prodotti dopo un ordine
     * @param id
     * @param quantity
     * @throws SQLException
     */
    public synchronized void doDecreaseProductQuantity (int id, String taglia, int quantity) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = UPDATE + TABLE_TAGLIAPRODOTTO + " SET Quantita = Quantita - ? WHERE ID_Prodotto = ? AND Taglia = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, quantity);
            ps.setInt(2, id);
            ps.setString(3, taglia);

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

    // Metodi per la gestione dei filtri

    /**
     * restituisce una lista di prodotti ordinata in base al nome di categoria
     * @return
     * @throws SQLException
     */
    public synchronized List<ProdottoBean> getProdottiOrdinatiPerCategoria() throws SQLException {
        List<ProdottoBean> prodotti = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT p.*, c.NomeCategoria FROM Prodotto p " +
                "JOIN Appartenenza a ON p.ID_Prodotto = a.ID_Prodotto " +
                "JOIN Categoria c ON a.NomeCategoria = c.NomeCategoria " +
                "ORDER BY CASE " +
                "WHEN c.NomeCategoria = 'T-Shirt' THEN 1 " +
                "WHEN c.NomeCategoria = 'Canotte' THEN 2 " +
                "WHEN c.NomeCategoria = 'Felpe con cappuccio e Felpe' THEN 3 " +
                "WHEN c.NomeCategoria = 'Giacche' THEN 4 " +
                "WHEN c.NomeCategoria = 'Occhiali' THEN 5 " +
                "WHEN c.NomeCategoria = 'Pantaloncini' THEN 6 " +
                "WHEN c.NomeCategoria = 'Cappelli' THEN 7 " +
                "WHEN c.NomeCategoria = 'Title Belts Replica' THEN 8 " +
                "WHEN c.NomeCategoria = 'Side Plates' THEN 9 " +
                "WHEN c.NomeCategoria = 'Memorabilia' THEN 10 " +
                "WHEN c.NomeCategoria = 'Foto' THEN 11 " +
                "WHEN c.NomeCategoria = 'Figure' THEN 12 " +
                "WHEN c.NomeCategoria = 'Orologi' THEN 13 " +
                "WHEN c.NomeCategoria = 'Cover Phone' THEN 14 " +
                "WHEN c.NomeCategoria = 'Zaini e borse' THEN 15 " +
                "ELSE 16 END";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            // HashSet per tenere traccia degli ID dei prodotti già aggiunti
            Set<Integer> aggiunti = new HashSet<>();

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idProdotto = rs.getInt(IDPROD_PARAM);
                if (!aggiunti.contains(idProdotto)) {
                    ProdottoBean prod = new ProdottoBean();
                    prod.setIDProdotto(rs.getInt(IDPROD_PARAM));
                    prod.setNomeProdotto(rs.getString(NOME_PARAM));
                    prod.setDescrizioneProdotto(rs.getString(DESCRIZIONE_PARAM));
                    prod.setPrezzoProdotto(rs.getFloat(PREZZO_PARAM));
                    prod.setPrezzoOffertaProdotto(rs.getFloat(PREZZO_OFFERTA_PARAM));
                    prod.setMarcaProdotto(rs.getString(MARCA_PARAM));
                    prod.setModelloProdotto(rs.getString(MODELLO_PARAM));
                    prod.setMaterialeProdotto(rs.getString(MATERIALE_PARAM));
                    prod.setDisponibilitaProdotto(rs.getBoolean(DISPONIBILITA_PARAM));

                    prodotti.add(prod);
                    aggiunti.add(idProdotto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            // Chiusura PreparedStatement e Connection
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
     * ritorna il prezzo del prodotto appartenente ad un ordine
     * @param idProd
     * @param idOrdine
     * @return
     * @throws SQLException
     */
    public synchronized float doRetrievePrezzoOrdine(int idProd, int idOrdine) throws SQLException {
        float prezzo = 0;

        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT Prezzo FROM ComposizioneOrdine WHERE ID_Prodotto = ? AND ID_Ordine = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, idProd);
            ps.setInt(2, idOrdine);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                prezzo = rs.getFloat(PREZZO_PARAM);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            // Chiusura PreparedStatement e Connection
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
        return prezzo;
    }


    /**
     * funzione che restituisce il tipo di categoria a cui appartiene un prodotto
     * @param idProdotto
     * @return
     * @throws SQLException
     */
    public synchronized String getTipoCategoria(int idProdotto) throws SQLException {
        String tipoCategoria = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT c.TipoCategoria FROM Prodotto p " +
                "JOIN Appartenenza a ON p.ID_Prodotto = a.ID_Prodotto " +
                "JOIN Categoria c ON a.NomeCategoria = c.NomeCategoria " +
                "WHERE p.ID_Prodotto = ? " +
                "AND c.TipoCategoria IN ('Title Belts', 'Abbigliamento', 'Accessori', 'Oggetti da collezione')";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, idProdotto);

            rs = ps.executeQuery();
            if (rs.next()) {
                tipoCategoria = rs.getString("TipoCategoria");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            // Chiusura ResultSet, PreparedStatement e Connection
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, MSG_ERROR_RS, e);
            }
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
        return tipoCategoria;
    }


    /**
     * funzione che restituisce il sesso di quel determinato prodotto
     * @param idProdotto
     * @return
     * @throws SQLException
     */
    public synchronized String getSessoProdotto (int idProdotto) throws SQLException {
        String sessoProdotto = "";
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT a.NomeCategoria FROM Prodotto p " +
                "JOIN Appartenenza a ON p.ID_Prodotto = a.ID_Prodotto " +
                "JOIN Categoria c ON a.NomeCategoria = c.NomeCategoria " +
                "WHERE p.ID_Prodotto = ? AND c.TipoCategoria = 'Sesso'";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, idProdotto);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                sessoProdotto = rs.getString("NomeCategoria");
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
        return sessoProdotto;
    }


    /**
     * funzione che verifica se un determinato prodotto è firmato o no
     * @param idProdotto
     * @return
     * @throws SQLException
     */
    public synchronized boolean isFirmato (int idProdotto) throws SQLException {
        boolean firmato = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "SELECT EXISTS (" +
                "SELECT 1 FROM Appartenenza a " +
                "JOIN Categoria c ON a.NomeCategoria = c.NomeCategoria " +
                "WHERE a.ID_Prodotto = ? AND c.TipoCategoria = 'Oggetti da collezione' AND c.NomeCategoria = 'Firmato')";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, idProdotto);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                firmato = rs.getBoolean(1);
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
        return firmato;
    }

    // Metodi per la gestione dell'admin

    /**
     * funzione che si occupa del salvataggio di un prodotto nel database
     * @param prod dettagli del prodotto
     * @param immagini immagini del prodotto
     * @param taglie le taglie del prodotto
     * @param categorie le categorie a cui appartiene
     * @throws SQLException
     */
    public synchronized void doSaveProduct(ProdottoBean prod, List<String> immagini, List<TagliaProdottoBean> taglie, List<CategoriaBean> categorie) throws SQLException {
        Connection conn = null;

        String queryProdotto = INSERT_INTO + TABLE_PRODOTTO + " (Nome, Descrizione, Materiale, Marca, Modello, Prezzo, Prezzo_Offerta, Disponibilita) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); // Avvia la transazione

            int idProdotto = inserisciProdotto(conn, queryProdotto, prod);
            inserisciImmagini(conn, idProdotto, immagini);
            inserisciTaglie(conn, idProdotto, taglie);
            inserisciCategorie(conn, idProdotto, categorie);

            conn.commit();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            // chiusura Connection
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, MSG_ERROR_CONN, e);
            }
        }
    }

    private int inserisciProdotto(Connection conn, String queryProdotto, ProdottoBean prod) throws SQLException {
        try (PreparedStatement psProdotto = conn.prepareStatement(queryProdotto, Statement.RETURN_GENERATED_KEYS)) {
            psProdotto.setString(1, prod.getNomeProdotto());
            psProdotto.setString(2, prod.getDescrizioneProdotto());
            psProdotto.setString(3, prod.getMaterialeProdotto());
            psProdotto.setString(4, prod.getMarcaProdotto());
            psProdotto.setString(5, prod.getModelloProdotto());
            psProdotto.setFloat(6, prod.getPrezzoProdotto());
            psProdotto.setFloat(7, prod.getPrezzoOffertaProdotto());
            psProdotto.setBoolean(8, prod.getDisponibilitaProdotto());

            psProdotto.executeUpdate();

            // Ottieni l'ID generato per il prodotto
            ResultSet generatedKeys = psProdotto.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Inserimento del prodotto fallito, nessun ID ottenuto.");
            }
        }
    }

    private void inserisciImmagini(Connection conn, int idProdotto, List<String> immagini) throws SQLException {
        String queryImmagine = INSERT_INTO + TABLE_IMMAGINE + " (NomeImg, ID_Prodotto) VALUES (?, ?)";

        try (PreparedStatement psImmagine = conn.prepareStatement(queryImmagine)) {
            // Imposta il valore del parametro ID_Prodotto al di fuori del ciclo
            psImmagine.setInt(2, idProdotto);

            for (String immagine : immagini) {
                psImmagine.setString(1, immagine);
                psImmagine.addBatch();
            }
            psImmagine.executeBatch();
        }
    }


    private void inserisciTaglie(Connection conn, int idProdotto, List<TagliaProdottoBean> taglie) throws SQLException {
        String queryTaglia = INSERT_INTO + TABLE_TAGLIAPRODOTTO + " (ID_Prodotto, Taglia, Quantita) VALUES (?, ?, ?)";

        try (PreparedStatement psTaglia = conn.prepareStatement(queryTaglia)) {
            // Imposta il valore del parametro ID_Prodotto al di fuori del ciclo
            psTaglia.setInt(1, idProdotto);

            for (TagliaProdottoBean taglia : taglie) {
                psTaglia.setString(2, taglia.getTaglia());
                psTaglia.setInt(3, taglia.getQuantita());
                psTaglia.addBatch();
            }
            psTaglia.executeBatch();
        }
    }


    private void inserisciCategorie(Connection conn, int idProdotto, List<CategoriaBean> categorie) throws SQLException {
        String queryCategoria = INSERT_INTO + TABLE_APPARTENENZA + " (NomeCategoria, ID_Prodotto) VALUES (?, ?)";

        try (PreparedStatement psCategoria = conn.prepareStatement(queryCategoria)) {
            // Imposta il valore del parametro ID_Prodotto al di fuori del ciclo
            psCategoria.setInt(2, idProdotto);

            for (CategoriaBean categoria : categorie) {
                psCategoria.setString(1, categoria.getNome());
                psCategoria.addBatch();
            }
            psCategoria.executeBatch();
        }
    }


    /**
     * funzione che gestice l'operazione di rimozione di un prodotto dal database
     * @param id
     * @throws SQLException
     */
    public synchronized void doDeleteProduct (int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "DELETE FROM " + TABLE_PRODOTTO + WHERE_IDPROD;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);

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
     * funzione che gestisce l'operazione di aggiunta quantità ad un prodotto con taglia indicata
     * @param id
     * @param taglia
     * @param quantity
     * @throws SQLException
     */
    public synchronized void addQuantityProduct (int id, String taglia, int quantity) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = UPDATE + TABLE_TAGLIAPRODOTTO + " SET Quantita = Quantita + ? WHERE ID_Prodotto = ? AND Taglia = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, quantity);
            ps.setInt(2, id);
            ps.setString(3, taglia);

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
     * funzione che gestisce l'operazione di modificare la disponibiltà di un prodotto
     * @param id
     * @throws SQLException
     */
    public synchronized void makeProductUnavailable (int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = UPDATE + TABLE_PRODOTTO + " SET Disponibilita = FALSE " + WHERE_IDPROD;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);

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
     * funzione che gestisce l'operazione di modificare la disponibiltà di un prodotto
     * @param id
     * @throws SQLException
     */
    public synchronized void makeProductAvailable (int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = UPDATE + TABLE_PRODOTTO + " SET Disponibilita = TRUE " + WHERE_IDPROD;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, id);

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
     * funzione che gestisce l'operazione di modificare il prezzo di un prodotto
     * @param id
     * @param newPrice
     * @throws SQLException
     */
    public synchronized void doUpdateProductPrice (int id, float newPrice) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = UPDATE + TABLE_PRODOTTO + " SET Prezzo = ? WHERE ID_Prodotto = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setFloat(1, newPrice);
            ps.setInt(2, id);

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
     * funzione che gestisce l'operazione di modificare il prezzo di offerta di un prodotto
     * @param id
     * @param newOfferPrice
     * @throws SQLException
     */
    public synchronized void doUpdateProductOfferPrice (int id, float newOfferPrice) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = UPDATE + TABLE_PRODOTTO + " SET Prezzo_Offerta = ? WHERE ID_Prodotto = ?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setFloat(1, newOfferPrice);
            ps.setInt(2, id);

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
     * funzione che gestisce l'operazione di aggiungere ad un prodotto l'appartenenza ad una categoria
     * @param idProdotto
     * @param nomeCategoria
     * @throws SQLException
     */
    public synchronized void doAddProductCategory(int idProdotto, String nomeCategoria) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String query = INSERT_INTO + TABLE_APPARTENENZA + " (NomeCategoria, ID_Prodotto) VALUES (?, ?)";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, nomeCategoria);
            ps.setInt(2, idProdotto);

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
