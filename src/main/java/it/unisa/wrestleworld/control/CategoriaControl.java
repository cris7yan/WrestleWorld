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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/CategoriaControl")
public class CategoriaControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final Logger logger = Logger.getLogger(CategoriaControl.class.getName());

    private static CategoriaModel catModel = new CategoriaModel();

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
            if(action.equals("visualizzaSuperstar")) {
                visualizzaSuperstar(request, response);
            }
            else if(action.equals("visualizzaPremiumLiveEvent")) {
                visualizzaPremiumLiveEvent(request, response);
            }
            else {
                visualizzaCategorie(request, response);
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


    private void visualizzaSuperstar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<CategoriaBean> superstar = catModel.doRetrieveAllSuperstar();
            List<CategoriaBean> ple = catModel.doRetrieveAllPLE(); // Aggiungi questa linea
            request.setAttribute("superstar", superstar);
            request.setAttribute("ple", ple); // Aggiungi questa linea

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/categorie.jsp");
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }

    private void visualizzaPremiumLiveEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<CategoriaBean> ple = catModel.doRetrieveAllPLE();
            List<CategoriaBean> superstar = catModel.doRetrieveAllSuperstar(); // Aggiungi questa linea
            request.setAttribute("ple", ple);
            request.setAttribute("superstar", superstar); // Aggiungi questa linea

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/categorie.jsp");
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }

    // Metodo aggiuntivo per visualizzare entrambe le categorie di default
    private void visualizzaCategorie(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<CategoriaBean> ple = catModel.doRetrieveAllPLE();
            List<CategoriaBean> superstar = catModel.doRetrieveAllSuperstar();
            request.setAttribute("ple", ple);
            request.setAttribute("superstar", superstar);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/categorie.jsp");
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        }
    }

}
