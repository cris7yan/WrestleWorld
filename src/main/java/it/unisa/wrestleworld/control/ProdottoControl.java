package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.ProdottoBean;
import it.unisa.wrestleworld.model.ProdottoModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ProdottoControl")
public class ProdottoControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static ProdottoModel prodModel = new ProdottoModel();
    static final Logger logger = Logger.getLogger(ProdottoControl.class.getName());
    private static final String MSG_ERROR_DOPOST = "Errore durante l'esecuzione di doPost";
    private static final String MSG_ERROR_FORWARD = "Errore durante il forward della richiesta";

    public ProdottoControl () {
        super();
    }

    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Liste per prelevare dal database i prodotti ed i prodotti piu venduti
            List<ProdottoBean> prodotti = prodModel.doRetrieveAll();
            List<ProdottoBean> bestSellers = prodModel.doRetrieveBestSellers();
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("bestSellers", bestSellers);

            // prelevo le immagini dei vari prodotti
            List<String> imgProdotti = new ArrayList<>();
            for(ProdottoBean prod : prodotti) {
                imgProdotti.add(prodModel.doRetrieveAllImages(prod).get(0));
            }
            request.setAttribute("imgProdotti", imgProdotti);

            // prelevo le immagini per i prodotti piu venduti
            List<String> imgBestProd = new ArrayList<>();
            for(ProdottoBean best : bestSellers) {
                imgBestProd.add(prodModel.doRetrieveAllImages(best).get(0));
            }
            request.setAttribute("imgBestProd", imgBestProd);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/index.jsp");
            reqDispatcher.forward(request, response);

        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }

    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doGet(request, response);
        } catch (ServletException ex) {
            logger.log(Level.SEVERE, MSG_ERROR_DOPOST, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, MSG_ERROR_DOPOST, ex);
        }
    }

}
