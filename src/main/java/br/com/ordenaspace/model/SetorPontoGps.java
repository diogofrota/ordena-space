package br.com.ordenaspace.model;

import java.util.Locale;

/**
 * Descricao da funcao:
 * Representa um vertice ordenado do poligono operacional de um setor.
 * Parametros e retorno:
 * Entidade mutavel com ordem, latitude e longitude.
 * Armazenamento e persistencia:
 * Mapeia a tabela setor_pontos_gps, vinculada por chave estrangeira ao setor.
 * TODO para evolucao online/producao:
 * Suportar importacao GIS e validacao topologica automatica dos vertices.
 */
public class SetorPontoGps {
    private Long id;
    private Long setorId;
    private int ordemPonto;
    private double latitude;
    private double longitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSetorId() {
        return setorId;
    }

    public void setSetorId(Long setorId) {
        this.setorId = setorId;
    }

    public int getOrdemPonto() {
        return ordemPonto;
    }

    public void setOrdemPonto(int ordemPonto) {
        this.ordemPonto = ordemPonto;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCoordenadaFormatada() {
        return String.format(Locale.US, "%.5f, %.5f", latitude, longitude);
    }
}
