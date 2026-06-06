package br.com.ordenaspace.servlet;

import br.com.ordenaspace.dao.ServicoAtivoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Descricao da funcao:
 * Renderiza a tabela operacional de monitoramento das guarnicoes em campo.
 * Parametros e retorno:
 * Processa GET HTTP e nao retorna valor alem da resposta HTML.
 * Armazenamento e persistencia:
 * Consulta servicos_ativos abertos enriquecidos com viatura, setor e policiais.
 * TODO para evolucao online/producao:
 * Adicionar atualizacao periodica parcial e socket para monitoramento em tempo real.
 */
public class MonitoramentoServlet extends BaseServlet {
    private final ServicoAtivoDAO servicoAtivoDAO = new ServicoAtivoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Monitoramento");
            request.setAttribute("activeMenu", "monitoramento");
            request.setAttribute("servicos", servicoAtivoDAO.findOpenWithDetails());
            view(request, response, "monitoramento.jsp");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao carregar monitoramento.", exception);
        }
    }
}
