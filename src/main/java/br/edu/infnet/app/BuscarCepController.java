package br.edu.infnet.app;

import br.edu.infnet.domain.contatos.Contato;
import br.edu.infnet.domain.contatos.Endereco;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "BuscarCepController", urlPatterns = {"/BuscarCep"})
public class BuscarCepController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1 - OBTER OS DADOS DO FORMULARIO
        String erroDb = null;
        String usuarioNome;

        HttpSession session = request.getSession();
        Contato contatoBuscandoCep = new Contato();

        Endereco endereco = new Endereco();

        try {
            contatoBuscandoCep.setId(Integer.parseInt(request.getParameter("id")));
        } catch (NumberFormatException e) {
            System.out.println("[SalvarEdicaoContatoController] NumberFormatException parsing getParameter('id')");
        }
        contatoBuscandoCep.setNome(request.getParameter("nome"));
        contatoBuscandoCep.setEmail(request.getParameter("email"));
        contatoBuscandoCep.setFone(request.getParameter("fone"));
        usuarioNome = session.getAttribute("usuarioNome").toString();
        contatoBuscandoCep.setUsuario(usuarioNome);

        endereco.setCep(request.getParameter("cep"));
        endereco.setLogradouro(request.getParameter("logradouro"));
        endereco.setComplemento(request.getParameter("complemento"));
        endereco.setBairro(request.getParameter("bairro"));
        endereco.setLocalidade(request.getParameter("localidade"));
        endereco.setUf(request.getParameter("uf"));

        contatoBuscandoCep.setEndereco(endereco);

        // DEBUG (exibir contato depois de pegar todos os parametros da request)
        System.out.println("[BuscarCepController] contatoBuscandoCep DEPOIS de pegar parametros do request = " + contatoBuscandoCep.toString());
        
        // 2 - VALIDAR DADOS
        
        // 3 - EXECUTAR O PROCESSAMENTO (salvar contato editado no banco de dados)
        
        // 4 - COLOCAR DADOS NA REQUISICAO
        
        // 5 - REDIRECIONAR
        
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
