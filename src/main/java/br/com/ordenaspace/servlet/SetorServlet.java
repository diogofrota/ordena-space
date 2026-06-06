package br.com.ordenaspace.servlet;

import br.com.ordenaspace.dao.SetorDAO;
import br.com.ordenaspace.model.Setor;
import br.com.ordenaspace.model.SetorPontoGps;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Descricao da funcao:
 * Controla cadastro geografico de setores e a listagem tabular de poligonos.
 * Parametros e retorno:
 * Processa GET e POST HTTP com serializacao JSON dos pontos do mapa.
 * Armazenamento e persistencia:
 * Persiste em setores e setor_pontos_gps via transacao no DAO.
 * TODO para evolucao online/producao:
 * Incluir edicao visual de poligono, importacao GeoJSON e validacao cartografica robusta.
 */
public class SetorServlet extends BaseServlet {
    private static final Gson GSON = new Gson();
    private static final Type POINT_LIST_TYPE = new TypeToken<List<PointPayload>>() { }.getType();
    private final SetorDAO setorDAO = new SetorDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = trim(request.getParameter("action"));
        if ("form".equalsIgnoreCase(action)) {
            request.setAttribute("pageTitle", "Cadastrar Setor");
            request.setAttribute("activeMenu", "setores");
            view(request, response, "setor-form.jsp");
            return;
        }

        try {
            request.setAttribute("pageTitle", "Setores");
            request.setAttribute("activeMenu", "setores");
            request.setAttribute("setores", setorDAO.findAllWithPoints());
            view(request, response, "setor-list.jsp");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao listar setores.", exception);
        }
    }

    /**
     * Descricao da funcao:
     * Persiste setor com ate seis pontos GPS ou remove um setor existente.
     * Parametros e retorno:
     * Recebe request/response HTTP e nao retorna valor.
     * Armazenamento e persistencia:
     * Insere ou exclui registros das tabelas setores e setor_pontos_gps.
     * TODO para evolucao online/producao:
     * Adicionar auditoria de alteracao de perimetro e workflow de aprovacao.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = trim(request.getParameter("action"));

        try {
            if ("delete".equalsIgnoreCase(action)) {
                Long setorId = parseLong(request.getParameter("id"));
                if (setorId == null) {
                    redirectWithFlash(request, response, "/setores", "danger", "Identificador de setor invalido.");
                    return;
                }

                setorDAO.deleteById(setorId);
                redirectWithFlash(request, response, "/setores", "success", "Setor excluido com sucesso.");
                return;
            }

            String nome = trim(request.getParameter("nome"));
            String pointsData = trim(request.getParameter("pointsData"));

            List<SetorPontoGps> points = parsePoints(pointsData);
            if (nome.isBlank() || points.size() < 3 || points.size() > 6) {
                request.setAttribute("error", "Informe o nome e desenhe um poligono entre 3 e 6 pontos.");
                request.setAttribute("pageTitle", "Cadastrar Setor");
                request.setAttribute("activeMenu", "setores");
                request.setAttribute("draftSetorNome", nome);
                view(request, response, "setor-form.jsp");
                return;
            }

            Setor setor = new Setor();
            setor.setNome(nome);
            setor.setPontos(points);
            setorDAO.save(setor);
            redirectWithFlash(request, response, "/setores", "success", "Setor cadastrado com sucesso.");
        } catch (JsonSyntaxException | IllegalArgumentException exception) {
            request.setAttribute("error", "Os pontos enviados pelo mapa sao invalidos.");
            request.setAttribute("pageTitle", "Cadastrar Setor");
            request.setAttribute("activeMenu", "setores");
            request.setAttribute("draftSetorNome", trim(request.getParameter("nome")));
            view(request, response, "setor-form.jsp");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao persistir setor.", exception);
        }
    }

    private List<SetorPontoGps> parsePoints(String pointsData) {
        List<PointPayload> payload = GSON.fromJson(pointsData, POINT_LIST_TYPE);
        List<SetorPontoGps> points = new ArrayList<>();
        if (payload == null) {
            return points;
        }

        int order = 1;
        for (PointPayload current : payload) {
            validateCoordinate(current.latitude(), "latitude");
            validateCoordinate(current.longitude(), "longitude");

            SetorPontoGps point = new SetorPontoGps();
            point.setOrdemPonto(order++);
            point.setLatitude(current.latitude());
            point.setLongitude(current.longitude());
            points.add(point);
        }
        return points;
    }

    private record PointPayload(double latitude, double longitude) {
    }

    private void validateCoordinate(double coordinate, String label) {
        if (Double.isNaN(coordinate) || Double.isInfinite(coordinate)) {
            throw new IllegalArgumentException("Coordenada invalida: " + label);
        }

        if ("latitude".equals(label) && (coordinate < -90.0d || coordinate > 90.0d)) {
            throw new IllegalArgumentException(String.format(Locale.US, "Latitude fora da faixa: %.6f", coordinate));
        }

        if ("longitude".equals(label) && (coordinate < -180.0d || coordinate > 180.0d)) {
            throw new IllegalArgumentException(String.format(Locale.US, "Longitude fora da faixa: %.6f", coordinate));
        }
    }
}
