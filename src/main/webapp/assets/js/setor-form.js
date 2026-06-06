document.addEventListener("DOMContentLoaded", () => {
    const mapContainer = document.getElementById("setorMap");
    if (!mapContainer || typeof L === "undefined") {
        return;
    }

    const hiddenInput = document.getElementById("pointsData");
    const tableBody = document.getElementById("pointsTableBody");
    const clearButton = document.getElementById("clearPointsButton");
    const saveButton = document.getElementById("saveSetorButton");

    const map = L.map("setorMap").setView([-22.9519, -43.2105], 11);
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
        attribution: "&copy; OpenStreetMap contributors"
    }).addTo(map);

    const polygonLayer = L.polygon([], {
        color: "#53c7ff",
        fillColor: "#1b9dd4",
        fillOpacity: 0.22,
        weight: 3
    }).addTo(map);

    const markers = [];
    const points = [];

    function syncState() {
        polygonLayer.setLatLngs(points.map((point) => [point.latitude, point.longitude]));
        hiddenInput.value = JSON.stringify(points);
        saveButton.disabled = points.length < 3 || points.length > 6;

        tableBody.innerHTML = "";
        for (let i = 0; i < 6; i += 1) {
            const row = document.createElement("tr");
            const point = points[i];
            row.innerHTML = point
                ? "<td>Ponto " + (i + 1) + "</td><td>" + point.latitude.toFixed(6) + "</td><td>" + point.longitude.toFixed(6) + "</td>"
                : "<td>Ponto " + (i + 1) + "</td><td>-</td><td>-</td>";
            tableBody.appendChild(row);
        }
    }

    map.on("click", (event) => {
        if (points.length >= 6) {
            window.alert("Cada setor permite no maximo 6 pontos.");
            return;
        }

        const point = {
            latitude: Number(event.latlng.lat.toFixed(6)),
            longitude: Number(event.latlng.lng.toFixed(6))
        };
        points.push(point);

        const marker = L.marker([point.latitude, point.longitude]).addTo(map);
        marker.bindTooltip("Ponto " + points.length, {permanent: true, direction: "top"});
        markers.push(marker);
        syncState();
    });

    clearButton.addEventListener("click", () => {
        points.splice(0, points.length);
        markers.forEach((marker) => marker.remove());
        markers.splice(0, markers.length);
        polygonLayer.setLatLngs([]);
        syncState();
    });

    syncState();
});
