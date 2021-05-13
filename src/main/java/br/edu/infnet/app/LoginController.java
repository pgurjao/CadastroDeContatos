package br.edu.infnet.app;

import br.edu.infnet.domain.Usuario;
import br.edu.infnet.infra.UsuarioRepository;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /// 1 - OBTER OS DADOS DO FORMULARIO
        String erroDb = null;
        Usuario usuario = new Usuario();
        Usuario usuarioSalvo = new Usuario();

        usuario.setUsuario(request.getParameter("usuario"));
        usuario.setSenha(request.getParameter("senha"));
//        usuario.setCreate_time(request.getParameter("create_time"));

        // 2 - VALIDAR DADOS
        ArrayList<String> erros = new ArrayList<>();

        if (StringUtils.isBlank(usuario.getUsuario())) {
            erros.add("O campo usuario eh obrigatorio");
        }

        if (StringUtils.isBlank(usuario.getSenha())) {
            erros.add("O campo senha eh obrigatorio");
        }

        // 3 - EXECUTAR O PROCESSAMENTO
        if (erros.isEmpty()) {

            // 3 - EXECUTAR O PROCESSAMENTO
            UsuarioRepository ur = new UsuarioRepository();

            try {
                usuarioSalvo = ur.buscarPorUsuario(usuario.getUsuario());

            } catch (Exception e) {
                System.out.println("[LoginController] Exception ao buscar usuario na DB");
                erroDb = ur.getErroDbRepository();
                System.out.println("[LoginController] erroDb = " + erroDb);
//                request.setAttribute("erroDb", erroDb);
            }
                System.out.println("[LoginController] usuarioSalvo = " + usuarioSalvo.toString() );
                // 3.1 - VERIFICAR SE USUARIO E SENHA ESTAO CORRETOS
                if (usuario.getUsuario().equals(usuarioSalvo.getUsuario())) {
                    System.out.println("[LoginController] Usuario correto, verificando senha...");
                } else {
                    erros.add("Usuario nao localizado");
                    System.out.println("[LoginController] Usuario nao localizado");
                }
                
                if (usuario.getSenha().equals(usuarioSalvo.getSenha())) {
                    System.out.println("[LoginController] Senha correta, login autorizado");
                } else {
                    erros.add("Senha incorreta");
                    System.out.println("[LoginController] Senha incorreta, login nao autorizado");
                }
        } else {
            // Deu erro na validacao dos dados do formulario (usuario ou senha invalidos, vazios, NULL, etc)
            request.setAttribute("erros", erros);
        }

        // 4 - COLOCAR DADOS NA REQUISICAO
        if (erroDb != null) { // Deu erro no banco de dados
            request.setAttribute("erroDb", erroDb);
            request.setAttribute("usuario", usuario);
        } else { // Nao deu erro no banco de dados
            if (!erros.isEmpty() ) {
                request.setAttribute("erros", erros);
                usuario.setSenha("");
                request.setAttribute("usuario", usuario);
            } else {
                request.setAttribute("sucesso", "Usuario e senha corretos!");
                usuario.setSenha("");
                usuario.setCreate_time(usuarioSalvo.getCreate_time() );
                request.setAttribute("usuario", usuario);
//                request.setAttribute("usuario", null);
            }
        }
        // 5 - REDIRECIONAR
        if (!erros.isEmpty() || erroDb != null ) {
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward (request, response);
        } else {
            RequestDispatcher rd = request.getRequestDispatcher("lista_contatos.jsp");
            rd.forward (request, response);
        }
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
