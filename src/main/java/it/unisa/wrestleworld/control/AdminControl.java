package it.unisa.wrestleworld.control;

import it.unisa.wrestleworld.model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/AdminControl")
@MultipartConfig
public class AdminControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final Logger logger = Logger.getLogger(AdminControl.class.getName());

    private static UtenteDAO utModel = new UtenteModel();
    private static OrdineDAO ordineModel = new OrdineModel();
    private static ProdottoDAO prodModel = new ProdottoModel();
    private static CategoriaDAO catModel = new CategoriaModel();
    private static TagliaProdottoDAO tagliaModel = new TagliaProdottoModel();

    private static final String ID_PROD_PARAM = "IDProd";
    private static final String APPLICATION_JSON_PARAM = "application/json";

    private static final String MSG_ERROR_DOPOST = "Errore durante l'esecuzione di doPost";
    private static final String MSG_ERROR_FORWARD = "Errore durante il forward della richiesta";
    private static final String MSG_ERROR_NUMBER = "ID o quantità non validi";


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
                    case "rendiDisponibileProdotto":
                        rendiDisponibileProdotto(request, response);
                        break;
                    case "creaNuovoProdotto":
                        creaNuovoProdotto(request, response);
                        break;
                    case "creaNuovaCategoria":
                        creaNuovaCategoria(request, response);
                        break;
                    case "aggiungiTagliaProdotto":
                        aggiungiTagliaProdotto(request, response);
                        break;
                    case "modificaPrezzo":
                        modificaPrezzo(request, response);
                        break;
                    case "modificaPrezzoOfferta":
                        modificaPrezzoOfferta(request, response);
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
            String idProdStr = request.getParameter(ID_PROD_PARAM);
            String taglia = request.getParameter("taglia");
            String quantitaStr = request.getParameter("quantita");

            int idProd = Integer.parseInt(idProdStr);
            int quantita = Integer.parseInt(quantitaStr);

            prodModel.addQuantityProduct(idProd, taglia, quantita);

            response.setContentType(APPLICATION_JSON_PARAM);
            PrintWriter out = response.getWriter();
            out.print("{\"message\": \"Quantità aggiornata con successo\"}");
            out.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, MSG_ERROR_NUMBER);
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
            String idProdStr = request.getParameter(ID_PROD_PARAM);

            int idProd = Integer.parseInt(idProdStr);

            prodModel.doDeleteProduct(idProd);

            response.setContentType(APPLICATION_JSON_PARAM);
            PrintWriter out = response.getWriter();
            out.print("{\"message\": \"Prodotto eliminato con successo\"}");
            out.flush();

            response.sendRedirect("catalogo.jsp");
        } catch (IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, MSG_ERROR_NUMBER);
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
            String idProdStr = request.getParameter(ID_PROD_PARAM);

            int idProd = Integer.parseInt(idProdStr);

            prodModel.makeProductUnavailable(idProd);

            response.setContentType(APPLICATION_JSON_PARAM);
            PrintWriter out = response.getWriter();
            out.print("{\"message\": \"Prodotto reso indisponibile con successo\"}");
            out.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, MSG_ERROR_NUMBER);
        }
    }


    /**
     * funzione che permette ad un admin di aggiornare la disponibilità di un prodotto
     * @param request
     * @param response
     * @throws IOException
     */
    private void rendiDisponibileProdotto (HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idProdStr = request.getParameter(ID_PROD_PARAM);

            int idProd = Integer.parseInt(idProdStr);

            prodModel.makeProductAvailable(idProd);

            response.setContentType(APPLICATION_JSON_PARAM);
            PrintWriter out = response.getWriter();
            out.print("{\"message\": \"Prodotto reso disponibile con successo\"}");
            out.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, MSG_ERROR_FORWARD, e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, MSG_ERROR_NUMBER);
        }
    }


    /**
     * funzione che permette ad un admin di creare un nuovo prodotto
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    private void creaNuovoProdotto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");  // Imposta la codifica della richiesta a UTF-8
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Preleva i dati dal form
            String nome = request.getParameter("nome");
            String descrizione = request.getParameter("descrizione");
            String materiale = request.getParameter("materiale");
            String marca = request.getParameter("marca");
            String modello = request.getParameter("modello");
            BigDecimal prezzoBigDecimal = new BigDecimal(request.getParameter("prezzo"));
            BigDecimal prezzoOffertaBigDecimal = new BigDecimal(request.getParameter("prezzo_offerta"));
            boolean disponibilita = Boolean.parseBoolean(request.getParameter("disponibilita"));

            // Preleva le immagini
            List<String> immagini = new ArrayList<>();
            for (Part part : request.getParts()) {
                if ("immagini".equals(part.getName())) {
                    String fileName = extractFileName(part);
                    if (!fileName.isEmpty()) {
                        String filePath = getServletContext().getRealPath("/") + "img/" + "prodotti/" + fileName;
                        salvaImmagine(filePath, part, response);
                        immagini.add(fileName);
                    }
                }
            }

            // Preleva le taglie
            String[] taglieArray = request.getParameterValues("taglie");
            String[] quantitaArray = request.getParameterValues("quantita");
            List<TagliaProdottoBean> taglie = new ArrayList<>();
            if (taglieArray != null && quantitaArray != null && taglieArray.length == quantitaArray.length) {
                for (int i = 0; i < taglieArray.length; i++) {
                    TagliaProdottoBean taglia = new TagliaProdottoBean();
                    taglia.setTaglia(taglieArray[i]);
                    taglia.setQuantita(Integer.parseInt(quantitaArray[i]));
                    taglie.add(taglia);
                }
            }

            // Preleva le categorie
            List<CategoriaBean> categorie = new ArrayList<>();

            String sesso = request.getParameter("sesso");
            if (sesso != null) {
                CategoriaBean categoria = new CategoriaBean();
                categoria.setNome(sesso);
                categorie.add(categoria);
            }

            String[] superstarArray = request.getParameterValues("superstar");
            if (superstarArray != null) {
                for (String superstar : superstarArray) {
                    CategoriaBean categoria = new CategoriaBean();
                    categoria.setNome(superstar);
                    categorie.add(categoria);
                }
            }

            String ple = request.getParameter("ple");
            if (ple != null) {
                CategoriaBean categoria = new CategoriaBean();
                categoria.setNome(ple);
                categorie.add(categoria);
            }

            String titleBelts = request.getParameter("title_belts");
            if (titleBelts != null) {
                CategoriaBean categoria = new CategoriaBean();
                categoria.setNome(titleBelts);
                categorie.add(categoria);
            }

            String abbigliamento = request.getParameter("abbigliamento");
            if (abbigliamento != null) {
                CategoriaBean categoria = new CategoriaBean();
                categoria.setNome(abbigliamento);
                categorie.add(categoria);
            }

            String accessori = request.getParameter("accessori");
            if (accessori != null) {
                CategoriaBean categoria = new CategoriaBean();
                categoria.setNome(accessori);
                categorie.add(categoria);
            }

            String[] oggettiDaCollezioneArray = request.getParameterValues("oggetti_da_collezione");
            if (oggettiDaCollezioneArray != null) {
                for (String oggetto : oggettiDaCollezioneArray) {
                    CategoriaBean categoria = new CategoriaBean();
                    categoria.setNome(oggetto);
                    categorie.add(categoria);
                }
            }

            float prezzo = prezzoBigDecimal.floatValue();
            float prezzoOfferta = prezzoOffertaBigDecimal.floatValue();

            // Crea l'oggetto ProdottoBean
            ProdottoBean prodotto = new ProdottoBean();
            prodotto.setNomeProdotto(nome);
            prodotto.setDescrizioneProdotto(descrizione);
            prodotto.setMaterialeProdotto(materiale);
            prodotto.setMarcaProdotto(marca);
            prodotto.setModelloProdotto(modello);
            prodotto.setPrezzoProdotto(prezzo);
            prodotto.setPrezzoOffertaProdotto(prezzoOfferta);
            prodotto.setDisponibilitaProdotto(disponibilita);

            // Salva il prodotto nel database
            prodModel.doSaveProduct(prodotto, immagini, taglie, categorie);

            response.sendRedirect("catalogo.jsp");
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, "Errore nella creazione del prodotto", e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Errore nel database", e);
        }
    }


    /**
     * funzione che permette ad un admin di creare una nuova categoria
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void creaNuovaCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");  // Imposta la codifica della richiesta a UTF-8
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Preleva i dati dal form
            String tipoCategoria = request.getParameter("tipo_categoria");
            String nomeCategoria = request.getParameter("nome_categoria");

            // Preleva l'immagine della categoria
            Part part = request.getPart("immagine_categoria");
            String fileName = extractFileName(part);
            String filePath = "";
            if (!fileName.isEmpty()) {
                filePath = getServletContext().getRealPath("/") + "img/" + "categorie/" + fileName;
                salvaImmagine(filePath, part, response);
            }

            // Crea l'oggetto CategoriaBean
            CategoriaBean categoria = new CategoriaBean();
            categoria.setTipo(tipoCategoria);
            categoria.setNome(nomeCategoria);
            categoria.setImg(fileName);

            // Salva la categoria nel database
            catModel.doSaveCategory(categoria);

            response.sendRedirect("CategoriaControl?action=visualizzaCategorie");
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, "Errore nella creazione della categoria", e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Errore nel database", e);
        }
    }


    /**
     * Estrae il nome del file dal part
     * @param part
     * @return
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                String fileName = s.substring(s.indexOf("=") + 2, s.length() - 1).replace("\"", "");
                System.out.println("File Name: " + fileName);  // Aggiungi log
                return fileName;
            }
        }
        return "";
    }


    /**
     * Salva l'immagine nella posizione specificata
     * @param path2
     * @param imgFile
     * @param response
     * @throws IOException
     */
    private void salvaImmagine(String path2, Part imgFile, HttpServletResponse response) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path2);
             InputStream is = imgFile.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore nel salvataggio dell'immagine", e);
            throw e; // Rilancia l'eccezione se necessario
        }
    }


    /**
     * funzione che permette ad un admin di salvare una nuova taglia ad un prodotto
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void aggiungiTagliaProdotto (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idProdotto = Integer.parseInt(request.getParameter(ID_PROD_PARAM));
            String taglia = request.getParameter("taglia");
            int quantita = Integer.parseInt(request.getParameter("quantita"));

            TagliaProdottoBean nuovaTaglia = new TagliaProdottoBean();
            nuovaTaglia.setIdProdotto(idProdotto);
            nuovaTaglia.setTaglia(taglia);
            nuovaTaglia.setQuantita(quantita);

            tagliaModel.doSaveTagliaProdotto(nuovaTaglia);

            response.setContentType(APPLICATION_JSON_PARAM);
            PrintWriter out = response.getWriter();
            out.print("{\"message\": \"Taglia aggiunta con successo al prodotto.\"}");
            out.flush();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, MSG_ERROR_NUMBER);
        }
    }


    /**
     * funzione che permette ad un admin di modificare il prezzo ad un prodotto
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void modificaPrezzo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idProdotto = Integer.parseInt(request.getParameter(ID_PROD_PARAM));
            float nuovoPrezzo = Float.parseFloat(request.getParameter("prezzo"));

            prodModel.doUpdateProductPrice(idProdotto, nuovoPrezzo);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"message\": \"Prezzo aggiornato con successo.\"}");
            out.flush();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante l'aggiornamento del prezzo.");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri non validi.");
        }
    }


    /**
     * funzione che permette ad un admin di modificare il prezzo di offerta ad un prodotto
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void modificaPrezzoOfferta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idProdotto = Integer.parseInt(request.getParameter(ID_PROD_PARAM));
            float nuovoPrezzoOfferta = Float.parseFloat(request.getParameter("prezzoOfferta"));

            prodModel.doUpdateProductOfferPrice(idProdotto, nuovoPrezzoOfferta);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"message\": \"Prezzo offerta aggiornato con successo.\"}");
            out.flush();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante l'aggiornamento del prezzo offerta.");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri non validi.");
        }
    }

}
