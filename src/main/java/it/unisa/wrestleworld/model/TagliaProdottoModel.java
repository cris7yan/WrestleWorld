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

public class TagliaProdottoModel implements TagliaProdottoDAO {
    private static Logger logger = Logger.getLogger(TagliaProdottoModel.class.getName());
    private static DataSource dataSource;

    private static final String TABLE_TAGLIAPRODOTTO = "TagliaProdotto";

    private static final String IDPROD_PARAM = "ID_Prodotto";
    private static final String ID_TAGLIAPROD_PARAM = "ID_TagliaProdotto";
    private static final String TAGLIA_PARAM = "Taglia";
    private static final String QUANTITA_PARAM = "Quantita";

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


    public synchronized List<TagliaProdottoBean> doRetrieveAllSizeByProduct (ProdottoBean prod) throws SQLException {
        List<TagliaProdottoBean> taglieProdotto =  new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;

        String queryTaglia = SELECT_ALL_FROM + TABLE_TAGLIAPRODOTTO + WHERE_IDPROD;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(queryTaglia);

            ps.setInt(1, prod.getIDProdotto());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TagliaProdottoBean taglia = new TagliaProdottoBean();

                taglia.setIdProdotto(rs.getInt(ID_TAGLIAPROD_PARAM));
                taglia.setIdProdotto(rs.getInt(IDPROD_PARAM));
                taglia.setTaglia(rs.getString(TAGLIA_PARAM));
                taglia.setQuantita(rs.getInt(QUANTITA_PARAM));

                taglieProdotto.add(taglia);
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
        return taglieProdotto;
    }
}
