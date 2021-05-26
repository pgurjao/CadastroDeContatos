<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cadastro de Contatos</title>
        <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
        <!-- ARQUIVOS .js NECESSARIOS PARA DATATABLES -->
        <!--<script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>-->
        <script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js" integrity="sha256-d0qcJpwLkJL+K8wbZdFutWDK0aNMgLJ4sSLIV9o4AlE=" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/1.10.24/js/dataTables.bootstrap.min.js" integrity="sha256-H/ZJHj902eqGocNJYjkD3OButj68n+T2NSBjnfV2Qok=" crossorigin="anonymous"></script>
        <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
        <!-- ARQUIVOS .css NECESSARIOS PARA DATATABLES -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.datatables.net/1.10.24/css/dataTables.bootstrap.min.css">
        <!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
        <!-- SCRIPT PARA CARREGAR DATATABLES EM PORTUGUES -->
        <script type="text/javascript">
            $(document).ready(function () {
                $('#tabelaContatos').DataTable({
                    "language": {
                        "url": "https://cdn.datatables.net/plug-ins/1.10.24/i18n/Portuguese-Brasil.json"
                    }
                });
            });
        </script>
    </head>
    <body style="margin: 20px">
        <h1>Cadastro de Contatos<h4>[<a href="Logout">Logout</a>]</h4></h1>
        <hr>
        <div style="border: 5px outset red; background-color: lightblue; text-align: center;">
            <h2>DEBUG - lista_contatos</h2>
            <h3><p><b>Session ID: </b>${pageContext.session.id}<br>
                    <b>contato.toString:</b>${contato}<br>
                    <b>Session Owner: </b>${sessionScope.usuarioNome}</p></h3>
        </div>

        <%-- TESTA SE PARAMETRO "usuarioNome" FOI PASSADO E EXIBE ERRO SE NAO FOI --%>

        <c:if test="${sessionScope.usuarioNome == null}">
            <br>
            <h4 style="color:red">Erro na requisicao: session owner (usuario) == null</h4>
        </c:if>

        <%-- TESTA SE PARAMETRO "usuarioNome" FOI PASSADO E EXIBE O RESTO DA PAGINA SE FOI --%>

        <c:if test="${sessionScope.usuarioNome != null}">

            <!--<h4>usuario: ${usuario.usuario}</h4>-->
            <!--<h4>senha: ${usuario.senha}</h4>-->
            <br>

            <%-- FORMULARIO DE INCLUSAO DE CONTATO --%>

            <form action="CadastrarContato" method="post"  accept-charset="UTF-8">
                <table border="1" cellpadding="3" cellspacing="0">
                    <tr>
                        <td>
                            Nome:
                        </td>
                        <td>
                            <input type="text" name="nome" autofocus maxlength="100" value="${contato.nome}">
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
                            <input type="submit" value="Cadastrar">
                        </td>
                    </tr>
                </table>
            </form>
            <br>

            <%-- EXIBICAO DE MENSAGENS DE SUCESSO OU ERRO AO SALVAR CONTATOS --%>

            <c:if test="${not empty sucesso && empty erros && empty erroDb && empty errosEndereco}">
                <h4 style="color:green">${sucesso}</h4>
            </c:if>
            <c:if test="${not empty contatoNome}">
                <h4 style="color:brown"><u>Erro ao gravar contato: '${contatoNome}'</u></h4>
            </c:if>
            <c:if test="${not empty erros}">
                <c:forEach var="erro" items="${erros}">
                    <h4 style="color:red">Erro: ${erro}</h4>
                </c:forEach>
            </c:if>
            <c:if test="${not empty erroDb}">
                <h4 style="color:brown">Erro ao gravar contato: ${erroDb}</h4>
            </c:if>
            <c:if test="${not empty errosEndereco}">
                <c:forEach var="erro" items="${errosEndereco}">
                    <h4 style="color:red">Erro: ${erro}</h4>
                </c:forEach>
            </c:if>
            <br>

            <c:if test="${sessionScope.usuarioNome == null}">
                <h4 style="color:red">Erro na requisicao: usuario == null</h4>
            </c:if>

            <%-- CHAMADA DE ContatoRepository DIRETAMENTE, SEM USAR O SERVLET, PARA LISTAR TODOS OS CONTATOS NO BANCO DE DADOS --%>

            <c:if test="${sessionScope.usuarioNome != null}">
                <jsp:useBean id="repository" scope="page" class="br.edu.infnet.infra.ContatoRepository" />
                <c:set var="contatos" value="${repository.listar(sessionScope.usuarioNome)}" />


                <%-- CRIA VARIAVEL 'erroDbRepository' PARA SALVAR EVENTUAL MENSAGEM DE ERRO AO CHAMAR 'repository.listar()' ACIMA --%>

                <c:set var="erroDbRepository" value="${repository.getErroDbRepository()}" />
                <c:if test="${erroDbRepository != null}">
                    <h4 style="color:red">Erro repository: ${erroDbRepository}</h4>
                </c:if>

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
                                    <th>Endereco</th>
                                    <th>Owner</th>
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
                                        <c:if test="${not empty contato.endereco}">
                                            <td>${contato.endereco.logradouro}, ${contato.endereco.complemento} - ${contato.endereco.bairro} - ${contato.endereco.localidade}-${contato.endereco.uf}, CEP ${contato.endereco.cep}</td>
                                        </c:if>
                                        <c:if test="${empty contato.endereco}">
                                            <td></td>
                                        </c:if>
                                        <td>${contato.usuario}</td>
                                        <td align="center">

                                            <%-- FORMULARIO DE EDICAO DE CONTATO --%>

                                            <form action="EditarContato" method="post">
                                                <input type="hidden" name="id" value="${contato.id}">
                                                <input type="image" alt="Submit" src="pencil_icon.png">
                                            </form>
                                        </td>
                                        <td align="center">

                                            <%-- FORMULARIO DE EXCLUSAO DE CONTATO --%>

                                            <form action="ExcluirContato" method="post">
                                                <!-- radio e botao para excluir -->
                                                <!-- <input type="radio" name="id" value="${contato.id}">
                                                <input type="submit" value="Excluir"> -->
                                                <!-- imagem como botao -->
                                                <input type="hidden" id="exclusao" name="id" value="${contato.id}">
                                                <input type="image" alt="Submit" src="trash_icon.png">
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <br>
                </c:if>
                <c:if test="${empty contatos}">
                    <h4 style="color:red">Erro ao listar contatos: Voce ainda nao tem contatos salvos</h4>
                </c:if>
            </c:if>
            <%-- EXIBICAO DE MENSAGEM DE ERRO DE BANCO DE DADOS AO TENTAR LISTAR CONTATOS --%>

            <c:if test="${erroDb == null && not empty erroDbRepository}">
                <h4 style="color:red">Erro ao listar contatos: DB ${erroDbRepository}</h4>
            </c:if>
        </c:if>
    </body>
</html>
