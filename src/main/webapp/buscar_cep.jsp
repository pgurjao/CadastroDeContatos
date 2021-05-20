<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <title>Buscar CEP</title>
    </head>
    <body style="margin: 20px">
        <h1>Buscar CEP<h4>[<a href="Logout">Logout</a>]</h4></h1>
        <hr>
        <div style="border: 5px outset red; background-color: lightblue; text-align: center;">
            <h2>DEBUG</h2>
            <h3><p><b>Session ID: </b>${pageContext.session.id}<br>
            <b>contato.toString(): </b>${contato}</p>
            <b>Session Owner: </b>${sessionScope.usuarioNome}</p></h3>
        </div>
        <br>
        <form action="PuxarCepApi" method="post">
            <input type="text" name="cep">
            <input type="submit" value="Buscar">
        </form>
        <c:if test="${not empty erros}">
                <c:forEach var="erro1" items="${erros}">
                    <h4 style="color:red">Erro: ${erro1}</h4>
                </c:forEach>
        </c:if>
        <h3>${endereco.cep}</h3>
        <h3>${endereco.logradouro}</h3>
        <h3>${endereco.bairro}</h3>
        <h3>${endereco.uf}</h3>
        <br>    
        <h3><a href="editar_contato.jsp">← Voltar</a></h3>
        <br>
        <br>
    </body>
</html>
