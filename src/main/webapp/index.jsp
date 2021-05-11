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
        <script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/1.10.24/js/dataTables.bootstrap.min.js"></script>
        <!-- --------------------------------------------------------------- -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.datatables.net/1.10.24/css/dataTables.bootstrap.min.css">
        <!-- --------------------------------------------------------------- -->
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
                        <input type="submit" value="Buscar"><input type="submit" value="Salvar">
                    </td>
                </tr>
            </table>
        </form>
        <!--<br />-->
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
        <jsp:useBean id="repository" scope="page" class="br.edu.infnet.infra.ContatoRepository" />
        <c:set var="contatos" value="${repository.listar()}" />
        <c:if test="${not empty contatos}">
            <div style="width: 50%">
                <table id="tabelaContatos" class="table table-striped table-bordered" border="1" cellpadding="3" cellspacing="0">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Nome</th>
                            <th>Email</th>
                            <th>Telefone</th>
                            <th>Editar</th>
                            <th>Excluir</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="contato" items="${contatos}">
                            <tr>
                                <td>${contato.id}</td>
                                <td>${contato.nome}</td>
                                <td>${contato.email}</td>
                                <td>${contato.fone}</td>
                                <td>
                                    <form action="CadastrarContato" method="post">
                                        <input type="hidden" id="edicaoContato" name="idContatoParaEditar" value="${contato.id}">
                                        <input type="hidden" name="edicao" value="true">
                                        <input type="image" alt="Submit" src="pencil_icon.png"  width="">
                                    </form>
                                </td>
                                <td>
                                    <form action="ExcluirContato" method="post">
                                        <!-- radio e botao para excluir----- -->
<!--                                        <input type="radio" name="idContatoParaExcluir" value="${contato.id}">
                                        <input type="submit" value="Excluir">-->
                                        <!-- imagem como botao-------------- -->
                                        <input type="hidden" id="exclusao" name="idContatoParaExcluir" value="${contato.id}">
                                        <input type="image" alt="Submit" src="trash_icon.png"  width="">
                                        <%--<c:set var="idContatoParaExcluir" scope="page" value="${contato.id}" />--%>
                                        <%--<c:set var="idContatoParaExcluir">"${contato.id}"</c:set>
                                        o id eh ${contato.id} --%>
                                        <%-- <u><c:if test="${not empty idParaDeletar}">${idParaDeletar}</c:if></u> --%>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <br />
        </c:if>
    </body>
</html>
