
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html lang="pt-br">

<head>
    <%@include file="base-head.jsp"%>
</head>

<body>

    <%@include file="modal.html"%>
    <%@include file="nav-menu.jsp"%>

    <div id="container" class="container-fluid">

        <!-- Mensagem de sucesso ou erro -->
        <div id="alert"
             style="${not empty message ? 'display: block;' : 'display: none;'}"
             class="alert alert-dismissable ${alertType eq 1 ? 'alert-success' : 'alert-danger'}">

            <a href="#" class="close" data-dismiss="alert"
               aria-label="close">&times;</a>

            ${message}

        </div>

        <!-- Cabeçalho -->
        <div id="top" class="row">

            <div class="col-md-3">
                <h3>Manutenções</h3>
            </div>

            <div class="col-md-6">
                <div class="input-group h2">

                    <input name="data[search]"
                           class="form-control"
                           id="search"
                           type="text"
                           placeholder="Pesquisar manutenções">

                    <span class="input-group-btn">
                        <button class="btn btn-danger" type="submit">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </span>

                </div>
            </div>

            <div class="col-md-3">

                <a href="${pageContext.request.contextPath}/manutencao/form"
                   class="btn btn-danger pull-right h2">

                    <span class="glyphicon glyphicon-plus"></span>
                    &nbsp;Adicionar Manutenção

                </a>

            </div>

        </div>

        <hr />

        <!-- Tabela de manutenções -->
        <div id="list" class="row">

            <div class="table-responsive col-md-12">

                <table class="table table-striped table-hover"
                       cellspacing="0" cellpadding="0">

                    <thead>

                        <tr>
                            <th>Título</th>
                            <th>Tipo</th>
                            <th>Data</th>
                            <th>Custo</th>
                            <th>Descrição</th>
                            <th>Equipamento</th>
                            <th>Editar</th>
                            <th>Excluir</th>
                        </tr>

                    </thead>

                    <tbody>

                        <c:forEach var="manutencao" items="${manutencoes}">

                            <tr>

                                <td>${manutencao.getTitulo()}</td>

                                <td>${manutencao.getTipo()}</td>

                                <td>${manutencao.getDataManutencao()}</td>

                                <td>${manutencao.getCusto()}</td>

                                <td>${manutencao.getDescricao()}</td>

                                <td>${manutencao.getEquipamento().getNome()}</td>

                                <!-- Editar -->
                                <td class="actions">

                                    <a class="btn btn-info btn-xs"
                                       href="${pageContext.request.contextPath}/manutencao/update?manutencaoId=${manutencao.getId()}">

                                        <span class="glyphicon glyphicon-edit"></span>

                                    </a>

                                </td>

                                <!-- Excluir -->
                                <td class="actions">

                                    <a class="btn btn-danger btn-xs modal-remove"
                                       manutencao-id="${manutencao.getId()}"
                                       manutencao-titulo="${manutencao.getTitulo()}"
                                       data-toggle="modal"
                                       data-target="#delete-modal"
                                       href="#">

                                        <span class="glyphicon glyphicon-trash"></span>

                                    </a>

                                </td>

                            </tr>

                        </c:forEach>

                    </tbody>

                </table>

            </div>

        </div>

        <!-- Paginação visual -->
		<div id="bottom" class="row">
		    <div class="col-md-12">
		        <ul class="pagination">
		
		            <%-- Botão Anterior --%>
		            <c:choose>
		                <c:when test="${currentPage > 1}">
		                    <li>
		                        <a href="${pageContext.request.contextPath}/manutencoes?page=${currentPage - 1}">
		                            &lt; Anterior
		                        </a>
		                    </li>
		                </c:when>
		                <c:otherwise>
		                    <li class="disabled">
		                        <a>&lt; Anterior</a>
		                    </li>
		                </c:otherwise>
		            </c:choose>
		
		            <%-- Números das páginas --%>
		            <c:forEach begin="1" end="${totalPages}" var="page">
		                <li class="${page == currentPage ? 'active' : ''}">
		                    <a href="${pageContext.request.contextPath}/manutencoes?page=${page}">
		                        ${page}
		                    </a>
		                </li>
		            </c:forEach>
		
		            <%-- Botão Próximo --%>
		            <c:choose>
		                <c:when test="${currentPage < totalPages}">
		                    <li class="next">
		                        <a href="${pageContext.request.contextPath}/manutencoes?page=${currentPage + 1}">
		                            Próximo &gt;
		                        </a>
		                    </li>
		                </c:when>
		                <c:otherwise>
		                    <li class="disabled">
		                        <a>Próximo &gt;</a>
		                    </li>
		                </c:otherwise>
		            </c:choose>
		
		        </ul>
		    </div>
		</div>

    </div>

    <!-- Scripts -->
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

    <script type="text/javascript">

        $(document).ready(function() {

            // Fecha o alerta após 3 segundos
            setTimeout(function() {
                $("#alert").slideUp(500);
            }, 3000);

            // Configura o modal de exclusão
            $(".modal-remove").click(function () {

                var manutencaoTitulo = $(this).attr('manutencao-titulo');

                var manutencaoId = $(this).attr('manutencao-id');

                $(".modal-body #hiddenValue").text(
                    "a manutenção '" + manutencaoTitulo + "'"
                );

                $("#id").attr("value", manutencaoId);

                $("#form").attr(
                    "action",
                    "${pageContext.request.contextPath}/manutencao/delete"
                );

            });

        });

    </script>

</body>

</html>
