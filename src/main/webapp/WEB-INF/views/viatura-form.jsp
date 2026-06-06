<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/includes/head.jspf" %>
<body>
<div class="layout-shell">
    <%@ include file="/WEB-INF/views/includes/sidebar.jspf" %>
    <main class="content-shell">
        <%@ include file="/WEB-INF/views/includes/topbar.jspf" %>
        <section class="page-stack">
            <%@ include file="/WEB-INF/views/includes/flash.jspf" %>
            <div class="page-head">
                <span class="section-kicker">frota</span>
                <h3 class="section-title">Cadastrar viatura</h3>
                <p>Informe identificacao operacional, placa e o tablet satelital vinculado.</p>
            </div>

            <section class="panel">
                <c:if test="${not empty error}">
                    <div class="flash flash-danger">
                        <span>${error}</span>
                    </div>
                </c:if>
                <form class="form-grid" method="post" action="${pageContext.request.contextPath}/viaturas">
                    <div class="field">
                        <label for="numero">Numero da viatura</label>
                        <input id="numero" name="numero" type="text" value="${viatura.numero}" placeholder="54-1001" required>
                    </div>

                    <div class="field">
                        <label for="placa">Placa</label>
                        <input id="placa" name="placa" type="text" value="${viatura.placa}" placeholder="RIO5A01" required>
                    </div>

                    <div class="field">
                        <label for="tabletSatelital">Tablet satelital</label>
                        <input id="tabletSatelital" name="tabletSatelital" type="text" value="${viatura.tabletSatelital}" placeholder="80001" required>
                    </div>

                    <div class="button-row">
                        <button class="btn btn-primary" type="submit">Salvar viatura</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/viaturas">Ver listagem</a>
                    </div>
                </form>
            </section>
        </section>
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
