package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.CategoriaBean;
import it.unisa.wrestleworld.model.CategoriaModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/CategoriaControl")
public class CategoriaControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final Logger logger = Logger.getLogger(CategoriaControl.class.getName());

    private static CategoriaModel catModel = new CategoriaModel();

    private static final String SUPERSTAR_PARAM = "superstar";
    private static final String PLE_PARAM = "ple";

    private static final String CATEGORIE_PAGE = "/categorie.jsp";

    private static final String MSG_ERROR_DOPOST = "Errore durante l'esecuzione di doPost";
    private static final String MSG_ERROR_FORWARD = "Errore durante il forward della richiesta";

    public CategoriaControl() {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // preleviamo l'azione dalla request
        String action = request.getParameter("action");

        try {
            if (action != null) {
                switch (action) {
                    case "visualizzaSuperstar":
                        visualizzaSuperstar(request, response);
                        break;
                    case "visualizzaPremiumLiveEvent":
                        visualizzaPremiumLiveEvent(request, response);
                        break;
                    case "visualizzaCategoriePerTipo":
                        visualizzaCategoriePerTipo(request, response);
                        break;
                    default:
                        visualizzaCategorie(request, response);
                        break;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Azione mancante");
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
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_DOPOST, e);
        }
    }


    /**
     * Metodo che si occupa di far visualizzare tutte le superstar presenti nel sito
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaSuperstar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<CategoriaBean> superstar = catModel.doRetrieveAllSuperstar();
            List<CategoriaBean> ple = catModel.doRetrieveAllPLE(); // Aggiungi questa linea
            request.setAttribute(SUPERSTAR_PARAM, superstar);
            request.setAttribute(PLE_PARAM, ple); // Aggiungi questa linea

            RequestDispatcher reqDispatcher = request.getRequestDispatcher(CATEGORIE_PAGE);
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }


    /**
     * Metodo per visualizzare i PLE presenti sul sito
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaPremiumLiveEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<CategoriaBean> ple = catModel.doRetrieveAllPLE();
            List<CategoriaBean> superstar = catModel.doRetrieveAllSuperstar(); // Aggiungi questa linea
            request.setAttribute(PLE_PARAM, ple);
            request.setAttribute(SUPERSTAR_PARAM, superstar); // Aggiungi questa linea

            RequestDispatcher reqDispatcher = request.getRequestDispatcher(CATEGORIE_PAGE);
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }


    /**
     * Metodo aggiuntivo per visualizzare entrambe le categorie di default
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaCategorie(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<CategoriaBean> ple = catModel.doRetrieveAllPLE();
            List<CategoriaBean> superstar = catModel.doRetrieveAllSuperstar();
            request.setAttribute(PLE_PARAM, ple);
            request.setAttribute(SUPERSTAR_PARAM, superstar);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher(CATEGORIE_PAGE);
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }


    private void visualizzaCategoriePerTipo (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Map<String, List<CategoriaBean>> categoriePerTipo = catModel.doRetrieveAllGroupedByType();
            request.setAttribute("categoriePerTipo", categoriePerTipo);

            // Debug: Log the size of the map
            logger.info("Categorie Per Tipo: " + categoriePerTipo);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("nuovoProdotto.jsp");
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }

}
