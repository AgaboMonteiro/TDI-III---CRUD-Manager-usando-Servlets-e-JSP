
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-br">

<head>
    <%@include file="base-head.jsp"%>
</head>

<body>
    <%@include file="nav-menu.jsp"%>

    <div id="container" class="container-fluid">

        <h3 class="page-header">Adicionar Equipamento</h3>

        <form action="${pageContext.request.contextPath}/equipamento/${action}" method="POST">

            <input type="hidden" value="${equipamento.getId()}" name="equipamentoId">

            <div class="row">

                <div class="form-group col-md-6">
                    <label for="nome">Nome</label>
                    <input type="text" class="form-control"
                           id="nome" name="nome"
                           autofocus="autofocus"
                           placeholder="Nome do equipamento"
                           required
                           oninvalid="this.setCustomValidity('Por favor, informe o nome do equipamento.')"
                           oninput="setCustomValidity('')"
                           value="${equipamento.getNome()}">
                </div>

                <div class="form-group col-md-6">
                    <label>Tipo</label>

                    <div>
                        <label>
                            <input type="radio" name="tipo" value="Computador"
                                ${equipamento.getTipo() == 'Computador' ? "checked" : ""}>
                            Computador
                        </label>

                        <label>
                            <input type="radio" name="tipo" value="Impressora"
                                ${equipamento.getTipo() == 'Impressora' ? "checked" : ""}>
                            Impressora
                        </label>

                        <label>
                            <input type="radio" name="tipo" value="Outro"
                                ${equipamento.getTipo() == 'Outro' ? "checked" : ""}>
                            Outro
                        </label>
                    </div>
                </div>

            </div>

            <div class="row">

                <div class="form-group col-md-4">
                    <label for="dataAquisicao">Data de aquisição</label>
                    <input type="date" class="form-control"
                           id="dataAquisicao" name="dataAquisicao"
                           required
                           value="${equipamento.getDataAquisicao()}">
                </div>

                <div class="form-group col-md-4">
                    <label for="valor">Valor</label>
                    <input type="number" class="form-control"
                           id="valor" name="valor"
                           step="0.01" min="0"
                           placeholder="Valor do equipamento"
                           required
                           value="${equipamento.getValor()}">
                </div>

                <div class="form-group col-md-4">
                    <label for="user">Usuário responsável</label>

                    <select id="user" class="form-control selectpicker"
                            name="user" required>

                        <option value="" disabled
                            ${empty equipamento ? "selected" : ""}>
                            Selecione um usuário
                        </option>

                        <c:forEach var="user" items="${users}">
                            <option value="${user.getId()}"
                                ${not empty equipamento and equipamento.getUser().getId() == user.getId() ? "selected" : ""}>
                                ${user.getName()}
                            </option>
                        </c:forEach>

                    </select>
                </div>

            </div>

            <div class="row">
                <div class="form-group col-md-12">
                    <label for="descricao">Descrição</label>
                    <textarea class="form-control"
                              id="descricao" name="descricao"
                              rows="4"
                              placeholder="Descrição do equipamento">${equipamento.getDescricao()}</textarea>
                </div>
            </div>

            <hr />

            <div id="actions" class="row pull-right">
                <div class="col-md-12">

                    <a href="${pageContext.request.contextPath}/equipamentos"
                       class="btn btn-default">Cancelar</a>

                    <button type="submit" class="btn btn-primary">
                        ${not empty equipamento ? "Alterar Equipamento" : "Cadastrar Equipamento"}
                    </button>

                </div>
            </div>

        </form>
    </div>

    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>

</body>
</html>
