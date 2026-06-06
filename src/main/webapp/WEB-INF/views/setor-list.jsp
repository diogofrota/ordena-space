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
                <span class="section-kicker">setores</span>
                <h3 class="section-title">Setores cadastrados</h3>
                <p>Visualize cada poligono pelas coordenadas operacionais marcadas em campo.</p>
            </div>

            <section class="table-card">
                <div class="button-row">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/setores?action=form">Novo setor</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/setores/mapa">Abrir mapa</a>
                </div>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Setor</th>
                            <th>Ponto 1</th>
                            <th>Ponto 2</th>
                            <th>Ponto 3</th>
                            <th>Ponto 4</th>
                            <th>Ponto 5</th>
                            <th>Ponto 6</th>
                            <th>Acoes</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${setores}" var="setor">
                            <tr>
                                <td>${setor.nome}</td>
                                <td>${setor.ponto1Resumo}</td>
                                <td>${setor.ponto2Resumo}</td>
                                <td>${setor.ponto3Resumo}</td>
                                <td>${setor.ponto4Resumo}</td>
                                <td>${setor.ponto5Resumo}</td>
                                <td>${setor.ponto6Resumo}</td>
                                <td>
                                    <div class="table-actions">
                                        <form method="post" action="${pageContext.request.contextPath}/setores" onsubmit="return confirm('Excluir setor?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${setor.id}">
                                            <button class="btn btn-danger" type="submit">Excluir</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty setores}">
                            <tr>
                                <td colspan="8">
                                    <div class="empty-state">Nenhum setor cadastrado.</div>
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
