<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/includes/head.jspf" %>
<body class="login-body">
<div class="login-grid">
    <section class="login-aside">
        <div>
            <span class="login-kicker">ordena space</span>
            <h1>Patrulhamento com comando, mapa e resposta imediata.</h1>
            <p class="login-copy">
                Painel web para monitorar viaturas com tablets satelitais, ativar servicos, desenhar setores e acompanhar alertas operacionais.
            </p>
        </div>

        <div class="login-badges">
            <span class="login-badge">Tomcat 10</span>
            <span class="login-badge">JSP + Servlet</span>
            <span class="login-badge">Oracle JDBC</span>
        </div>
    </section>

    <section class="login-card">
        <span class="section-kicker">acesso seguro</span>
        <h2>Entrar no painel</h2>
        <p class="subtle">Usuario inicial: policial@policial.com / caveira</p>

        <c:if test="${not empty error}">
            <div class="flash flash-danger">
                <span>${error}</span>
            </div>
        </c:if>
        <%@ include file="/WEB-INF/views/includes/flash.jspf" %>

        <form class="form-grid" method="post" action="${pageContext.request.contextPath}/login">
            <div class="field">
                <label for="email">Email</label>
                <input id="email" name="email" type="email" placeholder="policial@policial.com" value="${email}" required>
            </div>

            <div class="field">
                <label for="senha">Senha</label>
                <input id="senha" name="senha" type="password" placeholder="Informe sua senha" required>
            </div>

            <div class="button-row">
                <button class="btn btn-primary" type="submit">Acessar</button>
            </div>
        </form>
    </section>
</div>
</body>
</html>
