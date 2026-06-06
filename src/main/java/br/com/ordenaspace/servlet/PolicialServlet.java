package br.com.ordenaspace.servlet;

import br.com.ordenaspace.dao.PolicialDAO;
import br.com.ordenaspace.model.Policial;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Descricao da funcao:
 * Controla cadastro, listagem e remocao de policiais.
 * Parametros e retorno:
 * Processa GET e POST HTTP dirigidos pelo parametro action.
 * Armazenamento e persistencia:
 * Utiliza a tabela policiais via DAO para manter o cadastro operacional.
 * TODO para evolucao online/producao:
 * Incluir edicao, importacao em lote e filtros por graduacao e unidade.
 */
public class PolicialServlet extends BaseServlet {
    private final PolicialDAO policialDAO = new PolicialDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = trim(request.getParameter("action"));
        if ("form".equalsIgnoreCase(action)) {
            request.setAttribute("pageTitle", "Cadastrar Policial");
            request.setAttribute("activeMenu", "policiais");
            view(request, response, "policial-form.jsp");
            return;
        }

        try {
            request.setAttribute("pageTitle", "Policiais");
            request.setAttribute("activeMenu", "policiais");
            request.setAttribute("policiais", policialDAO.findAll());
            view(request, response, "policial-list.jsp");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao listar policiais.", exception);
        }
    }

    /**
     * Descricao da funcao:
     * Persiste novo policial ou exclui um registro selecionado.
     * Parametros e retorno:
     * Recebe request/response HTTP e nao retorna valor.
     * Armazenamento e persistencia:
     * Escreve e remove dados na tabela policiais.
     * TODO para evolucao online/producao:
     * Adicionar validacoes contra duplicidade funcional e edicao assistida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = trim(request.getParameter("action"));

        try {
            if ("delete".equalsIgnoreCase(action)) {
                Long policialId = parseLong(request.getParameter("id"));
                if (policialId == null) {
                    redirectWithFlash(request, response, "/policiais", "danger", "Identificador de policial invalido.");
                    return;
                }

                policialDAO.deleteById(policialId);
                redirectWithFlash(request, response, "/policiais", "success", "Policial excluido com sucesso.");
                return;
            }

            Policial policial = new Policial();
            policial.setRg(trim(request.getParameter("rg")));
            policial.setGraduacao(trim(request.getParameter("graduacao")));
            policial.setNomeGuerra(trim(request.getParameter("nomeGuerra")));

            if (policial.getRg().isBlank() || policial.getGraduacao().isBlank() || policial.getNomeGuerra().isBlank()) {
                request.setAttribute("error", "Preencha RG, graduacao e nome de guerra.");
                request.setAttribute("policial", policial);
                request.setAttribute("pageTitle", "Cadastrar Policial");
                request.setAttribute("activeMenu", "policiais");
                view(request, response, "policial-form.jsp");
                return;
            }

            policialDAO.save(policial);
            redirectWithFlash(request, response, "/policiais", "success", "Policial cadastrado com sucesso.");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao persistir policial.", exception);
        }
    }
}
