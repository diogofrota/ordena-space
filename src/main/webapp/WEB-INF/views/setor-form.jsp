<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="leafletEnabled" value="true" scope="request" />
<%@ include file="/WEB-INF/views/includes/head.jspf" %>
<body>
<div class="layout-shell">
    <%@ include file="/WEB-INF/views/includes/sidebar.jspf" %>
    <main class="content-shell">
        <%@ include file="/WEB-INF/views/includes/topbar.jspf" %>
        <section class="page-stack">
            <%@ include file="/WEB-INF/views/includes/flash.jspf" %>
            <div class="page-head">
                <span class="section-kicker">geofencing</span>
                <h3 class="section-title">Cadastrar setor por poligono</h3>
                <p>Marque entre 3 e 6 pontos no mapa para definir o perimetro operacional do setor.</p>
            </div>

            <c:if test="${not empty error}">
                <div class="flash flash-danger">
                    <span>${error}</span>
                </div>
            </c:if>

            <div class="split-grid">
                <section class="map-card">
                    <div class="field">
                        <label for="nome">Nome do setor</label>
                        <input id="nome" name="nome" form="setorForm" type="text" value="${draftSetorNome}" placeholder="Ex.: Copacabana" required>
                    </div>
                    <div id="setorMap" class="map-frame"></div>
                    <p class="point-table-note">Clique no mapa para adicionar pontos em ordem. Use o botao limpar para recomecar.</p>
                </section>

                <section class="table-card">
                    <form id="setorForm" class="form-grid" method="post" action="${pageContext.request.contextPath}/setores">
                        <input id="pointsData" type="hidden" name="pointsData" value="[]">
                        <div class="button-row">
                            <button id="saveSetorButton" class="btn btn-primary" type="submit">Salvar setor</button>
                            <button id="clearPointsButton" class="btn btn-secondary" type="button">Limpar pontos</button>
                        </div>
                    </form>

                    <div class="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>Ponto</th>
                                <th>Latitude</th>
                                <th>Longitude</th>
                            </tr>
                            </thead>
                            <tbody id="pointsTableBody"></tbody>
                        </table>
                    </div>
                </section>
            </div>
        </section>
    </main>
</div>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js" crossorigin="anonymous"></script>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/setor-form.js"></script>
</body>
</html>
