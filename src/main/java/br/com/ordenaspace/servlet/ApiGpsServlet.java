package br.com.ordenaspace.servlet;

import br.com.ordenaspace.service.GpsProcessingService;
import br.com.ordenaspace.service.GpsUpdateResult;
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
import java.util.NoSuchElementException;

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
    private final GpsProcessingService gpsProcessingService = new GpsProcessingService();

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

            GpsUpdateResult result = gpsProcessingService.processReading(tabletSatelital, latitude, longitude);

            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("servicoId", result.getServicoId());
            responseBody.put("tabletSatelital", result.getTabletSatelital());
            responseBody.put("setor", result.getSetor());
            responseBody.put("status", result.getStatus().name());
            responseBody.put("latitude", result.getLatitude());
            responseBody.put("longitude", result.getLongitude());
            responseBody.put("insidePolygon", result.isInsidePolygon());
            writeJson(response, HttpServletResponse.SC_OK, responseBody);
        } catch (JsonParseException exception) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, error("JSON invalido."));
        } catch (IllegalArgumentException exception) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, error(exception.getMessage()));
        } catch (NoSuchElementException exception) {
            writeJson(response, HttpServletResponse.SC_NOT_FOUND, error(exception.getMessage()));
        } catch (IllegalStateException exception) {
            writeJson(response, HttpServletResponse.SC_CONFLICT, error(exception.getMessage()));
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
