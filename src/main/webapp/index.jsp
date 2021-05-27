<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <title>Login Sistema Cadastro de Contatos</title>

    </head>
    <body style="margin: 20px">
        <h1>Login Sistema Cadastro de Contatos</h1>
        <hr>
<%--        <div style="border: 5px outset red; background-color: lightblue; text-align: center;">
            <h2>DEBUG</h2>
            <h3><p><b>Session ID: </b>${pageContext.session.id}</p><br>
            <p><b>Usuario.toString(): </b>${usuario}</p>
            <p><b>Erros: </b>${erros}</p>
            <p><b>ErroDb: </b>${erroDb}</p></h3>
        </div>--%>
        <br>

        <%-- FORMULARIO DE INCLUSAO DE CONTATO --%>

        <form action="Login" method="post">
            <table border="1" cellpadding="3" cellspacing="0">
                <tr>
                    <td>
                        Usuario:
                    </td>
                    <td>
                        <input type="text" name="usuarioNome" autofocus maxlength="20" value="${usuario.nome}">
                    </td>
                </tr>
                <tr>
                    <td>
                        Senha:
                    </td>
                    <td>
                        <input type="password" name="senha" maxlength="50" value="${usuario.senha}">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="Entrar">
                    </td>
                </tr>
            </table>
        </form>
        <br>

        <%-- EXIBICAO DE MENSAGENS DE SUCESSO OU ERRO AO SALVAR CONTATOS --%>

        <c:if test="${not empty sucesso}">
            <h4 style="color:green">${sucesso}</h4>
        </c:if>
        <c:if test="${not empty erros}">
            <c:forEach var="erro" items="${erros}">
                <h4 style="color:red">Erro: ${erro}</h4>
            </c:forEach>
        </c:if>
        <c:if test="${not empty erroDb}">
            <h4 style="color:brown">Erro ao acessar banco de dados: ${erroDb}</h4>
        </c:if>
        <br>
    </body>
</html>
