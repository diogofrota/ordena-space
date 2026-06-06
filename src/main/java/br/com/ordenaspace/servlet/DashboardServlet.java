package br.com.ordenaspace.servlet;

import br.com.ordenaspace.dao.PolicialDAO;
import br.com.ordenaspace.dao.ServicoAtivoDAO;
import br.com.ordenaspace.dao.SetorDAO;
import br.com.ordenaspace.dao.ViaturaDAO;
import br.com.ordenaspace.model.DashboardMetrics;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Descricao da funcao:
 * Renderiza o dashboard principal com indicadores executivos do sistema.
 * Parametros e retorno:
 * Processa GET HTTP e nao retorna valor alem da view montada.
 * Armazenamento e persistencia:
 * Consulta agregacoes das tabelas de viaturas, policiais, setores e servicos.
 * TODO para evolucao online/producao:
 * Acrescentar indicadores em tempo real e cache curto para leitura intensiva.
 */
public class DashboardServlet extends BaseServlet {
    private final ViaturaDAO viaturaDAO = new ViaturaDAO();
    private final PolicialDAO policialDAO = new PolicialDAO();
    private final SetorDAO setorDAO = new SetorDAO();
    private final ServicoAtivoDAO servicoAtivoDAO = new ServicoAtivoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DashboardMetrics metrics = new DashboardMetrics();
            metrics.setTotalViaturas(viaturaDAO.countAll());
            metrics.setTotalPoliciais(policialDAO.countAll());
            metrics.setTotalSetores(setorDAO.countAll());
            metrics.setServicosAtivos(servicoAtivoDAO.countOpenServices());

            request.setAttribute("pageTitle", "Dashboard");
            request.setAttribute("activeMenu", "dashboard");
            request.setAttribute("metrics", metrics);
            view(request, response, "dashboard.jsp");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao carregar dashboard.", exception);
        }
    }
}
