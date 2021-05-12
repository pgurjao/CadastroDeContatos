package br.edu.infnet.app;

import br.edu.infnet.infra.ContatoRepository;
import java.io.IOException;
//import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ExcluirContatoController", urlPatterns = {"/ExcluirContato"})
public class ExcluirContatoController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<String> erros = new ArrayList<>();
        int idParaDeletar = -1;
//        Contato contato = new Contato();

// 1 - OBTER OS DADOS DO FORMULARIO
// 2 - VALIDAR DADOS
        try {
            idParaDeletar = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            System.out.println("[ExcluirContatoController] NumberFormatException parsing id");
            erros.add("[ExcluirContato] NumberFormatException parsing id");
        }
        if(idParaDeletar == -1) {
            erros.add("[ExcluirContato] id informado eh invalido");
        }

// 3 - EXECUTAR O PROCESSAMENTO
        String erroDb = null;
        ContatoRepository cr = new ContatoRepository();
        try {
            cr.excluirPorId(idParaDeletar);
        } catch (Exception e) {
            System.out.println("[ExcluirContatoController] Exception ao excluir contato na DB");
            erroDb = e.getMessage();
// 4 - COLOCAR DADOS NA REQUISICAO
            request.setAttribute("erroDb", erroDb);
            request.setAttribute("erros", erros);
        }

// 5 - REDIRECIONAR
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
