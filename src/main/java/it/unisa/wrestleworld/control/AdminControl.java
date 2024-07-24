package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.*;

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

@WebServlet("/AdminControl")
public class AdminControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final Logger logger = Logger.getLogger(AdminControl.class.getName());

    static UtenteDAO utModel = new UtenteModel();
    static OrdineDAO ordineModel = new OrdineModel();
    static ProdottoDAO prodModel = new ProdottoModel();

    private static final String MSG_ERROR_INDEXPAGE = "Errore durante il reindirizzamento alla pagina principale";
    private static final String MSG_ERROR_DOPOST = "Errore durante l'esecuzione di doPost";
    private static final String MSG_ERROR_FORWARD = "Errore durante il forward della richiesta";


    public AdminControl() {
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
        String action = request.getParameter("action");

        try {
            if (action != null) {
                switch (action) {
                    case "visualizzaUtenti":
                        visualizzaUtenti(request, response);
                        break;
                    case "visualizzaOrdiniUtenti":
                        visualizzaOrdiniUtenti(request, response);
                        break;
                    default:
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Azione non valida");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doGet(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_DOPOST, e);
        }
    }


    private void visualizzaUtenti (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<UtenteBean> utenti = new ArrayList<>();
            utenti = utModel.doRetrieveAllUtenti();

            request.setAttribute("utenti", utenti);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/admin.jsp");
            reqDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    private void visualizzaOrdiniUtenti (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            List<OrdineBean> ordiniUtente = new ArrayList<>(ordineModel.doRetrieveAllByEmail(email));

            request.setAttribute("ordini", ordiniUtente);
            request.setAttribute("email", email);
            request.setAttribute("nomeOrdine", utModel.doRetrieveByEmail(email).getNome());
            request.setAttribute("cognomeOrdine", utModel.doRetrieveByEmail(email).getCognome());

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/ordini.jsp");
            reqDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

}
