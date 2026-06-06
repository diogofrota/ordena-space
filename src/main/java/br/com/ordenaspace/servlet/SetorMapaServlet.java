package br.com.ordenaspace.servlet;

import br.com.ordenaspace.dao.SetorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Descricao da funcao:
 * Exibe o mapa consolidado com todos os setores cadastrados.
 * Parametros e retorno:
 * Processa GET HTTP e nao retorna valor alem da JSP renderizada.
 * Armazenamento e persistencia:
 * Le setores e seus pontos do banco para desenhar poligonos em Leaflet.
 * TODO para evolucao online/producao:
 * Incluir filtros por area, layers tematicos e atualizacao automatica.
 */
public class SetorMapaServlet extends BaseServlet {
    private final SetorDAO setorDAO = new SetorDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("pageTitle", "Mapa de Setores");
            request.setAttribute("activeMenu", "setores");
            request.setAttribute("setores", setorDAO.findAllWithPoints());
            view(request, response, "setor-mapa.jsp");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao carregar mapa de setores.", exception);
        }
    }
}
