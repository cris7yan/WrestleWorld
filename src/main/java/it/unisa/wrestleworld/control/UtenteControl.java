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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;

@WebServlet("/UtenteControl")
public class UtenteControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final Logger logger = Logger.getLogger(UtenteControl.class.getName());

    static UtenteDAO utModel = new UtenteModel();
    static IndirizzoDAO indirizzoModel = new IndirizzoModel();
    static MetodoPagamentoDAO metodoPagamentoModel = new MetodoPagamentoModel();

    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";
    private static final String NOME_PARAM = "nome";
    private static final String COGNOME_PARAM = "cognome";
    private static final String DATA_NASCITA_PARAM = "dataNascita";
    private static final String ERROR_PARAM = "error";

    private static final String ERROR_PAGE = "/page500.jsp";
    private static final String INDEX_PAGE = "./index.jsp";

    private static final String ERROR_MESSAGE = "Si è verificato un errore: ";
    private static final String ERROR_MESSAGE_PAGE_ERROR = "Errore durante il reindirizzamento alla pagina di errore";


    public UtenteControl() {
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
            if (action != null) {
                switch (action) {
                    case "login":
                        login(request, response);
                        break;
                    case "logout":
                        logout(request, response);
                        break;
                    case "registrazione":
                        registrazione(request, response);
                        break;
                    case "verificaEmail":
                        verificaEmail(request, response);
                        break;
                    case "verificaUtenteRegistrato":
                        verificaUtenteRegistrato(request, response);
                        break;
                    case "modificaDati":
                        modificaDatiPersonali(request, response);
                        break;
                    case "modificaDatiAccesso":
                        modificaDatiAccesso(request, response);
                        break;
                    case "visualizzaIndirizzi":
                        visualizzaIndirizzi(request, response);
                        break;
                    case "rimuoviIndirizzo":
                        eliminaIndirizzo(request, response);
                        break;
                    case "aggiungiIndirizzo":
                        aggiungiIndirizzo(request, response);
                        break;
                    case "visualizzaMetodiPagamento":
                        visualizzaMetodiPagamento(request, response);
                        break;
                    case "rimuoviMetodoPagamento":
                        eliminaMetodoPagamento(request, response);
                        break;
                    case "aggiungiMetodoPagamento":
                        aggiungiMetodoPagamento(request, response);
                        break;
                    default:
                        RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
                        errorDispatcher.forward(request, response);
                        break;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (ServletException | IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
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
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        }
    }


    /**
     * funzione che gestisce l'operazione di login di un utente
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // preleviamo dalla request i valori di email e password
            String email = request.getParameter(EMAIL_PARAM);
            String password = request.getParameter(PASSWORD_PARAM);

            HttpSession session = request.getSession(true);

            if (email == null || password == null) {
                response.sendRedirect(INDEX_PAGE);
            } else {
                UtenteBean utente = utModel.doRetrieveByEmailPassword(email, password);
                if (utente == null) {
                    RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/login.jsp");
                    reqDispatcher.forward(request, response);
                } else {
                    session.setAttribute(EMAIL_PARAM, utente.getEmail());
                    session.setAttribute(NOME_PARAM, utente.getNome());
                    session.setAttribute(COGNOME_PARAM, utente.getCognome());
                    session.setAttribute(DATA_NASCITA_PARAM, utente.getDataNascita());
                    session.setAttribute("tipo", utente.getTipoUtente());

                    if ("Admin".equals(utente.getTipoUtente())) {
                        response.sendRedirect("catalogo.jsp");
                    } else {
                        response.sendRedirect(INDEX_PAGE);
                    }
                }
            }
        } catch (ServletException | IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
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
    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        try {
            response.sendRedirect(INDEX_PAGE);
        } catch (IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
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
    private void registrazione(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            // preleviamo i dati dalla request
            String email = request.getParameter(EMAIL_PARAM);
            String password = request.getParameter(PASSWORD_PARAM);
            String nome = request.getParameter(NOME_PARAM);
            String cognome = request.getParameter(COGNOME_PARAM);
            Date dataNascita = Date.valueOf(request.getParameter(DATA_NASCITA_PARAM));

            // creiamo un nuovo utente ed impostiamo i dati
            UtenteBean newUtente = new UtenteBean();
            newUtente.setEmail(email);
            newUtente.setPassword(password);
            newUtente.setNome(nome);
            newUtente.setCognome(cognome);
            newUtente.setDataNascita(dataNascita);
            newUtente.setTipoUtente("Utente");

            HttpSession session = request.getSession(true);

            if (utModel.verificaEmailEsistente(email)) {
                response.sendRedirect("login.jsp?error=registration"); // Redirect con parametro di errore
            } else {
                utModel.doSave(newUtente);
                session.setAttribute(EMAIL_PARAM, newUtente.getEmail());
                session.setAttribute("tipo", newUtente.getTipoUtente());
                response.sendRedirect(INDEX_PAGE);
            }

            UtenteBean utente = utModel.doRetrieveByEmail(email);    // Recupera i dati aggiunti dal database

            // Aggiunge i dati nella sessione
            session.setAttribute(NOME_PARAM, utente.getNome());
            session.setAttribute(COGNOME_PARAM, utente.getCognome());
            session.setAttribute(DATA_NASCITA_PARAM, utente.getDataNascita());
        } catch (IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce l'operazione di verifica di un email presente nel database
     * @param request
     * @param response
     * @throws IOException
     */
    private void verificaEmail (HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String email = request.getParameter(EMAIL_PARAM);
            boolean exist = utModel.verificaEmailEsistente(email);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            if (exist) {
                out.print("Email verificata");
            } else {
                out.print("Email non trovata");
            }
            out.flush();
        } catch (IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce la verifica se i dati inseriti nel form login sono presenti nel database
     * @param request
     * @param response
     * @throws SQLException
     * @throws ServletException
     * @throws IOException
     */
    private void verificaUtenteRegistrato (HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String email = request.getParameter(EMAIL_PARAM);
            String password = request.getParameter(PASSWORD_PARAM);
            UtenteBean utente = utModel.doRetrieveByEmailPassword(email, password);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            if(utente == null) {
                PrintWriter out = response.getWriter();
                out.print("Utente non esistente");
                out.flush();
            } else {
                PrintWriter out = response.getWriter();
                out.print("Utente verificato");
                out.flush();
            }
        } catch (IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }

    }


    /**
     * funzione che gestisce l'operazione della modifica dei dati personali di un utente
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     */
    private void modificaDatiPersonali(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute(EMAIL_PARAM);

            String nome = request.getParameter(NOME_PARAM);
            String cognome = request.getParameter(COGNOME_PARAM);
            Date data = Date.valueOf(request.getParameter(DATA_NASCITA_PARAM));

            utModel.doUpdateData(nome, cognome, data, email);   // Aggiorna i dati nel database

            UtenteBean updatedUtente = utModel.doRetrieveByEmail(email);    // Recupera i dati aggiornati dal database

            // Aggiorna i dati nella sessione
            session.setAttribute(NOME_PARAM, updatedUtente.getNome());
            session.setAttribute(COGNOME_PARAM, updatedUtente.getCognome());
            session.setAttribute(DATA_NASCITA_PARAM, updatedUtente.getDataNascita());

            // Reindirizza alla pagina del profilo
            response.sendRedirect("./profiloUtente.jsp");
        } catch (IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce l'operazione della modifica dei dati di accesso di un utente
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     */
    private void modificaDatiAccesso (HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute(EMAIL_PARAM);

            String newEmail = request.getParameter(EMAIL_PARAM);
            String newPassword = request.getParameter(PASSWORD_PARAM);

            utModel.doUpdateEmail(email, newEmail);
            utModel.doUpdatePassword(email, newPassword);

            UtenteBean updatedUtente = utModel.doRetrieveByEmail(newEmail);

            session.setAttribute(EMAIL_PARAM, updatedUtente.getEmail());
            session.setAttribute(PASSWORD_PARAM, updatedUtente.getPassword());

            // Reindirizza alla pagina del profilo
            response.sendRedirect("./profiloUtente.jsp");
        } catch (IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce la visualizzazione degli indirizzi per ogni utente
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     */
    private void visualizzaIndirizzi(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute(EMAIL_PARAM);

            List<Integer> idList = indirizzoModel.doRetrieveAllID(email);       // lista di tutti gli ID degli indirizzi di un utente

            List<IndirizzoBean> allIndirizzi = new ArrayList<>();       // lista di tutti gli indirizzi di un utente
            for (int ind : idList) {
                allIndirizzi.add(indirizzoModel.doRetrieveByID(ind));     // aggiungiamo in questa lista tutti gli indirizzi relativi agli id
            }

            request.setAttribute("indirizzi", allIndirizzi);
            RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/indirizzi.jsp");
            reqDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce il salvataggio di un nuovo indirizzo
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     */
    private void aggiungiIndirizzo(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute(EMAIL_PARAM);

            IndirizzoBean indirizzo = new IndirizzoBean();

            String via = request.getParameter("via");
            String citta = request.getParameter("citta");
            String provincia = request.getParameter("provincia");
            String cap = request.getParameter("cap");
            String nomeCompleto = request.getParameter("nomeCompleto");

            indirizzo.setViaIndirizzo(via);
            indirizzo.setCittaIndirizzo(citta);
            indirizzo.setProvinciaIndirizzo(provincia);
            indirizzo.setCAPIndirizzo(cap);
            indirizzo.setNomeCompletoIndirizzo(nomeCompleto);

            UtenteBean utente = utModel.doRetrieveByEmail(email);

            indirizzoModel.doSave(indirizzo, utente);
            response.sendRedirect("./indirizzi.jsp");
        } catch (IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce l'operazione di eliminazione di un indirizzo
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     */
    private void eliminaIndirizzo(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            int idIndirizzo = Integer.parseInt(request.getParameter("ID_Indirizzo"));
            indirizzoModel.doDelete(idIndirizzo);
            response.sendRedirect("./indirizzi.jsp");
        } catch (IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce la visualizzazione degli indirizzi per ogni utente
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     */
    private void visualizzaMetodiPagamento(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute(EMAIL_PARAM);

            List<Integer> idList = metodoPagamentoModel.doRetrieveAllID(email);       // lista di tutti gli ID dei metodi di pagamento di un utente

            List<MetodoPagamentoBean> allMetodi = new ArrayList<>();       // lista di tutti i metodi di pagamento di un utente
            for (int ind : idList) {
                allMetodi.add(metodoPagamentoModel.doRetrieveByID(ind));     // aggiungiamo in questa lista tutti i metodi di pagamento relativi agli id
            }

            request.setAttribute("metodiPagamento", allMetodi);
            RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/metodiPagamento.jsp");
            reqDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce il salvataggio di un nuovo indirizzo
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     */
    private void aggiungiMetodoPagamento(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute(EMAIL_PARAM);

            MetodoPagamentoBean metodo = new MetodoPagamentoBean();

            String numeroCarta = request.getParameter("numeroCarta");
            String intestatario = request.getParameter("intestatario");
            Date dataScadenza = Date.valueOf(request.getParameter("dataScadenza"));

            metodo.setNumeroCarta(numeroCarta);
            metodo.setIntestatario(intestatario);
            metodo.setDataScadenza(dataScadenza);

            UtenteBean utente = utModel.doRetrieveByEmail(email);

            metodoPagamentoModel.doSave(metodo, utente);
            response.sendRedirect("./metodiPagamento.jsp");
        } catch (IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }


    /**
     * funzione che gestisce l'operazione di eliminazione di un indirizzo
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     */
    private void eliminaMetodoPagamento(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            int idMetodo = Integer.parseInt(request.getParameter("ID_Pagamento"));
            metodoPagamentoModel.doDelete(idMetodo);
            response.sendRedirect("./metodiPagamento.jsp");
        } catch (IOException e) {
            request.setAttribute(ERROR_PARAM, ERROR_MESSAGE + e);
            RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
            try {
                errorDispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                log(ERROR_MESSAGE_PAGE_ERROR, ex);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

}
