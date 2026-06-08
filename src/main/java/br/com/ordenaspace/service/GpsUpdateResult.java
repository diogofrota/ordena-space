package br.com.ordenaspace.service;

import br.com.ordenaspace.model.ServicoStatus;

/**
 * Descricao da funcao:
 * Transporta o resultado consolidado de uma leitura GPS aplicada a um servico ativo.
 * Parametros e retorno:
 * Recebe dados no construtor e expoe getters imutaveis para resposta HTTP e logs internos.
 * Armazenamento e persistencia:
 * Nao persiste diretamente; representa o efeito ja gravado no banco pela camada de servico.
 * TODO para evolucao online/producao:
 * Acrescentar timestamp efetivo da telemetria e identificador da fonte externa.
 */
public class GpsUpdateResult {
    private final Long servicoId;
    private final String tabletSatelital;
    private final String setor;
    private final ServicoStatus status;
    private final double latitude;
    private final double longitude;
    private final boolean insidePolygon;

    public GpsUpdateResult(
        Long servicoId,
        String tabletSatelital,
        String setor,
        ServicoStatus status,
        double latitude,
        double longitude,
        boolean insidePolygon
    ) {
        this.servicoId = servicoId;
        this.tabletSatelital = tabletSatelital;
        this.setor = setor;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.insidePolygon = insidePolygon;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public String getTabletSatelital() {
        return tabletSatelital;
    }

    public String getSetor() {
        return setor;
    }

    public ServicoStatus getStatus() {
        return status;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isInsidePolygon() {
        return insidePolygon;
    }
}
