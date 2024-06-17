package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.ProdottoBean;
import it.unisa.wrestleworld.model.ProdottoModel;
import it.unisa.wrestleworld.util.Carrello;

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
    static final Logger logger = Logger.getLogger(ProdottoControl.class.getName());

    private static ProdottoModel prodModel = new ProdottoModel();
    private static Carrello carrello = new Carrello();

    private static final String MSG_ERROR_DOPOST = "Errore durante l'esecuzione di doPost";
    private static final String MSG_ERROR_FORWARD = "Errore durante il forward della richiesta";

    public ProdottoControl () {
        super();
    }


    /**
     * funzione che gestisce l'operazione doGet
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // preleviamo l'azione dalla request
        String action = request.getParameter("action");

        try {
            if(action != null) {
                if(action.equalsIgnoreCase("visualizzaHomePage")) {
                    visualizzaHomePage(request, response);
                } else if (action.equalsIgnoreCase("visualizzaCatalogo")) {
                    visualizzaCatalogo(request, response);
                } else if (action.equalsIgnoreCase("visualizzaDettagliProdotto")) {
                    visualizzaDettagliProdotto(request, response);
                } else if (action.equalsIgnoreCase("aggiungiAlCarrello")) {
                    aggiungiProdottoCarrello(request, response);
                }
            }
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }


    /**
     * funzione che gestisce l'operazione di doPost
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
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


    /**
     * funzione che permette la visualizzazione della homepage
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaHomePage (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Liste per prelevare dal database i prodotti ed i prodotti piu venduti
            List<ProdottoBean> bestSellers = prodModel.doRetrieveBestSellers();
            request.setAttribute("bestSellers", bestSellers);

            // prelevo le immagini per i prodotti piu venduti
            List<String> imgBestProd = new ArrayList<>();
            for (ProdottoBean best : bestSellers) {
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


    /**
     * funzione che gestisce la visualizzazione dei prodotti nel catalogo
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaCatalogo (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Liste per prelevare dal database i prodotti ed i prodotti piu venduti
            List<ProdottoBean> prodotti = prodModel.doRetrieveAll();
            request.setAttribute("prodotti", prodotti);

            // prelevo le immagini dei vari prodotti
            List<String> imgProdotti = new ArrayList<>();
            for(ProdottoBean prod : prodotti) {
                imgProdotti.add(prodModel.doRetrieveAllImages(prod).get(0));
            }
            request.setAttribute("imgProdotti", imgProdotti);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/catalogo.jsp");
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }


    /**
     * funzione che gestisce la visualizzazione dei dettagli di un prodotto
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaDettagliProdotto (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idProd = Integer.parseInt(request.getParameter("IDProd"));
            Object prod = prodModel.doRetrieveByID(idProd);

            List<String> imgProd = new ArrayList<>();
            imgProd = prodModel.doRetrieveAllImages((ProdottoBean) prod);

            request.setAttribute("prodotto", prod);
            request.setAttribute("imgProd", imgProd);
            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/dettagliProdotto.jsp");
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }


    /**
     * funzione che gestisce l'operazione di aggiunta di un prodotto al carrello
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void aggiungiProdottoCarrello (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idProd = Integer.parseInt(request.getParameter("IDProd"));
            if(prodModel.checkProductAvailability(idProd)) {
                carrello.addProdottoCarrello(prodModel.doRetrieveByID(idProd));
            }
            List<ProdottoBean> cart = carrello.getCarrello();
            request.getSession().setAttribute("carrello", carrello);
            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/carrello.jsp");
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }

}
