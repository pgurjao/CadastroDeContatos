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
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1 - OBTER OS DADOS DO FORMULARIO
        String erroDb = null;
        boolean usuarioCorreto = false;
        boolean senhaCorreta = false;

        HttpSession session = request.getSession();
        Usuario usuario = new Usuario();
        Usuario usuarioSalvo = new Usuario();

        usuario.setNome(request.getParameter("usuarioNome"));
        usuario.setSenha(request.getParameter("senha"));
        // usuario.setCreate_time(request.getParameter("create_time"));

        // 2 - VALIDAR DADOS
        ArrayList<String> erros = new ArrayList<>();

        // 2.1 - VERIFICAR SE O METODO UTILIZANDO FOI 'POST' E REJEITAR SE NAO TIVER SIDO
        if (!request.getMethod().equalsIgnoreCase("POST") ) {
            erros.add("Operacao invalida");
            session.invalidate();
            usuario.setSenha("");
            request.removeAttribute("usuario");
            request.setAttribute("erros", erros);
            System.out.println("[LoginController] Erro: somente sao aceitas requisicoes via POST");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward (request, response);
            return;
        }

        if (StringUtils.isBlank(usuario.getNome())) {
            erros.add("O campo usuario eh obrigatorio");
        }

        if (StringUtils.isBlank(usuario.getSenha())) {
            erros.add("O campo senha eh obrigatorio");
        }

        // 3 - EXECUTAR O PROCESSAMENTO
        if (erros.isEmpty()) { // Usuario e senha nao sao invalidos e podem ser buscados no banco de dados

            // BUSCAR USUARIO NO BANCO DE DADOS
            UsuarioRepository ur = new UsuarioRepository();
            try {
                usuarioSalvo = ur.buscarPorUsuario(usuario.getNome());
            } catch (Exception e) {
                System.out.println("[LoginController] Exception ao buscar usuario na DB");
                erroDb = ur.getErroDbRepository();
                System.out.println("[LoginController] erroDb = " + erroDb);
            }

            // DEBUG (exibir usuario retornado pelo banco de dados)
            System.out.println("[LoginController] usuarioSalvo (retornado pelo DB) = " + usuarioSalvo.toString() );

            // 3.1 - VERIFICAR SE BANCO DE DADOS RETORNOU NULL E ERRO E ENCERRAR CASO POSITIVO
            if (erroDb != null && usuarioSalvo.getNome() == null) {
                System.out.println("[LoginController] Erro na Db, impossivel fazer login no momento");
                erros.add("Impossivel fazer login no momento");
                usuario.setSenha("");
                // request.removeAttribute("usuario");
                request.setAttribute("usuario", usuario);
                request.setAttribute("erros", erros);
                request.setAttribute("erroDb", erroDb);
                session.invalidate();
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.forward (request, response);
                return;
            }
            
            // 3.2 - VERIFICAR SE USUARIO E SENHA ESTAO CORRETOS
            if (usuarioSalvo.getNome() != null && usuario.getNome().equals(usuarioSalvo.getNome())   ) {
                usuarioCorreto = true;
                System.out.println("[LoginController] Usuario correto, verificando senha...");
            } else {
                usuarioCorreto = false;
                erros.add("Usuario ou senha incorretos (Usuario nao localizado)");
                System.out.println("[LoginController] Usuario nao localizado, login nao autorizado");
            }

            if (usuarioSalvo.getSenha() != null && usuarioSalvo.getNome() != null && usuario.getSenha().equals(usuarioSalvo.getSenha())) {
                senhaCorreta = true;
                System.out.println("[LoginController] Senha correta, login autorizado");
            } else {
                senhaCorreta = false;
                erros.add("Usuario ou senha incorretos (Senha incorreta)");
                System.out.println("[LoginController] Senha incorreta, login nao autorizado");
            }

            // 3.3 GRAVAR SESSION ID NO BANCO DE DADOS
            if (usuarioCorreto && senhaCorreta) {
                // ur = new UsuarioRepository();
                try {
                    ur.gravarSessao(session.getId(), usuario.getNome() );
                } catch (Exception e) {
                    System.out.println("[LoginController] Exception ao gravar sessao na DB");
                    erroDb = ur.getErroDbRepository();
                    System.out.println("[LoginController] erroDb = " + erroDb);
                    // request.setAttribute("erroDb", erroDb);
                }
            }
        }

        // 4 - COLOCAR DADOS NA REQUISICAO
        if (!erros.isEmpty() ) { // Usuario ou senha nao sao iguais aos localizados no banco de dados,
                                 // ou usuario digitou dados invalidos (em branco, null, caracteres invalidos, etc)
            request.setAttribute("erros", erros);
            usuario.setSenha("");
            request.setAttribute("usuario", usuario);
        } else { // Nao deu erro ate agora, array 'erros' estah vazio
            if (usuarioCorreto && senhaCorreta) {
                usuario.setSenha("");
//                usuario.setCreate_time(usuarioSalvo.getCreate_time() );
//                request.setAttribute("usuario", usuario);
                System.out.println("[LoginController] Usuario e senha corretos, login autorizado");
                System.out.println("[LoginController] ------------------------------------------");
                // Gravar nome de usuario na SESSION
                session.setAttribute("usuarioNome", usuarioSalvo.getNome() );
            }
        }

        if (erroDb != null) { // Deu erro no banco de dados
            request.setAttribute("erroDb", erroDb);
            request.setAttribute("usuario", usuario);
        }



        // 5 - REDIRECIONAR
        if (!erros.isEmpty() || erroDb != null ) { // Usuario ou senha nao sao iguais aos localizados no banco de dados,
                                                   // ou usuario digitou dados invalidos (em branco, null, caracteres invalidos, etc),
                                                   // ou deu erro no banco de dados
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward (request, response);
        } else {
            if (erros.isEmpty() && erroDb == null && usuarioCorreto && senhaCorreta) { // Usuario e senha estao corretos e nao deu erro algum
                RequestDispatcher rd = request.getRequestDispatcher("lista_contatos.jsp");
                rd.forward (request, response);
            } else { // Equivalente ao "switch default", eh para pegar os estados inconsistentes.
                erros.add("Erro desconhecido ao processar a requisicao");
                session.invalidate();
                usuario.setSenha("");
                request.removeAttribute("usuario");
                request.setAttribute("erroDb", erroDb);
                request.setAttribute("erros", erros);
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.forward (request, response);
            }
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
