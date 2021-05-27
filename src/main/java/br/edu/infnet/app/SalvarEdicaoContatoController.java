package br.edu.infnet.app;

import br.edu.infnet.domain.contatos.Contato;
import br.edu.infnet.domain.contatos.Endereco;
import br.edu.infnet.infra.ContatoRepository;
import br.edu.infnet.infra.ControllerService;
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

@WebServlet(name = "SalvarEdicaoContatoController", urlPatterns = {"/SalvarEdicaoContato"})
public class SalvarEdicaoContatoController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1 - OBTER OS DADOS DO FORMULARIO
        ArrayList<String> erros = new ArrayList<>();
        String erroDb = null;
        String usuarioNome;
        int contatoId = -1;

        HttpSession session = request.getSession();
        usuarioNome = session.getAttribute("usuarioNome").toString();

        Contato contatoParaSalvar = new Contato();

        Endereco endereco = new Endereco();

        try {
            contatoId = Integer.parseInt(request.getParameter("id"));
            contatoParaSalvar.setId(contatoId);
        } catch (NumberFormatException e) {
            System.out.println("[SalvarEdicaoContatoController] NumberFormatException parsing getParameter('id')");
            erros.add("NumberFormatException getting userID");
        }

        if (contatoId == -1) {
            erros.add("Impossivel recuperar usuario ID");
        }

        contatoParaSalvar.setNome(request.getParameter("nome"));
        contatoParaSalvar.setEmail(request.getParameter("email"));
        contatoParaSalvar.setFone(request.getParameter("fone"));
        contatoParaSalvar.setUsuario(session.getAttribute("usuarioNome").toString());

        endereco.setCep(request.getParameter("cep"));
        endereco.setLogradouro(request.getParameter("logradouro"));
        endereco.setComplemento(request.getParameter("complemento"));
        endereco.setBairro(request.getParameter("bairro"));
        endereco.setLocalidade(request.getParameter("localidade"));
        endereco.setUf(request.getParameter("uf"));

        contatoParaSalvar.setEndereco(endereco);

        // 2 - VALIDAR DADOS
        ArrayList<String> errosEndereco = new ArrayList<>();

        if (StringUtils.isBlank(contatoParaSalvar.getNome())) {
            erros.add("O campo nome é obrigatório");
        } else {
            if (!ControllerService.isValidString(contatoParaSalvar.getNome() ) ) {
                erros.add("O campo nome contem caracteres inválidos");
            }
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEmail())) {
            erros.add("O campo email é obrigatório");
        } else {
            if (!ControllerService.isValidEmail(contatoParaSalvar.getEmail())) {
                erros.add("O email digitado nao é válido");
            }
        }

        if (StringUtils.isBlank(contatoParaSalvar.getFone())) {
            erros.add("O campo telefone é obrigatório");
        } else if (!StringUtils.isNumeric(contatoParaSalvar.getFone())) {
            erros.add("O campo telefone deve conter apenas números");
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEndereco().getCep())) { // INICIO VALIDACAO DOS CAMPOS DO ENDERECO
            errosEndereco.add("O CEP é obrigatorio");
        } else if (!StringUtils.isNumeric(contatoParaSalvar.getEndereco().getCep() )
                   || contatoParaSalvar.getEndereco().getCep().length() != 8) {
            errosEndereco.add("O campo CEP deve conter apenas números e possuir 8 digitos");
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEndereco().getLogradouro())) {
            errosEndereco.add("O logradouro é obrigatorio");
        } else {
            if (!ControllerService.isValidString(endereco.getLogradouro() ) ) {
                errosEndereco.add("O campo logradouro contem caracteres inválidos");
            }
        }

        if (StringUtils.isNotBlank(contatoParaSalvar.getEndereco().getComplemento())) { // O COMPLEMENTO É OPCIONAL
            if (!ControllerService.isValidString(contatoParaSalvar.getEndereco().getComplemento())) {
                errosEndereco.add("O campo complemento contem caracteres inválidos");
            }
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEndereco().getBairro())) {
            errosEndereco.add("O bairro é obrigatorio");
        } else {
            if (!ControllerService.isValidString(contatoParaSalvar.getEndereco().getBairro())) {
                errosEndereco.add("O campo bairro contem caracteres inválidos");
            }
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEndereco().getLocalidade())) {
            errosEndereco.add("A cidade é obrigatoria");
        } else {
            if (!ControllerService.isValidString(contatoParaSalvar.getEndereco().getLocalidade())) {
                errosEndereco.add("O campo localidade contem caracteres inválidos");
            }
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEndereco().getUf())) {
            errosEndereco.add("A UF é obrigatoria");
        }  else {
            if (!ControllerService.isValidString(contatoParaSalvar.getEndereco().getUf())) {
                errosEndereco.add("O campo UF contem caracteres inválidos");
            }
        }

        if (erros.isEmpty() && errosEndereco.isEmpty()) {

            if (request.getParameter("salvarContato") != null && request.getParameter("BuscarCep") == null) { // VERIFICA SE BOTAO APERTADO FOI 'SALVAR CONTATO' E CONTINUA SE POSITIVO

                // 2.1 - Buscar contatoAlterado no banco de dados pela Id
                Contato contatoNoBanco = new Contato();
                ContatoRepository cr = new ContatoRepository();
                try {
                    contatoNoBanco = cr.buscarPorId(contatoParaSalvar.getId(), usuarioNome);
                } catch (Exception e) {
                    System.out.println("[SalvarEdicaoContatoController] Exception ao editar contato");
                    erroDb = e.getMessage();
                }
                // DEBUG (exibir contatoAlterado localizado no banco)

                if (contatoNoBanco == null) {
                    System.out.println("[SalvarEdicaoContatoController] contatoNoBanco = null");
                }

                if (contatoParaSalvar.getNome().equals(contatoNoBanco.getNome())
                        && contatoParaSalvar.getEmail().equals(contatoNoBanco.getEmail())
                        && contatoParaSalvar.getFone().equals(contatoNoBanco.getFone())
                        && contatoParaSalvar.getEndereco().equals(contatoNoBanco.getEndereco())) { // USUARIO NAO ALTEROU NENHUM DADO DO CONTATO

                    erros.add("Voce nao alterou nenhum dado do contato, os dados que ja estavam no banco de dados foram mantidos");
                } else { // ALGUM DADO DO CONTATO FOI ALTERADO, SALVANDO...
                    if (contatoNoBanco.getUsuario().equals(usuarioNome)) { // O CONTATO FOI ALTERADO E PERTENCE A ESSE USUARIO

                        // 3 - EXECUTAR O PROCESSAMENTO (salvar contato editado no banco de dados)
                        cr = new ContatoRepository();
                        try {
                            cr.editar(contatoParaSalvar, usuarioNome);
                        } catch (Exception e) {
                            System.out.println("[SalvarEdicaoContatoController] Exception ao editar contatoEditado");
                            erroDb = e.getMessage();
                            System.out.println("[SalvarEdicaoContatoController] erroDb = " + erroDb);
                        }
                    } else { // O CONTATO NAO PERTENCE A ESSE USUARIO
                        erros.add("Voce nao tem permissao para editar esse contato");
                        System.out.println("[SalvarEdicaoContatoController] Voce nao tem permissao para editar esse contato");
                    }
                }
                // 4 - COLOCAR DADOS NA REQUISICAO
                if (erroDb != null) { // Erro na gravacao no banco de dados
                    request.setAttribute("erroDb", erroDb);
                    request.setAttribute("contato", contatoParaSalvar);
                } else { // Gravacao no banco com sucesso
                    if (erros.isEmpty()) {
                        request.setAttribute("sucesso", "Contato editado e salvo com sucesso!");
                    } else {
                        request.setAttribute("erros", erros);
                    }
//                    request.setAttribute("contato", null);
                }
            }
        } else { // Deu erro na validacao dos dados do formulario (usuarioID, nome, email, telefone, endereco invalido, etc)
            request.setAttribute("erros", erros);
            request.setAttribute("errosEndereco", errosEndereco);
            request.setAttribute("contatoNome", contatoParaSalvar.getNome() );
        }

        // 5 - REDIRECIONAR
        if (request.getParameter("salvarContato") != null && request.getParameter("BuscarCep") == null) { // VERIFICA SE BOTAO APERTADO FOI 'salvarContato'
            RequestDispatcher rd = request.getRequestDispatcher("lista_contatos.jsp");
            rd.forward(request, response);
        } else {
            // Executa codigo para alterar o endereco (direciona para o JSP aplicavel, etc)
            if (request.getParameter("BuscarCep") != null && request.getParameter("salvarContato") == null) { // VERIFICA SE BOTAO APERTADO FOI 'BuscarCep'
                contatoParaSalvar = new Contato();
                contatoParaSalvar.setId(contatoId);
                request.setAttribute("contato", contatoParaSalvar);
                RequestDispatcher rd = request.getRequestDispatcher("buscar_cep.jsp");
                rd.forward(request, response);
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
