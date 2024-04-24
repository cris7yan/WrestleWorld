package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.OrdineBean;
import it.unisa.wrestleworld.model.OrdineDAO;
import it.unisa.wrestleworld.model.OrdineModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/OrdineControl")
public class OrdineControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static OrdineDAO ordineModel = new OrdineModel();
    static final Logger logger = Logger.getLogger(OrdineControl.class.getName());
    private static final String MSG_ERROR_FORWARD = "Errore durante il forward della richiesta";
    private static final String MSG_ERROR_DOPOST = "Errore durante l'esecuzione di doPost";

    public OrdineControl () {
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
        String action = request.getParameter("action");

        try {
            if (action != null) {
                if(action.equalsIgnoreCase("visualizzaOrdini")) {
                    visualizzaOrdini(request, response);
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
     * funzione che si occupa di recuperare tutti gli ordini di un utente
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaOrdini (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute("email");
            List<OrdineBean> ordini = new ArrayList<>();

            ordini = ordineModel.doRetrieveAllByEmail(email);

            request.setAttribute("ordini", ordini);
            RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/ordini.jsp");
            reqDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}
