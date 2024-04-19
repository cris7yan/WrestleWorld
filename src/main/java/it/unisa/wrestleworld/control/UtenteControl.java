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
    private static final String MSG_ERROR_FORWARD = "Errore durante il forward della richiesta";
    private static final String INDEX_PAGE = "./index.jsp";


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
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
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

    private void login (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UtenteBean utente = new UtenteBean();

        // preleviamo dalla request i valori di email e password
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(true);

        if(email == null || password == null) {
            try {
                response.sendRedirect(INDEX_PAGE);
            } catch (IOException ex) {
                logger.log(Level.WARNING, MSG_ERROR_LOGINPAGE, ex);
            }
        } else {
            try {
                utente = utModel.doRetrieveByEmailPassword(email, password);
                if (utente == null) {
                    request.setAttribute("result", "Credenziali errate");
                    RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/login.jsp");
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
            } catch (SQLException e) {
                logger.log(Level.WARNING, e.getMessage());
            } catch (ServletException | IOException e) {
                logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
            }
        }
    }

    private void logout (HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        try {
            response.sendRedirect(INDEX_PAGE);
        } catch (IOException ex) {
            logger.log(Level.WARNING, MSG_ERROR_INDEXPAGE, ex);
        }
    }
}
