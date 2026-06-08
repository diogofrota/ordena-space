package br.com.ordenaspace.service;

import br.com.ordenaspace.config.GeoUtils;
import br.com.ordenaspace.dao.ServicoAtivoDAO;
import br.com.ordenaspace.model.ServicoAtivo;
import br.com.ordenaspace.model.ServicoStatus;

import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * Descricao da funcao:
 * Centraliza a validacao e aplicacao de leituras GPS recebidas de qualquer origem.
 * Parametros e retorno:
 * Recebe tablet, latitude e longitude; retorna um GpsUpdateResult com o novo estado do servico.
 * Armazenamento e persistencia:
 * Busca servico aberto, recalcula status espacial e grava latitude/longitude em servicos_ativos.
 * TODO para evolucao online/producao:
 * Persistir trilha historica e suportar deduplicacao por timestamp do dispositivo.
 */
public class GpsProcessingService {
    private final ServicoAtivoDAO servicoAtivoDAO;

    public GpsProcessingService() {
        this(new ServicoAtivoDAO());
    }

    public GpsProcessingService(ServicoAtivoDAO servicoAtivoDAO) {
        this.servicoAtivoDAO = servicoAtivoDAO;
    }

    public GpsUpdateResult processReading(String tabletSatelital, double latitude, double longitude) throws SQLException {
        validateInput(tabletSatelital, latitude, longitude);

        ServicoAtivo service = servicoAtivoDAO.findOpenByTabletSatelital(tabletSatelital.trim());
        if (service == null) {
            throw new NoSuchElementException("Nenhum servico ativo encontrado para o tablet informado.");
        }

        if (service.getSetor() == null || service.getSetor().getPontos() == null || service.getSetor().getPontos().size() < 3) {
            throw new IllegalStateException("O setor do servico nao possui poligono GPS valido.");
        }

        boolean insidePolygon = GeoUtils.isPointInsidePolygon(service.getSetor().getPontos(), latitude, longitude);
        ServicoStatus newStatus = insidePolygon ? ServicoStatus.NORMAL : ServicoStatus.ALERTA;
        servicoAtivoDAO.updateLocationAndStatus(service.getId(), latitude, longitude, newStatus);

        return new GpsUpdateResult(
            service.getId(),
            tabletSatelital.trim(),
            service.getSetor().getNome(),
            newStatus,
            latitude,
            longitude,
            insidePolygon
        );
    }

    private void validateInput(String tabletSatelital, double latitude, double longitude) {
        if (tabletSatelital == null || tabletSatelital.isBlank()) {
            throw new IllegalArgumentException("tabletSatelital e obrigatorio.");
        }

        if (Double.isNaN(latitude) || Double.isInfinite(latitude) || latitude < -90.0d || latitude > 90.0d) {
            throw new IllegalArgumentException("Latitude invalida.");
        }

        if (Double.isNaN(longitude) || Double.isInfinite(longitude) || longitude < -180.0d || longitude > 180.0d) {
            throw new IllegalArgumentException("Longitude invalida.");
        }
    }
}
