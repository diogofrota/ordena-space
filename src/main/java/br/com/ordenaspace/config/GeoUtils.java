package br.com.ordenaspace.config;

import br.com.ordenaspace.model.SetorPontoGps;

import java.util.List;

/**
 * Descricao da funcao:
 * Executa validacoes geoespaciais basicas usadas pelo monitoramento de viaturas por poligono.
 * Parametros e retorno:
 * Recebe listas de pontos GPS e coordenadas; retorna resultado booleano da avaliacao espacial.
 * Armazenamento e persistencia:
 * Nao persiste dados; opera sobre coordenadas carregadas do banco pelos DAOs.
 * TODO para evolucao online/producao:
 * Evoluir para biblioteca GIS dedicada com suporte a SRID, geofencing e precisao auditavel.
 */
public final class GeoUtils {
    private static final double EPSILON = 1.0e-9;

    private GeoUtils() {
    }

    /**
     * Descricao da funcao:
     * Verifica se um ponto esta dentro de um poligono usando ray casting.
     * Parametros e retorno:
     * Recebe lista ordenada de vertices, latitude e longitude; retorna true quando o ponto cai dentro do poligono.
     * Armazenamento e persistencia:
     * Consome pontos persistidos em setor_pontos_gps para definir o poligono operacional do setor.
     * TODO para evolucao online/producao:
     * Considerar margem de tolerancia para GPS impreciso e deteccao de borda com regras parametrizaveis.
     */
    public static boolean isPointInsidePolygon(List<SetorPontoGps> polygonPoints, double latitude, double longitude) {
        if (polygonPoints == null || polygonPoints.size() < 3) {
            return false;
        }

        boolean inside = false;
        int totalPoints = polygonPoints.size();

        for (int i = 0, j = totalPoints - 1; i < totalPoints; j = i++) {
            SetorPontoGps current = polygonPoints.get(i);
            SetorPontoGps previous = polygonPoints.get(j);

            double xi = current.getLongitude();
            double yi = current.getLatitude();
            double xj = previous.getLongitude();
            double yj = previous.getLatitude();

            if (isPointOnSegment(longitude, latitude, xi, yi, xj, yj)) {
                return true;
            }

            boolean intersects = ((yi > latitude) != (yj > latitude))
                && (longitude < ((xj - xi) * (latitude - yi) / (yj - yi)) + xi);

            if (intersects) {
                inside = !inside;
            }
        }

        return inside;
    }

    private static boolean isPointOnSegment(double px, double py, double ax, double ay, double bx, double by) {
        double crossProduct = (py - ay) * (bx - ax) - (px - ax) * (by - ay);
        if (Math.abs(crossProduct) > EPSILON) {
            return false;
        }

        double dotProduct = (px - ax) * (bx - ax) + (py - ay) * (by - ay);
        if (dotProduct < -EPSILON) {
            return false;
        }

        double squaredSegmentLength = ((bx - ax) * (bx - ax)) + ((by - ay) * (by - ay));
        return dotProduct <= squaredSegmentLength + EPSILON;
    }
}
