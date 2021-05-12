<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cadastro de Contatos</title>
        <!-- --------------------------------------------------------------- -->
        <!-- ARQUIVOS .js NECESSARIOS PARA DATATABLES -->
        <script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/1.10.24/js/dataTables.bootstrap.min.js"></script>
        <!-- --------------------------------------------------------------- -->
        <!-- ARQUIVOS .css NECESSARIOS PARA DATATABLES -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.datatables.net/1.10.24/css/dataTables.bootstrap.min.css">
        <!-- --------------------------------------------------------------- -->
        <!-- SCRIPT PARA CARREGAR DATATABLES EM PORTUGUES -->
        <script type="text/javascript">
            $(document).ready(function() {
                $('#tabelaContatos').DataTable( {
                    "language": {
                        "url": "https://cdn.datatables.net/plug-ins/1.10.24/i18n/Portuguese-Brasil.json"
                    }
                } );
            } );
        </script>
    </head>
    <body style="margin: 20px">
        <h1>Cadastro de Contatos</h1>
        <hr />
        
        <%-- FORMULARIO DE INCLUSAO DE CONTATO --%>
        
        <form action="CadastrarContato" method="post">
            <table border="1" cellpadding="3" cellspacing="0">
                <tr>
                    <td>
                        Nome:
                    </td>
                    <td>
                        <input type="text" name="nome" maxlength="100" value="${contato.nome}" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Email:
                    </td>
                    <td>
                        <input type="text" name="email" maxlength="50" value="${contato.email}" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Telefone:
                    </td>
                    <td>
                        <input type="text" name="fone" maxlength="50" value="${contato.fone}" />
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="Cadastrar">
                    </td>
                </tr>
            </table>
        </form>
        <br />
        
        <%-- EXIBICAO DE MENSAGENS DE SUCESSO OU ERRO AO SALVAR CONTATOS --%>
        
        <c:if test="${not empty sucesso}">
            <h4 style="color:green">${sucesso}</h4>
        </c:if>
        <c:if test="${not empty erros}">
            <c:forEach var="erro" items="${erros}">
                <h4 style="color:red">Erro: ${erro}</h4>
            </c:forEach>
        </c:if>
        <c:if test="${erroDb != null}">
            <h4 style="color:brown">Erro ao gravar contato: ${erroDb}</h4>
        </c:if>
        <br />
        
        <%-- CHAMADA DE ContatoRepository DIRETAMENTE, SEM USAR O SERVLET, PARA LISTAR TODOS OS CONTATOS NO BANCO DE DADOS --%>
        
        <jsp:useBean id="repository" scope="page" class="br.edu.infnet.infra.ContatoRepository" />
        <c:set var="contatos" value="${repository.listar()}" />
        
        <%-- CRIA VARIAVEL 'erroDbRepository' PARA SALVAR EVENTUAL MENSAGEM DE ERRO AO CHAMAR 'repository.listar()' ACIMA --%>
        
        <c:set var="erroDbRepository" value="${repository.getErroDbRepository()}" />
        
        <%-- CRIA TABELA PARA PREENCHER COM CONTATOS RETORNADOS POR 'repository.listar()' --%>
        
        <c:if test="${not empty contatos}">
            <div style="width: 50%">
                <table id="tabelaContatos" class="table table-striped table-bordered" border="1" cellpadding="3" cellspacing="0">
                    <thead>
                        <tr>
                            <th class="text-center">Id</th>
                            <th>Nome</th>
                            <th>Email</th>
                            <th>Telefone</th>
                            <th class="text-center">Editar</th>
                            <th class="text-center">Excluir</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="contato" items="${contatos}">
                            <tr>
                                <td align="center">${contato.id}</td>
                                <td>${contato.nome}</td>
                                <td>${contato.email}</td>
                                <td>${contato.fone}</td>
                                <td align="center">
                                    
                                    <%-- FORMULARIO DE EDICAO DE CONTATO --%>
                                    
                                    <form action="EditarContato" method="post">
                                        <%--<c:set var="idDb" scope="page" value="${contato.id}" />--%>
                                        <input type="hidden" name="id" value="${contato.id}">
                                        <input type="image" alt="Submit" src="pencil_icon.png"  width="20">
                                    </form>
                                </td>
                                <td align="center">
                                    
                                    <%-- FORMULARIO DE EXCLUSAO DE CONTATO --%>
                                    
                                    <form action="ExcluirContato" method="post">
                                        <!-- radio e botao para excluir----- -->
<!--                                        <input type="radio" name="id" value="${contato.id}">
                                        <input type="submit" value="Excluir">-->
                                        <!-- imagem como botao-------------- -->
                                        <input type="hidden" id="exclusao" name="id" value="${contato.id}">
                                        <input type="image" alt="Submit" src="trash_icon.png" width="20">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <br />
        </c:if>
        
        <%-- EXIBICAO DE MENSAGEM DE ERRO DE BANCO DE DADOS AO TENTAR LISTAR CONTATOS --%>
        
        <c:if test="${erroDb == null && not empty erroDbRepository}">
            <h4 style="color:red">Erro ao listar contatos: DB ${erroDbRepository}</h4>
        </c:if>
    </body>
</html>
