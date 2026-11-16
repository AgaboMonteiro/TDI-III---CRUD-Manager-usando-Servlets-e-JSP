
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

        <h3 class="page-header">Adicionar Manutenção</h3>

        <form action="${pageContext.request.contextPath}/manutencao/${action}" method="POST">

            <input type="hidden" value="${manutencao.getId()}" name="manutencaoId">

            <div class="row">

                <div class="form-group col-md-6">
                    <label for="titulo">Título</label>
                    <input type="text" class="form-control"
                           id="titulo" name="titulo"
                           autofocus="autofocus"
                           placeholder="Título da manutenção"
                           required
                           oninvalid="this.setCustomValidity('Por favor, informe o título da manutenção.')"
                           oninput="setCustomValidity('')"
                           value="${manutencao.getTitulo()}">
                </div>

                <div class="form-group col-md-6">
                    <label>Tipo</label>
                    <div>
                        <label>
                            <input type="radio" name="tipo" value="Preventiva"
                                ${manutencao.getTipo() == 'Preventiva' ? "checked" : ""}>
                            Preventiva
                        </label>

                        <label>
                            <input type="radio" name="tipo" value="Corretiva"
                                ${manutencao.getTipo() == 'Corretiva' ? "checked" : ""}>
                            Corretiva
                        </label>

                        <label>
                            <input type="radio" name="tipo" value="Preditiva"
                                ${manutencao.getTipo() == 'Preditiva' ? "checked" : ""}>
                            Preditiva
                        </label>
                    </div>
                </div>

            </div>

            <div class="row">

                <div class="form-group col-md-4">
                    <label for="dataManutencao">Data da manutenção</label>
                    <input type="date" class="form-control"
                           id="dataManutencao" name="dataManutencao"
                           required
                           value="${manutencao.getDataManutencao()}">
                </div>

                <div class="form-group col-md-4">
                    <label for="custo">Custo</label>
                    <input type="number" class="form-control"
                           id="custo" name="custo"
                           step="0.01" min="0"
                           placeholder="Custo da manutenção"
                           required
                           value="${manutencao.getCusto()}">
                </div>

                <div class="form-group col-md-4">
                    <label for="equipamento">Equipamento</label>
                    <select id="equipamento" class="form-control selectpicker"
                            name="equipamento" required>

                        <option value="" disabled
                            ${empty manutencao ? "selected" : ""}>
                            Selecione um equipamento
                        </option>

                        <c:forEach var="equipamento" items="${equipamentos}">
                            <option value="${equipamento.getId()}"
                                ${not empty manutencao and manutencao.getEquipamento().getId() == equipamento.getId() ? "selected" : ""}>
                                ${equipamento.getNome()}
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
                              placeholder="Descrição da manutenção">${manutencao.getDescricao()}</textarea>
                </div>

            </div>

            <hr />

            <div id="actions" class="row pull-right">

                <div class="col-md-12">

                    <a href="${pageContext.request.contextPath}/manutencoes"
                       class="btn btn-default">Cancelar</a>

                    <button type="submit" class="btn btn-primary">
                        ${not empty manutencao ? "Alterar Manutenção" : "Cadastrar Manutenção"}
                    </button>

                </div>

            </div>

        </form>

    </div>

    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>

</body>
</html>
