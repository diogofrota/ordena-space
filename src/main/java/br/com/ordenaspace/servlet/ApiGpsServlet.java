package br.com.ordenaspace.servlet;

import br.com.ordenaspace.config.GeoUtils;
import br.com.ordenaspace.dao.ServicoAtivoDAO;
import br.com.ordenaspace.model.ServicoAtivo;
import br.com.ordenaspace.model.ServicoStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Descricao da funcao:
 * Recebe leituras GPS do tablet satelital e recalcula o status do servico ativo.
 * Parametros e retorno:
 * Processa POST JSON e retorna JSON com o status operacional atualizado.
 * Armazenamento e persistencia:
 * Localiza um servico_ativo pela viatura/tablet e atualiza latitude, longitude e status no banco.
 * TODO para evolucao online/producao:
 * Proteger com autenticacao de dispositivo, rate limiting e assinatura criptografica da carga.
 */
public class ApiGpsServlet extends HttpServlet {
    private static final Gson GSON = new Gson();
    private final ServicoAtivoDAO servicoAtivoDAO = new ServicoAtivoDAO();

    /**
     * Descricao da funcao:
     * Processa a leitura GPS recebida, valida o servico aberto e responde em JSON.
     * Parametros e retorno:
     * Recebe request/response HTTP e nao retorna valor alem do payload JSON escrito.
     * Armazenamento e persistencia:
     * Atualiza dados de localizacao e status em servicos_ativos.
     * TODO para evolucao online/producao:
     * Persistir historico bruto de telemetria e gerar alertas assicronos.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try {
            String contentType = request.getContentType();
            if (contentType == null || !contentType.toLowerCase().startsWith("application/json")) {
                writeJson(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, error("Content-Type deve ser application/json."));
                return;
            }

            JsonObject payload = GSON.fromJson(request.getReader(), JsonObject.class);
            if (payload == null
                || !payload.has("tabletSatelital")
                || !payload.has("latitude")
                || !payload.has("longitude")) {
                writeJson(response, HttpServletResponse.SC_BAD_REQUEST, error("Payload JSON incompleto."));
                return;
            }

            String tabletSatelital = payload.get("tabletSatelital").getAsString();
            double latitude = payload.get("latitude").getAsDouble();
            double longitude = payload.get("longitude").getAsDouble();

            if (tabletSatelital == null || tabletSatelital.isBlank()) {
                writeJson(response, HttpServletResponse.SC_BAD_REQUEST, error("tabletSatelital e obrigatorio."));
                return;
            }

            if (Double.isNaN(latitude) || Double.isInfinite(latitude) || latitude < -90.0d || latitude > 90.0d) {
                writeJson(response, HttpServletResponse.SC_BAD_REQUEST, error("Latitude invalida."));
                return;
            }

            if (Double.isNaN(longitude) || Double.isInfinite(longitude) || longitude < -180.0d || longitude > 180.0d) {
                writeJson(response, HttpServletResponse.SC_BAD_REQUEST, error("Longitude invalida."));
                return;
            }

            ServicoAtivo service = servicoAtivoDAO.findOpenByTabletSatelital(tabletSatelital);
            if (service == null) {
                writeJson(response, HttpServletResponse.SC_NOT_FOUND, error("Nenhum servico ativo encontrado para o tablet informado."));
                return;
            }

            if (service.getSetor() == null || service.getSetor().getPontos() == null || service.getSetor().getPontos().size() < 3) {
                writeJson(response, HttpServletResponse.SC_CONFLICT, error("O setor do servico nao possui poligono GPS valido."));
                return;
            }

            boolean insidePolygon = GeoUtils.isPointInsidePolygon(service.getSetor().getPontos(), latitude, longitude);
            ServicoStatus newStatus = insidePolygon ? ServicoStatus.NORMAL : ServicoStatus.ALERTA;
            servicoAtivoDAO.updateLocationAndStatus(service.getId(), latitude, longitude, newStatus);

            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("servicoId", service.getId());
            responseBody.put("tabletSatelital", tabletSatelital);
            responseBody.put("setor", service.getSetor().getNome());
            responseBody.put("status", newStatus.name());
            responseBody.put("latitude", latitude);
            responseBody.put("longitude", longitude);
            responseBody.put("insidePolygon", insidePolygon);
            writeJson(response, HttpServletResponse.SC_OK, responseBody);
        } catch (JsonParseException exception) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, error("JSON invalido."));
        } catch (RuntimeException exception) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, error("Payload JSON invalido."));
        } catch (SQLException exception) {
            writeJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, error("Falha ao processar atualizacao de GPS."));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use POST para enviar GPS.");
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("error", message);
        return responseBody;
    }

    private void writeJson(HttpServletResponse response, int status, Map<String, Object> body) throws IOException {
        response.setStatus(status);
        response.getWriter().write(GSON.toJson(body));
    }
}
