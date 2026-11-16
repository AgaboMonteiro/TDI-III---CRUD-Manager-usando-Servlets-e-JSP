
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Login - CRUD Manager</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>

<body class="bg-light">

    <div class="container">
        <div class="row justify-content-center align-items-center"
             style="min-height: 100vh;">

            <div class="col-md-5 col-lg-4">

                <div class="card shadow">
                    <div class="card-body p-4">

                        <h3 class="text-center mb-4">
                            CRUD Manager
                        </h3>

                        <p class="text-center text-muted">
                            Entre com suas credenciais
                        </p>

                        <% if ("true".equals(request.getParameter("erro"))) { %>
                            <div class="alert alert-danger" role="alert">
                                E-mail ou senha inválidos.
                            </div>
                        <% } %>

                        <form action="${pageContext.request.contextPath}/login"
                              method="POST">

                            <div class="mb-3">
                                <label for="user_login" class="form-label">
                                    E-mail
                                </label>

                                <input type="email"
                                       class="form-control"
                                       id="user_login"
                                       name="user_login"
                                       placeholder="Digite seu e-mail"
                                       required>
                            </div>

                            <div class="mb-3">
                                <label for="user_pw" class="form-label">
                                    Senha
                                </label>

                                <input type="password"
                                       class="form-control"
                                       id="user_pw"
                                       name="user_pw"
                                       placeholder="Digite sua senha"
                                       required>
                            </div>

                            <button type="submit"
                                    class="btn btn-primary w-100">
                                Entrar
                            </button>

                        </form>

                    </div>
                </div>

            </div>
        </div>
    </div>

</body>
</html>
