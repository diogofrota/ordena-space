package br.com.ordenaspace.listener;

import br.com.ordenaspace.service.GpsPollingService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Descricao da funcao:
 * Inicializa e encerra o agendador de sincronizacao GPS junto com o ciclo do webapp.
 * Parametros e retorno:
 * Recebe eventos de contexto do servlet container e nao retorna valor.
 * Armazenamento e persistencia:
 * Armazena a instancia do agendador no ServletContext para reuso por servlets.
 * TODO para evolucao online/producao:
 * Integrar com health checks de infraestrutura e administracao remota do agendador.
 */
public class GpsPollingContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        GpsPollingService gpsPollingService = new GpsPollingService();
        sce.getServletContext().setAttribute(GpsPollingService.CONTEXT_ATTRIBUTE, gpsPollingService);
        gpsPollingService.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        GpsPollingService gpsPollingService = GpsPollingService.fromContext(sce.getServletContext());
        if (gpsPollingService != null) {
            gpsPollingService.stop();
        }
    }
}
