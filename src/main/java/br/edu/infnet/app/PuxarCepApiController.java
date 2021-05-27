package br.edu.infnet.app;

import br.edu.infnet.domain.contatos.Contato;
import br.edu.infnet.domain.contatos.Endereco;
import br.edu.infnet.infra.ContatoRepository;
import br.edu.infnet.infra.contatos.EnderecoService;
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

@WebServlet(name = "PuxarCepApiController", urlPatterns = {"/PuxarCepApi"})
public class PuxarCepApiController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1 - OBTER OS DADOS DO FORMULARIO    ----- CONSERTAR LOGICA DE INSERCAO DE ENDERECO QUANDO CEP NÃO EXISTE
        int contatoId;
        String usuarioNome;
        String cep;
        String erroDb;

        Contato contato = new Contato();
        Endereco endereco = new Endereco();
        Endereco enderecoOld = new Endereco();
        ArrayList<String> erros = new ArrayList<>();

        HttpSession session = request.getSession();
        usuarioNome = session.getAttribute("usuarioNome").toString();
        contato.setUsuario(usuarioNome);

        contato.setNome(request.getParameter("nome"));
        contato.setEmail(request.getParameter("email"));
        contato.setFone(request.getParameter("fone"));

        enderecoOld.setCep(request.getParameter("cep"));
        enderecoOld.setLogradouro(request.getParameter("logradouro"));
        enderecoOld.setComplemento(request.getParameter("complemento"));
        enderecoOld.setBairro(request.getParameter("bairro"));
        enderecoOld.setLocalidade(request.getParameter("localidade"));
        enderecoOld.setUf(request.getParameter("uf"));

        contato.setEndereco(enderecoOld);

        try {
            contatoId = Integer.parseInt(request.getParameter("id"));
            contato.setId(contatoId);
        } catch (NumberFormatException e) {
            System.out.println("[PuxarCepApiController] NumberFormatException parsing getParameter('id') (56)");
//            contatoId = 0;
        }
        cep = request.getParameter("cep");

        // 2 - VALIDAR DADOS
        if (StringUtils.isNotBlank(cep) && StringUtils.isNumeric(cep) && cep.length() == 8) { // VERIFICAR SE O CEP EH VALIDO, SEM PONTOS OU TRACOS

            // 3 - EXECUTAR O PROCESSAMENTO
            EnderecoService es = new EnderecoService();
            endereco = es.obterPorCep(cep);

            if (StringUtils.isNotBlank(endereco.getErro())) { // VERIFICA SE A RESPOSTA FOI 'CEP INEXISTENTE' (erro=true no json)
                if (endereco.getErro().equalsIgnoreCase("true")) {
                    erros.add("CEP nao existe");
                }
            } else { // O CEP EXISTE, INSERIR ENDERECO NO CONTATO E PEGAR DADOS DO CONTATO NA DB PARA INSERIR NA REQUISICAO
                endereco.setCep(endereco.getCep().replace("-", ""));

                // Buscar contato na DB pelo id (recebido de buscar_cep.jsp)
                ContatoRepository cr = new ContatoRepository();
                try {
                    contato = cr.buscarPorId(contato.getId(), usuarioNome);
                } catch (Exception e) {
                    System.out.println("[PuxarCepApiController] Exception ao buscar contato na DB (88)");
                    erroDb = e.getMessage();
                }
                contato.setEndereco(null); // APAGANDO ENDERECO DO CONTATO RETORNADO PELA DB
                contato.setEndereco(endereco); // SETANDO ENDERECO RETORNADO PELA BUSCA DO CEP ATRAVES DA API VIACEP

            }
        } else {
            erros.add("CEP invalido, deve conter conter 8 numeros e nao pode conter pontos ou tracos");
        }
        // 4 - COLOCAR DADOS NA REQUISICAO

//        contato.setEndereco(enderecoOld); // CONVERTER PARAMETRO ENDERECO PARA OBJETO ENDERECO
        request.setAttribute("contato", contato);

//        request.setAttribute("id", contatoId);
        // 5 - REDIRECIONAR
        if (erros.isEmpty()) {
            RequestDispatcher rd = request.getRequestDispatcher("editar_contato.jsp");
            rd.forward(request, response);
        } else {
            request.setAttribute("erros", erros);
            RequestDispatcher rd = request.getRequestDispatcher("buscar_cep.jsp");
            rd.forward(request, response);

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
