package br.edu.infnet.app;

import br.edu.infnet.domain.contatos.Contato;
import br.edu.infnet.domain.contatos.Endereco;
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

@WebServlet(name = "SalvarEdicaoContatoController", urlPatterns = {"/SalvarEdicaoContato"})
public class SalvarEdicaoContatoController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1 - OBTER OS DADOS DO FORMULARIO
        String erroDb = null;
        String usuarioNome;
        int contatoId;

        HttpSession session = request.getSession();
        usuarioNome = session.getAttribute("usuarioNome").toString();
        System.out.println("[SalvarEdicaoContatoController] usuarioNome = " + usuarioNome);

        Contato contatoParaSalvar = new Contato();

        Endereco endereco = new Endereco();

        try {
            contatoId = Integer.parseInt(request.getParameter("id"));
            contatoParaSalvar.setId(contatoId);
        } catch (NumberFormatException e) {
            System.out.println("[SalvarEdicaoContatoController] NumberFormatException parsing getParameter('id')");
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

        // DEBUG (exibir contato depois de pegar todos os parametros da request)
        System.out.println("[SalvarEdicaoContatoController] contatoParaSalvar.toString DEPOIS de pegar parametros do request = " + contatoParaSalvar.toString());
        System.out.println("[SalvarEdicaoContatoController] endereco = " + contatoParaSalvar.toString());

        // DEBUG (exibir parametros request)
//        Enumeration<String> params = request.getParameterNames();
//        while (params.hasMoreElements()) {
//            String paramName = params.nextElement();
//            System.out.println("[SalvarEdicaoContatoController] Parameter Name: " + paramName + ", Value: " + request.getParameter(paramName));
//        }
        // DEBUG (exibir contatoAlterado)
        System.out.println("[SalvarEdicaoContatoController] contatoParaSalvar = " + contatoParaSalvar.toString());

        // 2 - VALIDAR DADOS
        ArrayList<String> erros = new ArrayList<>();
        ArrayList<String> errosEndereco = new ArrayList<>();

        if (StringUtils.isBlank(contatoParaSalvar.getNome())) {
            erros.add("O campo nome é obrigatório");
        } else {
            contatoParaSalvar.setNome(ControllerService.convertToUtf_8(contatoParaSalvar.getNome() ) );
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEmail())) {
            erros.add("O campo email é obrigatório");
        } else {
            contatoParaSalvar.setEmail(ControllerService.convertToUtf_8(contatoParaSalvar.getEmail()) );
        }

        if (StringUtils.isBlank(contatoParaSalvar.getFone())) {
            erros.add("O campo telefone é obrigatório");
        } else if (!StringUtils.isNumeric(contatoParaSalvar.getFone())) {
            erros.add("O campo telefone deve conter apenas números");
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEndereco().getCep())) { // INICIO VALIDACAO DOS CAMPOS DO ENDERECO
            errosEndereco.add("O CEP é obrigatorio");
        } else if (!StringUtils.isNumeric(contatoParaSalvar.getFone())) {
            erros.add("O campo CEP deve conter apenas números");
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEndereco().getLogradouro())) {
            errosEndereco.add("O logradouro é obrigatorio");
        } else {
            endereco.setLogradouro(ControllerService.convertToUtf_8(endereco.getLogradouro() ));
        }

        if (StringUtils.isNotBlank(contatoParaSalvar.getEndereco().getComplemento())) { // O COMPLEMENTO É OPCIONAL
            endereco.setComplemento(ControllerService.convertToUtf_8(endereco.getComplemento()));
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEndereco().getBairro())) {
            errosEndereco.add("O bairro é obrigatorio");
        } else {
            endereco.setBairro(ControllerService.convertToUtf_8(endereco.getBairro()));
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEndereco().getLocalidade())) {
            errosEndereco.add("A cidade é obrigatoria");
        } else {
            endereco.setLocalidade(ControllerService.convertToUtf_8(endereco.getLocalidade()));
        }

        if (StringUtils.isBlank(contatoParaSalvar.getEndereco().getUf())) {
            errosEndereco.add("A UF é obrigatoria");
        }  else {
            endereco.setUf(ControllerService.convertToUtf_8(endereco.getUf()));
        }
        
        
        contatoParaSalvar.setEndereco(endereco); // SETANDO O ENDERECO APOS TODAS AS CONVERSOES NOS CAMPOS
        
        if (erros.isEmpty() && errosEndereco.isEmpty()) {

            if (request.getParameter("salvarContato") != null && request.getParameter("BuscarCep") == null) { // VERIFICA SE BOTAO APERTADO FOI 'SALVAR CONTATO' E CONTINUA SE POSITIVO
                System.out.println("[SalvarEdicaoContatoController] Botao apertado = salvar contato, validando dados...");

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
                System.out.println("[SalvarEdicaoContatoController] contatoNoBanco = " + contatoNoBanco.toString());

                if (contatoNoBanco == null) {
                    System.out.println("[SalvarEdicaoContatoController] contatoNoBanco = null");
                }

                if (contatoParaSalvar.getNome().equals(contatoNoBanco.getNome())
                        && contatoParaSalvar.getEmail().equals(contatoNoBanco.getEmail())
                        && contatoParaSalvar.getFone().equals(contatoNoBanco.getFone())
                        && contatoParaSalvar.getEndereco().equals(contatoNoBanco.getEndereco())) { // USUARIO NAO ALTEROU NENHUM DADO DO CONTATO

                    erros.add("Voce nao alterou nenhum dado do contato, os dados que ja estavam no banco de dados foram mantidos");
                    System.out.println("[SalvarEdicaoContatoController] Voce nao alterou nenhum dado do contato, os dados que ja estavam no banco de dados foram mantidos");
                } else { // ALGUM DADO DO CONTATO FOI ALTERADO, SALVANDO...
                    if (contatoNoBanco.getUsuario().equals(usuarioNome)) { // O CONTATO FOI ALTERADO E PERTENCE A ESSE USUARIO
                        System.out.println("[SalvarEdicaoContatoController] Dados do contato foram alterados, salvando...");
                        
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
        } else { // Deu erro na validacao dos dados do formulario (nome, email, telefone, endereco invalido, etc)
            request.setAttribute("erros", erros);
            request.setAttribute("errosEndereco", errosEndereco);
            request.setAttribute("contatoNome", contatoParaSalvar.getNome() );
        }

        // 5 - REDIRECIONAR
        if (request.getParameter("salvarContato") != null && request.getParameter("BuscarCep") == null) { // VERIFICA SE BOTAO APERTADO FOI 'salvarContato'
            System.out.println("[SalvarEdicaoContatoController] Encerrando execucao (botao salvar contato)");
            RequestDispatcher rd = request.getRequestDispatcher("lista_contatos.jsp");
            rd.forward(request, response);
        } else {
            // Executa codigo para alterar o endereco (direciona para o JSP aplicavel, etc)
            if (request.getParameter("BuscarCep") != null && request.getParameter("salvarContato") == null) { // VERIFICA SE BOTAO APERTADO FOI 'BuscarCep'
                System.out.println("[SalvarEdicaoContatoController] Encerrando execucao (botao BuscarCep)");
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
