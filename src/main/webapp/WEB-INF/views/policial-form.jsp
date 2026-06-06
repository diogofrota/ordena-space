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
                <span class="section-kicker">efetivo</span>
                <h3 class="section-title">Cadastrar policial</h3>
                <p>Mantenha a base operacional pronta para ativacao de servicos e monitoramento.</p>
            </div>

            <section class="panel">
                <c:if test="${not empty error}">
                    <div class="flash flash-danger">
                        <span>${error}</span>
                    </div>
                </c:if>
                <form class="form-grid" method="post" action="${pageContext.request.contextPath}/policiais">
                    <div class="field">
                        <label for="rg">RG</label>
                        <input id="rg" name="rg" type="text" value="${policial.rg}" placeholder="70001" required>
                    </div>

                    <div class="field">
                        <label for="graduacao">Posto ou graduacao</label>
                        <select id="graduacao" name="graduacao" required>
                            <option value="">Selecione</option>
                            <option value="Soldado" ${policial.graduacao eq 'Soldado' ? 'selected' : ''}>Soldado</option>
                            <option value="Cabo" ${policial.graduacao eq 'Cabo' ? 'selected' : ''}>Cabo</option>
                            <option value="Sargento" ${policial.graduacao eq 'Sargento' ? 'selected' : ''}>Sargento</option>
                        </select>
                    </div>

                    <div class="field">
                        <label for="nomeGuerra">Nome de guerra</label>
                        <input id="nomeGuerra" name="nomeGuerra" type="text" value="${policial.nomeGuerra}" placeholder="Falcao" required>
                    </div>

                    <div class="button-row">
                        <button class="btn btn-primary" type="submit">Salvar policial</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/policiais">Ver listagem</a>
                    </div>
                </form>
            </section>
        </section>
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
