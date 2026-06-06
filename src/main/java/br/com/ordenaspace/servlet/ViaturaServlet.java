package br.com.ordenaspace.servlet;

import br.com.ordenaspace.dao.ViaturaDAO;
import br.com.ordenaspace.model.Viatura;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Descricao da funcao:
 * Controla cadastro, listagem e exclusao de viaturas.
 * Parametros e retorno:
 * Processa GET e POST HTTP com base no parametro action.
 * Armazenamento e persistencia:
 * Encapsula operacoes sobre a tabela viaturas via DAO.
 * TODO para evolucao online/producao:
 * Adicionar edicao, filtros e upload de documentos de frota.
 */
public class ViaturaServlet extends BaseServlet {
    private final ViaturaDAO viaturaDAO = new ViaturaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = trim(request.getParameter("action"));
        if ("form".equalsIgnoreCase(action)) {
            request.setAttribute("pageTitle", "Cadastrar Viatura");
            request.setAttribute("activeMenu", "viaturas");
            view(request, response, "viatura-form.jsp");
            return;
        }

        try {
            request.setAttribute("pageTitle", "Viaturas");
            request.setAttribute("activeMenu", "viaturas");
            request.setAttribute("viaturas", viaturaDAO.findAll());
            view(request, response, "viatura-list.jsp");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao listar viaturas.", exception);
        }
    }

    /**
     * Descricao da funcao:
     * Trata submissao de cadastro e exclusao de viaturas.
     * Parametros e retorno:
     * Recebe request/response HTTP e nao retorna valor.
     * Armazenamento e persistencia:
     * Insere ou remove registros na tabela viaturas.
     * TODO para evolucao online/producao:
     * Acrescentar validacao de placa padronizada e workflow de edicao.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = trim(request.getParameter("action"));

        try {
            if ("delete".equalsIgnoreCase(action)) {
                Long viaturaId = parseLong(request.getParameter("id"));
                if (viaturaId == null) {
                    redirectWithFlash(request, response, "/viaturas", "danger", "Identificador de viatura invalido.");
                    return;
                }

                viaturaDAO.deleteById(viaturaId);
                redirectWithFlash(request, response, "/viaturas", "success", "Viatura excluida com sucesso.");
                return;
            }

            Viatura viatura = new Viatura();
            viatura.setNumero(trim(request.getParameter("numero")));
            viatura.setPlaca(trim(request.getParameter("placa")).toUpperCase());
            viatura.setTabletSatelital(trim(request.getParameter("tabletSatelital")));

            if (viatura.getNumero().isBlank() || viatura.getPlaca().isBlank() || viatura.getTabletSatelital().isBlank()) {
                request.setAttribute("error", "Preencha numero, placa e tablet satelital.");
                request.setAttribute("viatura", viatura);
                request.setAttribute("pageTitle", "Cadastrar Viatura");
                request.setAttribute("activeMenu", "viaturas");
                view(request, response, "viatura-form.jsp");
                return;
            }

            viaturaDAO.save(viatura);
            redirectWithFlash(request, response, "/viaturas", "success", "Viatura cadastrada com sucesso.");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao persistir viatura.", exception);
        }
    }
}
