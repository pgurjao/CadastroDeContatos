package br.edu.infnet.app;

import br.edu.infnet.domain.Contato;
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
import org.apache.commons.lang.StringUtils;

@WebServlet(name = "CadastrarContatoController", urlPatterns = {"/CadastrarContato"})
public class CadastrarContatoController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1 - OBTER OS DADOS DO FORMULARIO
        String erroDb = null;
        Contato contato = new Contato();
        contato.setNome(request.getParameter("nome") );
        contato.setEmail(request.getParameter("email") );
        contato.setFone(request.getParameter("fone") );
        
        // 2 - VALIDAR DADOS
        ArrayList<String> erros = new ArrayList<>();
        
        if(StringUtils.isBlank(contato.getNome() ) ) {
            erros.add("O campo nome é obrigatório");
        }
        
        if(StringUtils.isBlank(contato.getEmail()) ) {
            erros.add("O campo email é obrigatório");
        }
        
        if(StringUtils.isBlank(contato.getFone()) ) {
            erros.add("O campo telefone é obrigatório");
        } else if(!StringUtils.isNumeric(contato.getFone() ) ) {
            erros.add("O campo telefone deve conter apenas números");
        }
        
        if(erros.isEmpty() ) {
            // -----------
            // 3 - EXECUTAR O PROCESSAMENTO
            ContatoRepository cr = new ContatoRepository();
            try {
                cr.inserir(contato);
            }
            catch (Exception e) {
                System.out.println("[CadastrarContatoController] Exception ao inserir contato na DB");
                erroDb = e.getMessage();
                request.setAttribute("dadosExistem", "Ja existe contato com esses dados no cadastro");
            }
            
            // 4 - COLOCAR DADOS NA REQUISICAO
            if(erroDb != null ) { // Deu erro na gravacao no banco de dados
                request.setAttribute("erroDb", erroDb);
                request.setAttribute("contato", contato);
            } else { // Nao deu erro na gravacao no banco de dados
                request.setAttribute("sucesso", "Contatos salvos com sucesso!");
                request.setAttribute("contato", null);
            }
        } else {
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
