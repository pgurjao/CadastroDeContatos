<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <title>Edicao de Contatos</title>
    </head>
    <body style="margin: 20px">
        <h1>Editar Contato</h1>
        <hr>
        <h4>Session ID: ${pageContext.session.id}</h4>
        <h4>contato.toString(): ${contato}</h4>
        <form action="SalvarEdicaoContato" method="post">
            <%--<c:set var="idDb" value="${contato.id}" />--%>
            <input type="hidden" name="id" value="${contato.id}">
            <br>
            <h4>Altere os dados desejados abaixo</h4>
            <table border="1" cellpadding="3" cellspacing="0">
                <tr>
                    <td>
                        Nome:
                    </td>
                    <td>
                        <input type="text" name="nome" maxlength="100" value="${contato.nome}">
                    </td>
                </tr>
                <tr>
                    <td>
                        Email:
                    </td>
                    <td>
                        <input type="text" name="email" maxlength="50" value="${contato.email}">
                    </td>
                </tr>
                <tr>
                    <td>
                        Telefone:
                    </td>
                    <td>
                        <input type="text" name="fone" maxlength="50" value="${contato.fone}">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        Endereco <input type="submit" name="alterarEndereco" value="Alterar">
                    </td>
                </tr>
                <tr>
                    <td>
                        CEP:
                    </td>
                    <td>
                        <input type="text" name="cep" maxlength="9" value="${contato.endereco.cep}">
                    </td>
                </tr>
                <tr>
                    <td>
                        Logradouro:
                    </td>
                    <td>
                        <input type="text" name="logradouro" maxlength="50" value="${contato.endereco.logradouro}">
                    </td>
                </tr>
                <tr>
                    <td>
                        Complemento:
                    </td>
                    <td>
                        <input type="text" name="complemento" maxlength="50" value="${contato.endereco.complemento}">
                    </td>
                </tr>
                <tr>
                    <td>
                        Bairro:
                    </td>
                    <td>
                        <input type="text" name="bairro" maxlength="50" value="${contato.endereco.bairro}">
                    </td>
                </tr>
                <tr>
                    <td>
                        Localidade:
                    </td>
                    <td>
                        <input type="text" name="localidade" maxlength="50" value="${contato.endereco.localidade}">
                    </td>
                </tr>
                <tr>
                    <td>
                        UF:
                    </td>
                    <td>
                        <input type="text" name="uf" maxlength="2" value="${contato.endereco.uf}">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" name="salvarContato" value="Salvar contato">
                    </td>
                </tr>
            </table>
        </form>
        <h3><a href="lista_contatos.jsp">← Voltar</a></h3>
        <br>
        <br>
    </body>
</html>
