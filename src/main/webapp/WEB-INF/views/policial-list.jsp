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
                <h3 class="section-title">Policiais cadastrados</h3>
                <p>Base de guarnicoes disponivel para escala e acionamento em campo.</p>
            </div>

            <section class="table-card">
                <div class="button-row">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/policiais?action=form">Novo policial</a>
                </div>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>RG</th>
                            <th>Posto ou Graduacao</th>
                            <th>Nome</th>
                            <th>Acoes</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${policiais}" var="policial">
                            <tr>
                                <td>${policial.rg}</td>
                                <td>${policial.graduacao}</td>
                                <td>${policial.nomeGuerra}</td>
                                <td>
                                    <div class="table-actions">
                                        <form method="post" action="${pageContext.request.contextPath}/policiais" onsubmit="return confirm('Excluir policial?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${policial.id}">
                                            <button class="btn btn-danger" type="submit">Excluir</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty policiais}">
                            <tr>
                                <td colspan="4">
                                    <div class="empty-state">Nenhum policial cadastrado.</div>
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
