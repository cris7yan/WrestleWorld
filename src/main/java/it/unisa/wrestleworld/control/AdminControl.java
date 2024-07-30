package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
                    case "visualizzaOrdiniTotali":
                        visualizzaOrdiniTotali(request, response);
                        break;
                    case "incrementaQuantitaProdotto":
                        incrementaQuantitaProdotto(request, response);
                        break;
                    case "eliminaProdotto":
                        eliminaProdotto(request, response);
                        break;
                    case "rendiIndisponibileProdotto":
                        rendiIndisponibileProdotto(request, response);
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


    /**
     * funzione che permette di visualizzare tutti gli utenti del sito
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaUtenti (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<UtenteBean> utenti = utModel.doRetrieveAllUtenti();

            request.setAttribute("utenti", utenti);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/adminUtenti.jsp");
            reqDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che permette di visualizzare tutti gli ordini effettuati da un solo utente
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
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


    /**
     * funzione che permette di visualizzare tutti gli ordini effettuati sul sito
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaOrdiniTotali (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<OrdineBean> ordini = ordineModel.doRetrieveAllOrdini();

            request.setAttribute("ordini", ordini);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/adminOrdini.jsp");
            reqDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che permette ad un admin di modificare la quantità di un prodotto
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void incrementaQuantitaProdotto (HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idProdStr = request.getParameter("IDProd");
            String taglia = request.getParameter("taglia");
            String quantitaStr = request.getParameter("quantita");

            int idProd = Integer.parseInt(idProdStr);
            int quantita = Integer.parseInt(quantitaStr);

            prodModel.addQuantityProduct(idProd, taglia, quantita);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"message\": \"Quantità aggiornata con successo\"}");
            out.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID o quantità non validi");
        }
    }


    /**
     * funzione che permette ad un admin di eliminare un prodotto dal sistema
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void eliminaProdotto (HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idProdStr = request.getParameter("IDProd");

            int idProd = Integer.parseInt(idProdStr);

            prodModel.doDeleteProduct(idProd);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"message\": \"Prodotto eliminato con successo\"}");
            out.flush();

            response.sendRedirect("catalogo.jsp");
        } catch (IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID o quantità non validi");
        }
    }


    /**
     * funzione che permette ad un admin di aggiornare la disponibilità di un prodotto
     * @param request
     * @param response
     * @throws IOException
     */
    private void rendiIndisponibileProdotto (HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idProdStr = request.getParameter("IDProd");

            int idProd = Integer.parseInt(idProdStr);

            prodModel.makeProductUnavailable(idProd);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"message\": \"Prodotto reso indisponibile con successo\"}");
            out.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID o quantità non validi");
        }
    }

}
