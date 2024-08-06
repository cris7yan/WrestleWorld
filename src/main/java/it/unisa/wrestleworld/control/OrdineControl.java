package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.*;
import it.unisa.wrestleworld.util.Carrello;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/OrdineControl")
public class OrdineControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final Logger logger = Logger.getLogger(OrdineControl.class.getName());

    private static final String EMAIL_PARAM = "email";
    private static final String ERROR_PARAM = "error";
    private static String fatturePath = "fatture";
    private static String templateFatturePath = "templatefattura";

    private static final String ERROR_PAGE = "/pageError.jsp";

    static OrdineDAO ordineModel = new OrdineModel();
    static ProdottoDAO prodModel = new ProdottoModel();
    static IndirizzoDAO indirizzoModel = new IndirizzoModel();
    static MetodoPagamentoDAO metodoPagamentoModel = new MetodoPagamentoModel();
    static UtenteDAO utModel = new UtenteModel();

    private static final String ERROR_MESSAGE = "Si è verificato un errore: ";
    private static final String ERROR_MESSAGE_PAGE_ERROR = "Errore durante il reindirizzamento alla pagina di errore";

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
                    case "checkout":
                        checkout(request, response);
                        break;
                    case "generaFattura":
                        generaFattura(request, response);
                        break;
                    default:
                        RequestDispatcher errorDispatcher = getServletContext().getRequestDispatcher(ERROR_PAGE);
                        errorDispatcher.forward(request, response);
                        break;
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
     * funzione che si occupa di recuperare tutti gli ordini di un utente
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaOrdini (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute(EMAIL_PARAM);
            List<OrdineBean> ordini = ordineModel.doRetrieveAllByEmail(email);

            request.setAttribute("ordini", ordini);
            RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/ordini.jsp");
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
     * funzione che permette la visualizzazione dei metodi di pagamento e degli indirizzi di un determinato utente nella fase di acquisto
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void visualizzaDatiUtente (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute(EMAIL_PARAM);

            if (email == null || email.isEmpty()) {
                response.sendRedirect("login.jsp");
                return;
            }

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
     * funzione che gestisce l'operazione di effettuazione di un'ordine
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void checkout (HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute(EMAIL_PARAM);
            Carrello carrello = (Carrello) session.getAttribute("carrello");

            LocalDate oggi = LocalDate.now();
            Date dataOggi = Date.valueOf(oggi);
            ordineModel.doSave(dataOggi, carrello.getPrezzoCarrello(), email);

            int idNuovoOrdine = ordineModel.doRetrieveLastOrdineID();
            List<ProdottoBean> prodottiCarrello = carrello.getCarrello();
            for (ProdottoBean prod : prodottiCarrello) {
                String taglia = prod.getTagliaSelezionata();
                int quantita = prod.getQuantitaCarrello();
                float prezzo;

                if (prod.getPrezzoOffertaProdotto() > 0 && prod.getPrezzoOffertaProdotto() < prod.getPrezzoProdotto()) {
                    prezzo = prod.getPrezzoOffertaProdotto();
                } else {
                    prezzo = prod.getPrezzoProdotto();
                }

                ordineModel.doUpdateComprendeOrdine(idNuovoOrdine, prod.getIDProdotto(), taglia, quantita, prezzo);
                prodModel.doDecreaseProductQuantity(prod.getIDProdotto(), taglia, quantita);
            }

            carrello.svuotaCarrello();

            RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/index.jsp");
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
     * funzione che genera una fattura di un ordine ad un utente
     * @param request
     * @param response
     */
    private void generaFattura(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("IdOrdine"));
            String email = ordineModel.doRetrieveOrdineByOrderId(id).getUtenteOrdine().getEmail();

            String nomeCognome = utModel.doRetrieveByEmail(email).getNome() + " " + utModel.doRetrieveByEmail(email).getCognome();
            Date data = ordineModel.doRetrieveOrdineByOrderId(id).getDataOrdine();

            List<ProdottoBean> prodotti = ordineModel.doRetrieveOrdineByID(id);
            int limit = 21;
            int numProd = 0;

            String servletPath = request.getServletContext().getRealPath("");
            String totalPath = servletPath + File.separator  + fatturePath + File.separator  + "WrestleWorldFattura" + id + ".pdf";

            File file = new File(servletPath + templateFatturePath + File.separator  + "WrestleWordFatturapagina1.pdf");
            PDDocument fattura = PDDocument.load(file);
            PDPage page = fattura.getDocumentCatalog().getPages().get(0);
            PDPageContentStream contentStream = new PDPageContentStream(fattura, page, PDPageContentStream.AppendMode.APPEND, true, true);
            PDType1Font font = PDType1Font.TIMES_ROMAN;

            //Coordinate

            //Nome e cognome cliente
            float coordinataX1 = 422;
            float coordinataY1 = 718.50f;

            //Numero fattura
            float coordinataX2 = 450;
            float coordinataY2 = 761.55f;

            //Data ordine
            float coordinataX3 = 450;
            float coordinataY3 = 749.5f;

            //Prodotto
            float coordinataProdottoX = 83;
            float coordinataProdottoY = 611.8f;

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(coordinataX2, coordinataY2);
            contentStream.showText(String.valueOf(id));
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(coordinataX3, coordinataY3);
            contentStream.showText(String.valueOf(data));
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(coordinataX1, coordinataY1);
            contentStream.showText(nomeCognome);
            contentStream.endText();

            int quantita = 0;
            float prezzo = 0;
            float prezzoTotale = 0;
            String descrizione = "";

            float prezzoSpesa = 0;

            for(ProdottoBean prod : prodotti) {
                numProd++;
                if(numProd > limit ) {
                    file = new File(servletPath + templateFatturePath +  File.separator  + "WrestleWordFatturapagina2.pdf");
                    page = PDDocument.load(file).getDocumentCatalog().getPages().get(0);

                    fattura.addPage(page);

                    contentStream.close();

                    coordinataProdottoY = 731.7f;
                    contentStream = new PDPageContentStream(fattura, page, PDPageContentStream.AppendMode.APPEND, true, true);
                    numProd = 1;
                    limit = 26;
                }

                //Definisci il prodotto da scrivere

                quantita = quantitaForProd(prod);
                prezzo = prezzoForProd(prod, id);
                prezzoTotale = prezzoTotaleForProd(prod, id);
                descrizione = getNomeProd(prod);
                prezzoSpesa += prezzoTotale;

                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(coordinataProdottoX, coordinataProdottoY);
                contentStream.showText(String.valueOf(quantita));
                contentStream.endText();
                coordinataProdottoX = 126.5f;

                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(coordinataProdottoX, coordinataProdottoY);
                contentStream.showText(descrizione);
                contentStream.endText();
                coordinataProdottoX = 360;

                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(coordinataProdottoX, coordinataProdottoY);
                contentStream.showText(String.valueOf(prezzo + " €"));
                contentStream.endText();
                coordinataProdottoX = 455.5f;

                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(coordinataProdottoX, coordinataProdottoY);
                contentStream.showText(String.valueOf(prezzoTotale + " €"));
                contentStream.endText();

                coordinataProdottoX = 83;
                coordinataProdottoY = coordinataProdottoY - 24.9f;

            }

            coordinataProdottoX = 360;
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.setNonStrokingColor(new Color(255, 0, 0));
            contentStream.newLineAtOffset(coordinataProdottoX, coordinataProdottoY);
            contentStream.showText(String.valueOf("TOTALE"));
            contentStream.endText();

            coordinataProdottoX = 455.5f;

            Locale.setDefault(Locale.US);
            String prezzoArrotondato;
            Locale.setDefault(Locale.ITALY);

            prezzoSpesa = prezzoSpesa + 5.0f; //spedizione

            prezzoArrotondato = String.format("%.2f", prezzoSpesa);

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.setNonStrokingColor(new Color(255, 0, 0));
            contentStream.newLineAtOffset(coordinataProdottoX, coordinataProdottoY);
            contentStream.showText(String.valueOf(prezzoArrotondato + " €"));
            contentStream.endText();

            contentStream.close();
            fattura.save(totalPath);
            fattura.close();

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(new File(totalPath));
                }
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("ordini.jsp");
            dispatcher.forward(request, response);
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

    // Metodi utili per la generazione della fattura

    /**
     * restituisce la quantità del prodotto acquistato
     * @param prod
     * @return
     */
    private int quantitaForProd(ProdottoBean prod) {
        return prod.getQuantitaCarrello();
    }

    /**
     * restituisce il prezzo del prodotto acquistato
     * @param prod
     * @return
     */
    private float prezzoForProd(ProdottoBean prod, int id) throws SQLException {
        return prodModel.doRetrievePrezzoOrdine(prod.getIDProdotto(), id);
    }

    /**
     * restituisce il prezzo totale del prodotto per la quantità acquistato
     * @param prod
     * @return
     */
    private float prezzoTotaleForProd(ProdottoBean prod, int id) throws SQLException {
        return prodModel.doRetrievePrezzoOrdine(prod.getIDProdotto(), id) * prod.getQuantitaCarrello();
    }

    /**
     * restituisce il nome del prodotto acquistato
     * @param prod
     * @return
     */
    private String getNomeProd (ProdottoBean prod) {
        return prod.getNomeProdotto();
    }

}