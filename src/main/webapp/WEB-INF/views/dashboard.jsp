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
                <span class="section-kicker">visao geral</span>
                <h3 class="section-title">Centro de controle operacional</h3>
                <p>Consolide frota, efetivo, setores e servicos em um unico painel pronto para operacao e deploy.</p>
            </div>

            <div class="metrics-grid">
                <article class="metric-card">
                    <div class="metric-label">Total de Viaturas</div>
                    <div class="metric-value">${metrics.totalViaturas}</div>
                </article>
                <article class="metric-card">
                    <div class="metric-label">Total de Policiais</div>
                    <div class="metric-value">${metrics.totalPoliciais}</div>
                </article>
                <article class="metric-card">
                    <div class="metric-label">Total de Setores</div>
                    <div class="metric-value">${metrics.totalSetores}</div>
                </article>
                <article class="metric-card">
                    <div class="metric-label">Servicos Ativos</div>
                    <div class="metric-value">${metrics.servicosAtivos}</div>
                </article>
            </div>

            <div class="dashboard-grid">
                <article class="panel">
                    <span class="section-kicker">fluxo rapido</span>
                    <h3 class="section-title">Acoes prioritarias</h3>
                    <div class="helper-grid">
                        <a class="helper-card" href="${pageContext.request.contextPath}/ativacao">
                            <h4>Ativar Servico</h4>
                            <p class="subtle">Selecione viatura, setor e equipe para iniciar uma jornada de 12 horas.</p>
                        </a>
                        <a class="helper-card" href="${pageContext.request.contextPath}/monitoramento">
                            <h4>Monitoramento</h4>
                            <p class="subtle">Acompanhe status, coordenadas e chamadas simuladas em tempo real.</p>
                        </a>
                        <a class="helper-card" href="${pageContext.request.contextPath}/setores/mapa">
                            <h4>Mapa de Setores</h4>
                            <p class="subtle">Veja o perimetro de todos os setores cadastrados sobre o mapa da cidade.</p>
                        </a>
                        <a class="helper-card" href="${pageContext.request.contextPath}/viaturas?action=form">
                            <h4>Nova Viatura</h4>
                            <p class="subtle">Cadastre rapidamente novas viaturas e tablets satelitais da operacao.</p>
                        </a>
                    </div>
                </article>

                <article class="panel">
                    <span class="section-kicker">diretrizes</span>
                    <h3 class="section-title">Leituras de uso</h3>
                    <div class="helper-grid">
                        <div class="helper-card">
                            <h4>Status GPS</h4>
                            <p class="subtle">`AGUARDANDO_GPS` no inicio, `NORMAL` dentro do poligono e `ALERTA` ao sair do setor.</p>
                        </div>
                        <div class="helper-card">
                            <h4>Geofencing</h4>
                            <p class="subtle">Cada setor aceita ate 6 vertices GPS para desenho do poligono operacional.</p>
                        </div>
                        <div class="helper-card">
                            <h4>Login inicial</h4>
                            <p class="subtle">Use o acesso administrativo seed para validar a primeira subida local.</p>
                        </div>
                        <div class="helper-card">
                            <h4>Persistencia</h4>
                            <p class="subtle">Toda a camada web trabalha com JDBC Oracle e servlets Jakarta, sem Spring.</p>
                        </div>
                    </div>
                </article>
            </div>
        </section>
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
