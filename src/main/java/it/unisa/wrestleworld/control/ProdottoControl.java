package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.*;
import it.unisa.wrestleworld.util.Carrello;

import com.google.gson.Gson;
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

@WebServlet("/ProdottoControl")
public class ProdottoControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final Logger logger = Logger.getLogger(ProdottoControl.class.getName());

    private static ProdottoDAO prodModel = new ProdottoModel();
    private static TagliaProdottoDAO taglieModel = new TagliaProdottoModel();
    private static Carrello carrelloBean = new Carrello();

    private static final String CARRELLO_PARAM = "carrello";
    private static final String ID_PROD_PARAM = "IDProd";
    private static final String RICERCA_PARAM = "ricerca";
    private static final String ERROR_PARAM = "error";

    private static final String ERROR_PAGE = "/page500.jsp";

    private static final String MSG_ERROR_RECUPERO_DATI_HOMEPAGE = "Errore durante il recupero dei dati per la HomePage ";
    private static final String ERROR_MESSAGE = "Si è verificato un errore: ";
    private static final String ERROR_MESSAGE_PAGE_ERROR = "Errore durante il reindirizzamento alla pagina di errore";

    public ProdottoControl () {
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
        // preleviamo l'azione dalla request
        String action = request.getParameter("action");

        // gestione carrello di un utente
        Carrello carrelloUtente = (Carrello) request.getSession().getAttribute(CARRELLO_PARAM);   // preleviamo il carrello dalla sessione
        if(carrelloUtente == null) {
            // Se non esiste, ne crea uno nuovo e lo memorizza nella sessione
            carrelloUtente = new Carrello();
            request.getSession().setAttribute(CARRELLO_PARAM, carrelloUtente);
        }

        try {
            if (action != null) {
                switch (action) {
                    case "visualizzaHomePage":
                        visualizzaHomePage(request, response);
                        break;
                    case "visualizzaCatalogo":
                        visualizzaCatalogo(request, response);
                        break;
                    case "visualizzaDettagliProdotto":
                        visualizzaDettagliProdotto(request, response);
                        break;
                    case "aggiungiAlCarrello":
                        aggiungiProdottoCarrello(request, response);
                        break;
                    case "rimuoviDalCarrello":
                        rimuoviProdottoCarrello(request, response);
                        break;
                    case "suggerimentiRicerca":
                        suggerimentiProdottiRicerca(request, response);
                        break;
                    case RICERCA_PARAM:
                        ricerca(request, response);
                        break;
                    case "visualizzaProdottiCategoria":
                        visualizzaProdottiCategoria(request, response);
                        break;
                    default:
                        RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
                        errorDispatcher.forward(request, response);
                        break;
                }
            } else {
                visualizzaHomePage(request, response);
            }
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
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
     * funzione che permette la visualizzazione della homepage
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaHomePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ProdottoBean> bestSellers = recuperaBestSellers();
            request.setAttribute("bestSellers", bestSellers);

            List<String> imgBestProd = new ArrayList<>();
            for (ProdottoBean best : bestSellers) {
                imgBestProd.add(prodModel.doRetrieveAllImages(best).get(0));
            }
            request.setAttribute("imgBestProd", imgBestProd);

            List<ProdottoBean> bestOnOffer = recuperaBestOnOffer();
            request.setAttribute("bestOnOffer", bestOnOffer);

            List<String> imgBestOnOffer = new ArrayList<>();
            for (ProdottoBean offer : bestOnOffer) {
                imgBestOnOffer.add(prodModel.doRetrieveAllImages(offer).get(0));
            }
            request.setAttribute("imgBestOnOffer", imgBestOnOffer);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/index.jsp");
            reqDispatcher.forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, MSG_ERROR_RECUPERO_DATI_HOMEPAGE, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, MSG_ERROR_RECUPERO_DATI_HOMEPAGE);
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
     * funzione che recupera i bestSellers
     * @return
     * @throws SQLException
     */
    private List<ProdottoBean> recuperaBestSellers() throws SQLException {
        return prodModel.doRetrieveBestSellers();
    }

    /**
     * funzione che recupera i migliori prodotti in offerta
     * @return
     * @throws SQLException
     */
    private List<ProdottoBean> recuperaBestOnOffer() throws SQLException {
        return prodModel.doRetrieveBestOnOffer();
    }


    /**
     * funzione che gestisce la visualizzazione dei prodotti nel catalogo
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaCatalogo (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Liste per prelevare dal database i prodotti ed i prodotti piu venduti
            List<ProdottoBean> prodotti = prodModel.getProdottiOrdinatiPerCategoria();
            request.setAttribute("prodotti", prodotti);

            // prelevo le immagini dei vari prodotti
            List<String> imgProdotti = new ArrayList<>();
            for(ProdottoBean prod : prodotti) {
                imgProdotti.add(prodModel.doRetrieveAllImages(prod).get(0));
            }
            request.setAttribute("imgProdotti", imgProdotti);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/catalogo.jsp");
            reqDispatcher.forward(request, response);
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
     * funzione che gestisce la visualizzazione dei dettagli di un prodotto
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaDettagliProdotto (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idProd = Integer.parseInt(request.getParameter(ID_PROD_PARAM));
            Object prod = prodModel.doRetrieveByID(idProd);

            List<String> imgProd = prodModel.doRetrieveAllImages((ProdottoBean) prod);

            List<TagliaProdottoBean> taglieProd = taglieModel.doRetrieveAllSizeByProduct((ProdottoBean) prod);

            request.setAttribute("prodotto", prod);
            request.setAttribute("imgProd", imgProd);
            request.setAttribute("taglieProd", taglieProd);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/dettagliProdotto.jsp");
            reqDispatcher.forward(request, response);
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
     * funzione che gestisce l'operazione di aggiunta di un prodotto al carrello
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void aggiungiProdottoCarrello (HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idProd = Integer.parseInt(request.getParameter(ID_PROD_PARAM));
            String taglia = request.getParameter("taglia");
            if(prodModel.checkProductAvailability(idProd)) {
                ProdottoBean prodotto = prodModel.doRetrieveByID(idProd);
                prodotto.setTagliaSelezionata(taglia);
                carrelloBean.aggiungiProdottoCarrello(prodotto, taglia);
            }
            List<ProdottoBean> cart = carrelloBean.getCarrello();
            request.getSession().setAttribute(CARRELLO_PARAM, carrelloBean);
            request.setAttribute(CARRELLO_PARAM, cart);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String jsonCart = gson.toJson(cart);
            out.print(jsonCart);
            out.flush();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
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
     * funzione che gestisce la rimozione di un prodotto dal carrello
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void rimuoviProdottoCarrello (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idProd = Integer.parseInt(request.getParameter(ID_PROD_PARAM));
            String taglia = request.getParameter("taglia");
            carrelloBean.rimuoviProdottoCarrello(idProd, taglia);

            request.getSession().setAttribute(CARRELLO_PARAM, carrelloBean);
            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/carrello.jsp");
            reqDispatcher.forward(request, response);
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
     * funzione che gestisce l'operazione di ricerca
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void ricerca(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String nome = request.getParameter(RICERCA_PARAM);

            ProdottoBean prodotto = prodModel.doRetrieveByName(nome);
            if (prodotto != null) {
                List<String> imgProd = prodModel.doRetrieveAllImages(prodotto);

                List<TagliaProdottoBean> taglieProd = taglieModel.doRetrieveAllSizeByProduct(prodotto);

                request.setAttribute("prodotto", prodotto);
                request.setAttribute("imgProd", imgProd);
                request.setAttribute("taglieProd", taglieProd);

                RequestDispatcher reqDispatcher = request.getRequestDispatcher("/dettagliProdotto.jsp");
                reqDispatcher.forward(request, response);
            } else {
                // Gestione caso prodotto non trovato
                request.setAttribute("errorMessage", "Prodotto non trovato");
                RequestDispatcher reqDispatcher = request.getRequestDispatcher("/index.jsp");
                reqDispatcher.forward(request, response);
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
     * funzione che gestisce la ricerca tramite suggerimento dei prodotti
     * @param request
     * @param response
     * @throws IOException
     */
    private void suggerimentiProdottiRicerca (HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String ricerca = request.getParameter(RICERCA_PARAM);
            List<String> suggerimenti = new ArrayList<>();
            suggerimenti.addAll(prodModel.doRetrieveBySuggest(ricerca));

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print("{\"suggerimenti\": " + new Gson().toJson(suggerimenti) + "}");
            out.flush();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
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
     * funzione che gestisce la visualizzazione dei prodotti di una determinata categoria
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaProdottiCategoria (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String categoria = request.getParameter("categoria");
            List<ProdottoBean> prodottiCategoria = prodModel.doRetrieveByCategory(categoria);

            List<String> imgProdottiCategoria = new ArrayList<>();
            for(ProdottoBean prod : prodottiCategoria) {
                imgProdottiCategoria.add(prodModel.doRetrieveAllImages(prod).get(0));
            }
            request.setAttribute("prodottiCategoria", prodottiCategoria);
            request.setAttribute("imgProdottiCategoria", imgProdottiCategoria);

            RequestDispatcher reqDispatcher = request.getRequestDispatcher("/prodottiCategoria.jsp");
            reqDispatcher.forward(request, response);
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

}
