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
                <h3 class="section-title">Viaturas cadastradas</h3>
                <p>Gerencie a frota conectada ao sistema e remova registros que nao devem permanecer ativos.</p>
            </div>

            <section class="table-card">
                <div class="button-row">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/viaturas?action=form">Nova viatura</a>
                </div>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Numero</th>
                            <th>Placa</th>
                            <th>Tablet Satelital</th>
                            <th>Acoes</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${viaturas}" var="viatura">
                            <tr>
                                <td>${viatura.numero}</td>
                                <td>${viatura.placa}</td>
                                <td>${viatura.tabletSatelital}</td>
                                <td>
                                    <div class="table-actions">
                                        <form method="post" action="${pageContext.request.contextPath}/viaturas" onsubmit="return confirm('Excluir viatura?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${viatura.id}">
                                            <button class="btn btn-danger" type="submit">Excluir</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty viaturas}">
                            <tr>
                                <td colspan="4">
                                    <div class="empty-state">Nenhuma viatura cadastrada.</div>
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </section>
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
