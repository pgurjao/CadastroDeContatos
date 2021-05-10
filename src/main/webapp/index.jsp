<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cadastro de Contatos</title>
        <!--<script type="javascript"></script>-->

    </head>
    
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
                        <input type="submit" value="Salvar">
                    </td>
                </tr>
            </table>
        </form>
        <c:if test="${not empty sucesso}">
            <h3 style="color:green">${sucesso}</h3>
        </c:if>
        <c:if test="${not empty erros}">
            <c:forEach var="erro" items="${erros}">
                <h3 style="color:red">${erro}</h3>
            </c:forEach>
        </c:if>
        <c:if test="${erroDb != null}">
            <h3 style="color:brown">Erro ao gravar contato: ${erroDb}</h3>
        </c:if>
</html>
