package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.UtenteBean;
import it.unisa.wrestleworld.model.UtenteDAO;
import it.unisa.wrestleworld.model.UtenteModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;

@WebServlet("/UtenteControl")
public class UtenteControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static UtenteDAO utModel = new UtenteModel();
    static final Logger logger = Logger.getLogger(UtenteControl.class.getName());
    private static final String MSG_ERROR_LOGINPAGE = "Errore durante il reindirizzamento alla pagina di login";
    private static final String MSG_ERROR_INDEXPAGE = "Errore durante il reindirizzamento alla pagina principale";
    private static final String MSG_ERROR_DOPOST = "Errore durante l'esecuzione di doPost";
    private static final String MSG_ERROR_FORWARD = "Errore durante il forward della richiesta";
    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";
    private static final String INDEX_PAGE = "./index.jsp";


    public UtenteControl () {
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
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // preleviamo l'azione dalla request
        String action = request.getParameter("action");

        try {
            if(action != null) {
                if(action.equalsIgnoreCase("login")) {
                    login(request, response);
                } else if (action.equalsIgnoreCase("logout")) {
                    logout(request, response);
                } else if(action.equalsIgnoreCase("registrazione")) {
                    registrazione(request, response);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
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
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doGet(request, response);
        } catch (ServletException ex) {
            logger.log(Level.SEVERE, MSG_ERROR_DOPOST, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, MSG_ERROR_DOPOST, ex);
        }
    }


    /**
     * funzione che gestisce l'operazione di login di un utente
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void login (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UtenteBean utente = new UtenteBean();

            // preleviamo dalla request i valori di email e password
            String email = request.getParameter(EMAIL_PARAM);
            String password = request.getParameter(PASSWORD_PARAM);

            HttpSession session = request.getSession(true);

            if (email == null || password == null) {
                response.sendRedirect(INDEX_PAGE);
            } else {
                utente = utModel.doRetrieveByEmailPassword(email, password);
                if (utente == null) {
                    request.setAttribute("result", "Credenziali errate");
                    RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/login.jsp");
                    reqDispatcher.forward(request, response);
                } else {
                    session.setAttribute(EMAIL_PARAM, utente.getEmail());
                    session.setAttribute("tipo", utente.getTipoUtente());
                    response.sendRedirect(INDEX_PAGE);
                }
            }
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce l'operazione di logout
     * @param request
     * @param response
     * @throws IOException
     */
    private void logout (HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        try {
            response.sendRedirect(INDEX_PAGE);
        } catch (IOException ex) {
            logger.log(Level.WARNING, MSG_ERROR_INDEXPAGE, ex);
        }
    }


    /**
     * funzione che gestisce l'operazione di registrazione di un utente
     * @param request
     * @param response
     * @throws SQLException
     * @throws ServletException
     * @throws IOException
     */
    private void registrazione (HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        try {
            // preleviamo i dati dalla request
            String email = request.getParameter(EMAIL_PARAM);
            String password = request.getParameter(PASSWORD_PARAM);
            String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            Date dataNascita = Date.valueOf(request.getParameter("dataNascita"));

            // creiamo un nuovo utente ed impostiamo i dati
            UtenteBean newUtente = new UtenteBean();
            newUtente.setEmail(email);
            newUtente.setPassword(password);
            newUtente.setNome(nome);
            newUtente.setCognome(cognome);
            newUtente.setDataNascita(dataNascita);
            newUtente.setTipoUtente("Utente");

            HttpSession session = request.getSession(true);

            if(utModel.verificaEmailEsistente(email)) {
                request.setAttribute("result", "Email già utilizzata, sceglierne un'altra");
                RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/registrazione.jsp");
                reqDispatcher.forward(request, response);
            } else {
                utModel.doSave(newUtente);
                session.setAttribute(EMAIL_PARAM, newUtente.getEmail());
                session.setAttribute("tipo", newUtente.getTipoUtente());
                response.sendRedirect(INDEX_PAGE);
            }
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}
