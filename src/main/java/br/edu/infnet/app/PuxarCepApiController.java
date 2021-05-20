package br.edu.infnet.app;

import br.edu.infnet.domain.contatos.Endereco;
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
        
        String usuarioNome;
        String cep;
        ArrayList<String> erros = new ArrayList<>();
        
        HttpSession session = request.getSession();
        usuarioNome = session.getAttribute("usuarioNome").toString();
        cep = request.getParameter("cep");
        
        if (StringUtils.isNotBlank(cep) && StringUtils.isNumeric(cep) && cep.length() == 8) {
            EnderecoService es = new EnderecoService();
            try {
                Endereco endereco = es.obterPorCep(cep);
                request.setAttribute("endereco", endereco);
            } catch (Exception e) {
                System.out.println("[PuxarCepApiController] Exception obtendo CEP via API = ");
                e.printStackTrace();
            }
        } else {
            erros.add("CEP invalido, deve conter conter 8 numeros e nao pode conter pontos ou tracos");
            request.setAttribute("erros", erros);
        }
        
        if (erros.isEmpty() ) {
            RequestDispatcher rd = request.getRequestDispatcher("editar_contato.jsp");
            rd.forward(request, response);
        } else {
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
