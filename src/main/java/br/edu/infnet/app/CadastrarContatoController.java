package br.edu.infnet.app;

import br.edu.infnet.domain.contatos.Contato;
import br.edu.infnet.infra.ContatoRepository;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

@WebServlet(name = "CadastrarContatoController", urlPatterns = {"/CadastrarContato"})
public class CadastrarContatoController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1 - OBTER OS DADOS DO FORMULARIO
        HttpSession session = request.getSession();
        String erroDb = null;
        Contato contato = new Contato();
//        contato.setNome(StringEscapeUtils.escapeJson(request.getParameter("nome") ) );
        
        contato.setNome(request.getParameter("nome") );
        contato.setEmail(request.getParameter("email"));
        contato.setFone(request.getParameter("fone"));
        contato.setUsuario( session.getAttribute("usuarioNome").toString() );

        // 2 - VALIDAR DADOS
        ArrayList<String> erros = new ArrayList<>();

        if (StringUtils.isBlank(contato.getNome())) {
            erros.add("O campo nome é obrigatório");
        } else {
            System.out.println("[CadastrarContatoController] contato.getNome ANTES da conversao pra UTF 8= " + contato.getNome() );
            contato.setNome(ControllerService.convertToUtf_8(contato.getNome() ) );
        }

        if (StringUtils.isBlank(contato.getEmail())) {
            erros.add("O campo email é obrigatório");
        } else {
            contato.setEmail(ControllerService.convertToUtf_8(contato.getEmail()) );
        }

        if (StringUtils.isBlank(contato.getFone())) {
            erros.add("O campo telefone é obrigatório");
        } else if (!StringUtils.isNumeric(contato.getFone())) {
            erros.add("O campo telefone deve conter apenas números");
        }

        if (erros.isEmpty()) {

            // 3 - EXECUTAR O PROCESSAMENTO
            ContatoRepository cr = new ContatoRepository();

            try {
                cr.inserir(contato);
            } catch (Exception e) {
                System.out.println("[CadastrarContatoController] Exception ao inserir contato na DB");
                erroDb = cr.getErroDbRepository();
                System.out.println("[CadastrarContatoController] " + erroDb);
                request.setAttribute("dadosExistem", "Ja existe contato com esses dados no cadastro");
//                request.setAttribute("erroDb", erroDb);
            }
        } else {
            // Deu erro na validacao dos dados do formulario (email ou telefone invalido, etc)
            request.setAttribute("erros", erros);
        }

        // 4 - COLOCAR DADOS NA REQUISICAO
        if (erroDb != null) { // Deu erro na gravacao no banco de dados
            request.setAttribute("erroDb", erroDb);
            request.setAttribute("contato", contato);
        } else { // Nao deu erro na gravacao no banco de dados
            if(!erros.isEmpty() ) {
                request.setAttribute("contato", contato);
            } else {
                request.setAttribute("sucesso", "Contato salvo com sucesso!");
                request.setAttribute("contato", null);
            }
        }

    // 5 - REDIRECIONAR
    RequestDispatcher rd = request.getRequestDispatcher("lista_contatos.jsp");
    rd.forward (request, response);
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
