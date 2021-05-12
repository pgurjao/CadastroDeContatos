package br.edu.infnet.app;

import br.edu.infnet.domain.Contato;
import br.edu.infnet.infra.ContatoRepository;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

@WebServlet(name = "EditarContatoController", urlPatterns = {"/EditarContato"})
public class EditarContatoController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1 - OBTER OS DADOS DO FORMULARIO
        String erroDb = null;
        Contato contato = new Contato();
        contato.setId(Integer.parseInt(request.getParameter("id")));
        contato.setNome(request.getParameter("nome") );
        contato.setEmail(request.getParameter("email") );
        contato.setFone(request.getParameter("fone") );

        // DEBUG (exibir contato)
        System.out.println("[EditarContatoController] contato logo no inicio = " + contato.toString());
        
        // 2 - Buscar contato no banco de dados pela Id
        ContatoRepository cr = new ContatoRepository();
        try {
            contato = cr.buscarPorId(contato.getId());
        } catch (Exception e) {
            System.out.println("[EditarContatoController] Exception ao editar contato");
            erroDb = e.getMessage();
        }

        // DEBUG (exibir contato)
        System.out.println("[EditarContatoController] contato apos busca = " + contato.toString());

        // 3 - VALIDAR DADOS
        ArrayList<String> erros = new ArrayList<>();

        if (StringUtils.isBlank(contato.getNome())) {
            erros.add("O campo nome é obrigatório");
        }

        if (StringUtils.isBlank(contato.getEmail())) {
            erros.add("O campo email é obrigatório");
        }

        if (StringUtils.isBlank(contato.getFone())) {
            erros.add("O campo telefone é obrigatório");
        } else if (!StringUtils.isNumeric(contato.getFone())) {
            erros.add("O campo telefone deve conter apenas números");
        }

        // 4 - COLOCAR DADOS NA REQUISICAO
        if (erros.isEmpty()) {
//            request.setAttribute("sucesso", "Contato carregado com sucesso!");
            request.setAttribute("contato", contato);
        }

        // 5 - REDIRECIONAR
        System.out.println("[EditarContatoController] Encerrando execucao");
        RequestDispatcher rd = request.getRequestDispatcher("editar_contato.jsp");
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
