package br.com.ordenaspace.servlet;

import br.com.ordenaspace.dao.PolicialDAO;
import br.com.ordenaspace.dao.ServicoAtivoDAO;
import br.com.ordenaspace.dao.SetorDAO;
import br.com.ordenaspace.dao.ViaturaDAO;
import br.com.ordenaspace.model.ServicoAtivo;
import br.com.ordenaspace.model.ServicoStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Descricao da funcao:
 * Coordena a ativacao e finalizacao manual de servicos operacionais.
 * Parametros e retorno:
 * Processa GET e POST HTTP para formularios de escala e tabela de servicos abertos.
 * Armazenamento e persistencia:
 * Consulta recursos disponiveis e grava servicos_ativos com status operacional.
 * TODO para evolucao online/producao:
 * Integrar validacao de escala oficial e politicas de turno em tempo real.
 */
public class AtivacaoServlet extends BaseServlet {
    private final ViaturaDAO viaturaDAO = new ViaturaDAO();
    private final SetorDAO setorDAO = new SetorDAO();
    private final PolicialDAO policialDAO = new PolicialDAO();
    private final ServicoAtivoDAO servicoAtivoDAO = new ServicoAtivoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Ativacao Operacional");
            request.setAttribute("activeMenu", "ativacao");
            request.setAttribute("viaturasDisponiveis", viaturaDAO.findAvailableForActivation());
            request.setAttribute("setores", setorDAO.findAllWithPoints());
            request.setAttribute("policiaisDisponiveis", policialDAO.findAvailableForActivation());
            request.setAttribute("servicos", servicoAtivoDAO.findOpenWithDetails());
            view(request, response, "ativacao.jsp");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao carregar tela de ativacao.", exception);
        }
    }

    /**
     * Descricao da funcao:
     * Ativa novo servico ou finaliza um servico aberto selecionado na tabela.
     * Parametros e retorno:
     * Recebe request/response HTTP e nao retorna valor.
     * Armazenamento e persistencia:
     * Insere ou atualiza registros em servicos_ativos.
     * TODO para evolucao online/producao:
     * Registrar justificativa de encerramento e enviar eventos para fila externa.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = trim(request.getParameter("action"));

        try {
            if ("finalizar".equalsIgnoreCase(action)) {
                Long serviceId = parseLong(request.getParameter("id"));
                if (serviceId == null) {
                    redirectWithFlash(request, response, "/ativacao", "danger", "Identificador de servico invalido.");
                    return;
                }

                servicoAtivoDAO.finalizeService(serviceId);
                redirectWithFlash(request, response, "/ativacao", "success", "Servico finalizado com sucesso.");
                return;
            }

            Long viaturaId = parseLong(request.getParameter("viaturaId"));
            Long setorId = parseLong(request.getParameter("setorId"));
            Long policial1Id = parseLong(request.getParameter("policial1Id"));
            Long policial2Id = parseLong(request.getParameter("policial2Id"));

            if (viaturaId == null || setorId == null || policial1Id == null) {
                redirectWithFlash(request, response, "/ativacao", "danger", "Selecione viatura, setor e policial 1.");
                return;
            }

            if (policial2Id != null && policial1Id.equals(policial2Id)) {
                redirectWithFlash(request, response, "/ativacao", "danger", "Policial 1 e Policial 2 nao podem ser o mesmo.");
                return;
            }

            ServicoAtivo servicoAtivo = new ServicoAtivo();
            servicoAtivo.setViaturaId(viaturaId);
            servicoAtivo.setSetorId(setorId);
            servicoAtivo.setPolicial1Id(policial1Id);
            servicoAtivo.setPolicial2Id(policial2Id);
            servicoAtivo.setDataHoraInicio(LocalDateTime.now());
            servicoAtivo.setDuracaoHoras(12);
            servicoAtivo.setStatus(ServicoStatus.AGUARDANDO_GPS);

            servicoAtivoDAO.create(servicoAtivo);
            redirectWithFlash(request, response, "/ativacao", "success", "Servico ativado com sucesso.");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao processar ativacao.", exception);
        }
    }
}
