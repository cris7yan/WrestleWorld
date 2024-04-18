package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.UtenteBean;
import it.unisa.wrestleworld.model.UtenteModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class LoginControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginControl () {
        super();
    }

    /**
     * funzione che gestisce l'operazione di login di un utente
     * @param request
     * @param response
     */
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // preleviamo dalla request l'email e la password dell'utente
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UtenteBean utente = new UtenteBean();
        UtenteModel utModel = new UtenteModel();

        // preleviamo l'azione dalla request
        String action = request.getParameter("action");

        try {

            if(action != null) {
                // se l'action è la login
                if(action.equalsIgnoreCase("login")) {
                    // recuperiamo l'utente dal database mediante l'email presa dalla request
                    utente = utModel.doRetriveByEmail(email);

                    // verifichiamo se l'utente esiste nel database
                    if(utente.getEmail().equalsIgnoreCase("") || utente.getEmail() == null) {
                        // se non esiste, viene reinderizzato di nuovo alla pagina di login
                        response.sendRedirect("login.jsp");
                    } else {

                        if(utente != null) {
                            if(utente.getEmail().equalsIgnoreCase(email)) {

                                // se esiste, verifichiamo se l'email è di un utente o di un admin
                                if(utente.getPassword().equalsIgnoreCase(password)) {
                                    if(utente.getTipoUtente().equalsIgnoreCase("Utente")) {
                                        // effettuata per gestire lo stato dell'utente dopo il login
                                        request.getSession().setAttribute("UtenteLoggato", utente);
                                        response.sendRedirect("./catalogo.jsp");
                                    }

                                    if(utente.getTipoUtente().equalsIgnoreCase("Admin")) {
                                        // effettuata per gestire lo stato dell'utente dopo il login
                                        request.getSession().setAttribute("AdminLoggato", utente);
                                        response.sendRedirect("./catalogo.jsp");
                                    }
                                } else {
                                    response.sendRedirect("./login.jsp");
                                }
                            } else {
                                response.sendRedirect("./login.jsp");
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp");
        }
    }

    /**
     * funzione che si occupa di gestire e delegare la gestione della richiesta alla doPost
     * @param request
     * @param response
     */
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
