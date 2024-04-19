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

@WebServlet("/UtenteControl")
public class UtenteControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static UtenteDAO utModel = new UtenteModel();
    static final Logger logger = Logger.getLogger(UtenteControl.class.getName());
    private static final String MSG_ERROR_LOGINPAGE = "Errore durante il reindirizzamento alla pagina di login";
    private static final String MSG_ERROR_INDEXPAGE = "Errore durante il reindirizzamento alla pagina principale";
    private static final String MSG_ERROR_DOPOST = "Errore durante l'esecuzione di doPost";
    private static final String INDEX_PAGE = "./index.jsp";
    private static final String LOGIN_PAGE = "./login.jsp";

    public UtenteControl () {
        super();
    }

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
                }
            }
        } catch (ServletException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

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
     * funzione che gestisce l'operazione di login
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void login (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // preleviamo dalla request i valori di email e password
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UtenteBean utente = new UtenteBean();

        HttpSession session = request.getSession(true);

        if(email == null || password == null) {
            response.sendRedirect(LOGIN_PAGE);
        } else {
            try {
                utente = utModel.doRetrieveByEmailPassword(email, password);
            } catch (SQLException e) {
                logger.log(Level.WARNING, e.getMessage());
            }
            if(utente == null) {
                request.setAttribute("result", "Credenziali errate");
                RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher(LOGIN_PAGE);
                reqDispatcher.forward(request, response);
            } else {
                session.setAttribute("email", utente.getEmail());
                session.setAttribute("tipo", utente.getTipoUtente());
                try {
                    response.sendRedirect(INDEX_PAGE);
                } catch (IOException ex) {
                    logger.log(Level.WARNING, MSG_ERROR_INDEXPAGE, ex);
                }
            }
        }
    }

    /**
     * funzione che gestisce l'operazione di logout
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void logout (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        try {
            response.sendRedirect(INDEX_PAGE);
        } catch (IOException ex) {
            logger.log(Level.WARNING, MSG_ERROR_INDEXPAGE, ex);
        }
    }

}
