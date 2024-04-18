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
    Logger logger = Logger.getLogger(UtenteControl.class.getName());

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
                    UtenteBean utente;

                    // preleviamo dalla request i valori di email e password
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");

                    HttpSession session = request.getSession(true);

                    if(email == null || password == null) {
                        response.sendRedirect("./login.jsp");
                    } else {
                        utente = utModel.doRetrieveByEmailPassword(email, password);
                        if(utente == null) {
                            request.setAttribute("result", "Credenziali errate");
                            RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/login.jsp");
                            reqDispatcher.forward(request, response);
                        } else {
                            session.setAttribute("email", utente.getEmail());
                            session.setAttribute("tipo", utente.getTipoUtente());
                            response.sendRedirect("./index.jsp");
                        }
                    }
                } else if (action.equalsIgnoreCase("logout")) {
                    request.getSession().invalidate();
                    response.sendRedirect("./index.jsp");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
