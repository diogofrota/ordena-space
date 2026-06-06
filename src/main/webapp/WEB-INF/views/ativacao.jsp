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
                <span class="section-kicker">escala tatico</span>
                <h3 class="section-title">Ativacao de servico</h3>
                <p>Abra uma nova jornada de 12 horas com setor, viatura e equipe operacional.</p>
            </div>

            <div class="split-grid">
                <section class="panel">
                    <form class="form-grid" method="post" action="${pageContext.request.contextPath}/ativacao">
                        <div class="field">
                            <label for="viaturaId">Selecionar viatura</label>
                            <select id="viaturaId" name="viaturaId" required>
                                <option value="">Selecione</option>
                                <c:forEach items="${viaturasDisponiveis}" var="viatura">
                                    <option value="${viatura.id}">${viatura.numero} | ${viatura.placa} | Tablet ${viatura.tabletSatelital}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="field">
                            <label for="setorId">Selecionar setor</label>
                            <select id="setorId" name="setorId" required>
                                <option value="">Selecione</option>
                                <c:forEach items="${setores}" var="setor">
                                    <option value="${setor.id}">${setor.nome}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="field">
                            <label for="policial1Id">Selecionar policial 1</label>
                            <select id="policial1Id" name="policial1Id" required>
                                <option value="">Selecione</option>
                                <c:forEach items="${policiaisDisponiveis}" var="policial">
                                    <option value="${policial.id}">${policial.nomeCompletoOperacional} | RG ${policial.rg}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="field">
                            <label for="policial2Id">Selecionar policial 2 opcional</label>
                            <select id="policial2Id" name="policial2Id">
                                <option value="">Nao alocar</option>
                                <c:forEach items="${policiaisDisponiveis}" var="policial">
                                    <option value="${policial.id}">${policial.nomeCompletoOperacional} | RG ${policial.rg}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="button-row">
                            <button class="btn btn-primary" type="submit">Ativar servico</button>
                        </div>
                    </form>
                </section>

                <section class="panel">
                    <span class="section-kicker">observacao</span>
                    <h3 class="section-title">Regras desta versao</h3>
                    <div class="helper-grid">
                        <div class="helper-card">
                            <h4>Duracao fixa</h4>
                            <p class="subtle">Todo servico nasce com 12 horas de duracao.</p>
                        </div>
                        <div class="helper-card">
                            <h4>Status inicial</h4>
                            <p class="subtle">O fluxo comeca em `AGUARDANDO_GPS` ate a primeira leitura do tablet.</p>
                        </div>
                        <div class="helper-card">
                            <h4>Recursos livres</h4>
                            <p class="subtle">A tela prioriza viaturas e policiais ainda nao alocados em servicos abertos.</p>
                        </div>
                        <div class="helper-card">
                            <h4>Encerramento</h4>
                            <p class="subtle">A finalizacao manual altera o status para `FINALIZADO`.</p>
                        </div>
                    </div>
                </section>
            </div>

            <section class="table-card">
                <h3 class="section-title">Servicos ativos</h3>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Viatura</th>
                            <th>Setor</th>
                            <th>Policial 1</th>
                            <th>Policial 2</th>
                            <th>Inicio</th>
                            <th>Duracao</th>
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
                                <td>${servico.duracaoHoras}h</td>
                                <td>
                                    <div class="table-actions">
                                        <form method="post" action="${pageContext.request.contextPath}/ativacao" onsubmit="return confirm('Finalizar servico?');">
                                            <input type="hidden" name="action" value="finalizar">
                                            <input type="hidden" name="id" value="${servico.id}">
                                            <button class="btn btn-danger" type="submit">Finalizar Servico</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty servicos}">
                            <tr>
                                <td colspan="7">
                                    <div class="empty-state">Nenhum servico ativo no momento.</div>
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
