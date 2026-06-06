package br.com.ordenaspace.config;

import br.com.ordenaspace.model.SetorPontoGps;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeoUtilsTest {

    @Test
    void shouldReturnTrueWhenPointIsInsidePolygon() {
        assertTrue(GeoUtils.isPointInsidePolygon(square(), -22.9510, -43.1910));
    }

    @Test
    void shouldReturnFalseWhenPointIsOutsidePolygon() {
        assertFalse(GeoUtils.isPointInsidePolygon(square(), -22.9400, -43.1700));
    }

    @Test
    void shouldTreatBoundaryPointAsInsidePolygon() {
        assertTrue(GeoUtils.isPointInsidePolygon(square(), -22.9500, -43.1900));
    }

    private List<SetorPontoGps> square() {
        return List.of(
            point(1, -22.9520, -43.1920),
            point(2, -22.9520, -43.1900),
            point(3, -22.9500, -43.1900),
            point(4, -22.9500, -43.1920)
        );
    }

    private SetorPontoGps point(int order, double latitude, double longitude) {
        SetorPontoGps point = new SetorPontoGps();
        point.setOrdemPonto(order);
        point.setLatitude(latitude);
        point.setLongitude(longitude);
        return point;
    }
}
