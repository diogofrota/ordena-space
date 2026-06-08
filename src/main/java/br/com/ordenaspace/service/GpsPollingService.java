package br.com.ordenaspace.service;

import br.com.ordenaspace.config.GpsProviderConfig;
import br.com.ordenaspace.dao.ServicoAtivoDAO;
import br.com.ordenaspace.integration.GpsProviderClient;
import br.com.ordenaspace.integration.GpsProviderReading;
import br.com.ordenaspace.model.ServicoAtivo;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Descricao da funcao:
 * Agenda sincronizacoes periodicas com a API externa de GPS para servicos abertos.
 * Parametros e retorno:
 * Expoe start/stop para o ciclo do container e syncTabletNow para leitura imediata por demanda.
 * Armazenamento e persistencia:
 * Le servicos abertos, consulta o provedor externo e delega a gravacao ao GpsProcessingService.
 * TODO para evolucao online/producao:
 * Paralelizar por lotes controlados e registrar metricas por dispositivo consultado.
 */
public class GpsPollingService {
    public static final String CONTEXT_ATTRIBUTE = GpsPollingService.class.getName();

    private static final Logger LOGGER = Logger.getLogger(GpsPollingService.class.getName());

    private final GpsProviderConfig config;
    private final GpsProviderClient client;
    private final GpsProcessingService processingService;
    private final ServicoAtivoDAO servicoAtivoDAO;
    private ScheduledExecutorService executor;

    public GpsPollingService() {
        this(createDefaultConfig());
    }

    private GpsPollingService(GpsProviderConfig config) {
        this(
            config,
            new GpsProviderClient(config),
            new GpsProcessingService(),
            new ServicoAtivoDAO()
        );
    }

    private static GpsProviderConfig createDefaultConfig() {
        return GpsProviderConfig.fromEnvironment();
    }

    public GpsPollingService(
        GpsProviderConfig config,
        GpsProviderClient client,
        GpsProcessingService processingService,
        ServicoAtivoDAO servicoAtivoDAO
    ) {
        this.config = config;
        this.client = client;
        this.processingService = processingService;
        this.servicoAtivoDAO = servicoAtivoDAO;
    }

    public void start() {
        if (!config.isEnabled()) {
            LOGGER.info("Sincronizacao GPS externa desabilitada por GPS_PROVIDER_ENABLED=false.");
            return;
        }

        executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "ordena-space-gps-polling");
            thread.setDaemon(true);
            return thread;
        });

        executor.scheduleWithFixedDelay(this::syncOpenServicesSafely, 5L, config.getIntervalSeconds(), TimeUnit.SECONDS);
        LOGGER.info("Sincronizacao GPS externa iniciada com intervalo de " + config.getIntervalSeconds() + " segundos.");
    }

    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }

    public boolean isEnabled() {
        return config.isEnabled();
    }

    public boolean syncTabletNow(String tabletSatelital) {
        if (!config.isEnabled() || tabletSatelital == null || tabletSatelital.isBlank()) {
            return false;
        }

        try {
            Optional<GpsProviderReading> reading = client.fetchLatestPosition(tabletSatelital.trim());
            if (reading.isEmpty()) {
                LOGGER.fine("Sem leitura GPS disponivel para o tablet " + tabletSatelital + '.');
                return false;
            }

            GpsProviderReading gpsReading = reading.get();
            GpsUpdateResult result = processingService.processReading(
                gpsReading.getTabletSatelital(),
                gpsReading.getLatitude(),
                gpsReading.getLongitude()
            );
            LOGGER.info("GPS sincronizado para tablet " + result.getTabletSatelital() + " com status " + result.getStatus() + '.');
            return true;
        } catch (IOException | InterruptedException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            LOGGER.log(Level.WARNING, "Falha ao consultar API externa de GPS para o tablet " + tabletSatelital + '.', exception);
            return false;
        } catch (SQLException | RuntimeException exception) {
            LOGGER.log(Level.WARNING, "Falha ao aplicar leitura GPS do tablet " + tabletSatelital + '.', exception);
            return false;
        }
    }

    public static GpsPollingService fromContext(ServletContext servletContext) {
        Object attribute = servletContext.getAttribute(CONTEXT_ATTRIBUTE);
        return attribute instanceof GpsPollingService gpsPollingService ? gpsPollingService : null;
    }

    private void syncOpenServicesSafely() {
        try {
            syncOpenServices();
        } catch (SQLException exception) {
            LOGGER.log(Level.WARNING, "Falha ao listar servicos abertos para sincronizacao GPS.", exception);
        }
    }

    private void syncOpenServices() throws SQLException {
        List<ServicoAtivo> openServices = servicoAtivoDAO.findOpenWithDetails();
        Set<String> tablets = new LinkedHashSet<>();
        for (ServicoAtivo service : openServices) {
            if (service.getViatura() != null && service.getViatura().getTabletSatelital() != null) {
                tablets.add(service.getViatura().getTabletSatelital().trim());
            }
        }

        for (String tablet : tablets) {
            syncTabletNow(tablet);
        }
    }
}
