package br.com.ordenaspace.integration;

/**
 * Descricao da funcao:
 * Representa a ultima posicao GPS retornada pela API externa por tablet satelital.
 * Parametros e retorno:
 * Recebe os campos essenciais no construtor e expoe getters imutaveis.
 * Armazenamento e persistencia:
 * Nao persiste; trafega dados entre cliente HTTP e processamento GPS interno.
 * TODO para evolucao online/producao:
 * Acrescentar timestamp de captura, velocidade e rumo quando o provedor disponibilizar.
 */
public class GpsProviderReading {
    private final String tabletSatelital;
    private final double latitude;
    private final double longitude;

    public GpsProviderReading(String tabletSatelital, double latitude, double longitude) {
        this.tabletSatelital = tabletSatelital;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTabletSatelital() {
        return tabletSatelital;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
