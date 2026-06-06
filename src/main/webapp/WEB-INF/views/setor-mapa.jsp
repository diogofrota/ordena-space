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
                <span class="section-kicker">cartografia</span>
                <h3 class="section-title">Mapa consolidado dos setores</h3>
                <p>Todos os setores cadastrados sao desenhados como poligonos sobre a base OpenStreetMap.</p>
            </div>

            <section class="map-card">
                <div id="setoresMap" class="map-frame"></div>
            </section>
        </section>
    </main>
</div>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js" crossorigin="anonymous"></script>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
<script>
    const map = L.map("setoresMap").setView([-22.9519, -43.2105], 11);
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
        attribution: "&copy; OpenStreetMap contributors"
    }).addTo(map);

    const bounds = [];
    <c:forEach items="${setores}" var="setor">
        const polygon${setor.id} = L.polygon([
            <c:forEach items="${setor.pontos}" var="ponto">
            [${ponto.latitude}, ${ponto.longitude}],
            </c:forEach>
        ], {
            color: "#53c7ff",
            fillColor: "#2ab3f0",
            fillOpacity: 0.20,
            weight: 3
        }).addTo(map).bindPopup("${setor.nome}");
        bounds.push(...polygon${setor.id}.getLatLngs()[0]);
    </c:forEach>

    if (bounds.length > 0) {
        map.fitBounds(bounds, {padding: [24, 24]});
    }
</script>
</body>
</html>
