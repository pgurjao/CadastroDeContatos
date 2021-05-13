package br.edu.infnet.app;

import br.edu.infnet.domain.Contato;
import br.edu.infnet.infra.ContatoRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

@WebServlet(name = "SalvarEdicaoContatoController", urlPatterns = {"/SalvarEdicaoContato"})
public class SalvarEdicaoContatoController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1 - OBTER OS DADOS DO FORMULARIO
        String erroDb = null;
        Contato contatoAlterado = new Contato();
        try {
            contatoAlterado.setId(Integer.parseInt(request.getParameter("id")));
        } catch (NumberFormatException e) {
            System.out.println("[SalvarEdicaoContatoController] NumberFormatException parsing getParameter('id')");
        }
        contatoAlterado.setNome(request.getParameter("nome"));
        contatoAlterado.setEmail(request.getParameter("email"));
        contatoAlterado.setFone(request.getParameter("fone"));

        // DEBUG (exibir parametros request)
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            System.out.println("[SalvarEdicaoContatoController] Parameter Name: " + paramName + ", Value: " + request.getParameter(paramName));
        }

        // DEBUG (exibir contatoAlterado)
        System.out.println("[SalvarEdicaoContatoController] contatoEditado = " + contatoAlterado.toString());
//        System.out.println("[SalvarEdicaoContatoController] URL = " + request.getRequestURL() );

        // 2 - VALIDAR DADOS
        ArrayList<String> erros = new ArrayList<>();

        if (StringUtils.isBlank(contatoAlterado.getNome())) {
            erros.add("O campo nome é obrigatório");
        }

        if (StringUtils.isBlank(contatoAlterado.getEmail())) {
            erros.add("O campo email é obrigatório");
        }

        if (StringUtils.isBlank(contatoAlterado.getFone())) {
            erros.add("O campo telefone é obrigatório");
        } else if (!StringUtils.isNumeric(contatoAlterado.getFone())) {
            erros.add("O campo telefone deve conter apenas números");
        }

        if (erros.isEmpty()) {
            // 3 - Buscar contatoAlterado no banco de dados pela Id
            ContatoRepository cr = new ContatoRepository();
            Contato contatoNoBanco = new Contato();
            try {
                contatoNoBanco = cr.buscarPorId(contatoAlterado.getId());
            } catch (Exception e) {
                System.out.println("[SalvarEdicaoContatoController] Exception ao editar contato");
                erroDb = e.getMessage();
            }
            // DEBUG (exibir contatoAlterado localizado no banco)
            System.out.println("[SalvarEdicaoContatoController] contatoNoBanco = " + contatoNoBanco.toString());

            if (contatoAlterado.getNome().equals(contatoNoBanco.getNome())
                    && contatoAlterado.getEmail().equals(contatoNoBanco.getEmail())
                    && contatoAlterado.getFone().equals(contatoNoBanco.getFone()) ) { // Usuario nao alterou nenhum dado do contato

                erros.add("Voce nao alterou nenhum dado do contato, os dados que ja estavam no banco de dados foram mantidos");
                System.out.println("[SalvarEdicaoContatoController] Voce nao alterou nenhum dado do contato, os dados que ja estavam no banco de dados foram mantidos");
            } else {
                System.out.println("[SalvarEdicaoContatoController] Dados do contato foram alterados, salvando...");
                // 3 - EXECUTAR O PROCESSAMENTO (salvar contato editado no banco de dados)
                cr = new ContatoRepository();
                try {
                    cr.editar(contatoAlterado);
                } catch (Exception e) {
                    System.out.println("[SalvarEdicaoContatoController] Exception ao editar contatoEditado");
                    erroDb = e.getMessage();
                }
            }
            // 4 - COLOCAR DADOS NA REQUISICAO
            if (erroDb != null) { // Erro na gravacao no banco de dados
                request.setAttribute("erroDb", erroDb);
                request.setAttribute("contato", contatoAlterado);
            } else { // Gravacao no banco com sucesso
                if (erros.isEmpty()) {
                    request.setAttribute("sucesso", "Contato editado e salvo com sucesso!");
                } else {
                    request.setAttribute("erros", erros);
                }
                request.setAttribute("contato", null);
            }
        } else { // Deu erro na validacao dos dados do formulario (email ou telefone invalido, etc)
            request.setAttribute("erros", erros);
        }

    // 5 - REDIRECIONAR
    System.out.println("[SalvarEdicaoContatoController] Encerrando execucao");
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
