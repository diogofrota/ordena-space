package br.com.ordenaspace.integration;

import br.com.ordenaspace.config.GpsProviderConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

/**
 * Descricao da funcao:
 * Consulta a API externa responsavel por expor a ultima posicao GPS por tablet.
 * Parametros e retorno:
 * Recebe tablet satelital e retorna Optional<GpsProviderReading> quando houver leitura disponivel.
 * Armazenamento e persistencia:
 * Nao persiste dados; faz apenas leitura HTTP remota.
 * TODO para evolucao online/producao:
 * Implementar retries exponenciais, circuit breaker e observabilidade HTTP detalhada.
 */
public class GpsProviderClient {
    private static final Gson GSON = new Gson();

    private final GpsProviderConfig config;
    private final HttpClient httpClient;

    public GpsProviderClient(GpsProviderConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(config.getTimeoutSeconds()))
            .build();
    }

    public Optional<GpsProviderReading> fetchLatestPosition(String tabletSatelital) throws IOException, InterruptedException {
        if (!config.isEnabled()) {
            return Optional.empty();
        }

        if (config.getBaseUrl() == null || config.getBaseUrl().isBlank()) {
            throw new IOException("GPS_PROVIDER_BASE_URL nao configurada.");
        }

        String encodedTablet = URLEncoder.encode(tabletSatelital, StandardCharsets.UTF_8);
        String path = config.getEndpointTemplate().replace("{tabletSatelital}", encodedTablet);
        URI uri = URI.create(config.getBaseUrl() + path);

        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
            .header("Accept", "application/json")
            .GET();

        if (config.getAuthToken() != null) {
            builder.header("Authorization", "Bearer " + config.getAuthToken());
        }

        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 404) {
            return Optional.empty();
        }

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Provedor GPS respondeu com status " + response.statusCode() + ".");
        }

        try {
            JsonObject payload = GSON.fromJson(response.body(), JsonObject.class);
            if (payload == null || !payload.has("latitude") || !payload.has("longitude")) {
                throw new IOException("Payload da API GPS incompleto.");
            }

            String responseTablet = payload.has("tabletSatelital") && !payload.get("tabletSatelital").isJsonNull()
                ? payload.get("tabletSatelital").getAsString()
                : tabletSatelital;

            return Optional.of(new GpsProviderReading(
                responseTablet,
                payload.get("latitude").getAsDouble(),
                payload.get("longitude").getAsDouble()
            ));
        } catch (JsonParseException exception) {
            throw new IOException("Payload da API GPS invalido.", exception);
        } catch (RuntimeException exception) {
            throw new IOException("Payload da API GPS invalido.", exception);
        }
    }
}
