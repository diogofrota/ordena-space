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
                <span class="section-kicker">campo</span>
                <h3 class="section-title">Monitoramento das equipes</h3>
                <p>Consulte status, coordenadas atuais e abra chamadas simuladas de apoio por voz ou video.</p>
            </div>

            <section class="table-card">
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Viatura</th>
                            <th>Setor</th>
                            <th>Policial 1</th>
                            <th>Policial 2</th>
                            <th>Horario de Inicio</th>
                            <th>Status</th>
                            <th>Latitude Atual</th>
                            <th>Longitude Atual</th>
                            <th>Acoes</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${servicos}" var="servico">
                            <tr>
                                <td>${servico.viatura.numero}</td>
                                <td>${servico.setor.nome}</td>
                                <td>${servico.policial1.nomeCompletoOperacional}</td>
                                <td>${servico.policial2Nome}</td>
                                <td>${servico.dataHoraInicioFormatada}</td>
                                <td><span class="status-pill status-${servico.status}">${servico.status.descricaoExibicao}</span></td>
                                <td>${servico.latitudeAtualFormatada}</td>
                                <td>${servico.longitudeAtualFormatada}</td>
                                <td>
                                    <div class="table-actions">
                                        <button
                                            class="btn btn-voice js-call-trigger"
                                            type="button"
                                            data-type="voz"
                                            data-target="${servico.viatura.numero} - ${servico.policial1.nomeGuerra}">
                                            Chamada de Voz
                                        </button>
                                        <button
                                            class="btn btn-video js-call-trigger"
                                            type="button"
                                            data-type="video"
                                            data-target="${servico.viatura.numero} - ${servico.policial1.nomeGuerra}">
                                            Chamada de Video
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty servicos}">
                            <tr>
                                <td colspan="9">
                                    <div class="empty-state">Nenhuma equipe em monitoramento neste momento.</div>
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

<div class="modal" data-call-modal>
    <div class="modal-card">
        <span class="section-kicker">simulacao</span>
        <h3 class="section-title" data-call-title>Chamada</h3>
        <p class="subtle" data-call-text>Conexao operacional em andamento.</p>
        <div class="tag">Canal seguro de demonstracao</div>
        <div class="modal-actions">
            <button class="btn btn-secondary" type="button" data-call-close>Fechar</button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
