package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.*;

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
    static final Logger logger = Logger.getLogger(OrdineControl.class.getName());

    static OrdineDAO ordineModel = new OrdineModel();
    static IndirizzoDAO indirizzoModel = new IndirizzoModel();
    static MetodoPagamentoDAO metodoPagamentoModel = new MetodoPagamentoModel();

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
                switch (action) {
                    case "visualizzaOrdini":
                        visualizzaOrdini(request, response);
                        break;
                    case "visualizzaDettagliOrdine":
                        visualizzaDettagliOrdini(request, response);
                        break;
                    case "visualizzaDatiUtente":
                        visualizzaDatiUtente(request, response);
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
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doGet(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_DOPOST, e);
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
            List<OrdineBean> ordini = ordineModel.doRetrieveAllByEmail(email);

            request.setAttribute("ordini", ordini);
            RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/ordini.jsp");
            reqDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce la visualizzazione dei dettagli di un ordine
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaDettagliOrdini (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("idOrdine"));
            List<ProdottoBean> prodottiOrdine = new ArrayList<>(ordineModel.doRetrieveOrdineByID(id));

            request.setAttribute("prodottiOrdine", prodottiOrdine);
            RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/dettagliOrdine.jsp");
            reqDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    private void visualizzaDatiUtente (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute("email");

            List<Integer> indirizzi = indirizzoModel.doRetrieveAllID(email);
            List<IndirizzoBean> indirizziUtente = new ArrayList<>();
            for (int ind : indirizzi) {
                indirizziUtente.add(indirizzoModel.doRetrieveByID(ind));
            }

            List<Integer> metodi = metodoPagamentoModel.doRetrieveAllID(email);
            List<MetodoPagamentoBean> metodiPagamentoUtente = new ArrayList<>();
            for(int mp : metodi) {
                metodiPagamentoUtente.add(metodoPagamentoModel.doRetrieveByID(mp));
            }

            request.setAttribute("indirizziUtente", indirizziUtente);
            request.setAttribute("metodiPagamentoUtente", metodiPagamentoUtente);

            RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/paginaAcquisto.jsp");
            reqDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

}