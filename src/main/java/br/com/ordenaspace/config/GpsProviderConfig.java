package br.com.ordenaspace.config;

/**
 * Descricao da funcao:
 * Reune as configuracoes de acesso ao provedor externo de GPS lidas do ambiente.
 * Parametros e retorno:
 * Expoe factory a partir de variaveis de ambiente e getters para uso pelo agendador.
 * Armazenamento e persistencia:
 * Nao persiste dados; apenas carrega configuracao processual do container.
 * TODO para evolucao online/producao:
 * Migrar para vault centralizado com rotacao de segredo e validacao de certificados.
 */
public class GpsProviderConfig {
    private static final boolean DEFAULT_ENABLED = true;
    private static final String DEFAULT_BASE_URL = "https://api-ordena-space-production.up.railway.app";
    private static final String DEFAULT_ENDPOINT_TEMPLATE = "/api/telemetria/tablets/{tabletSatelital}/ultima-posicao";
    private static final int DEFAULT_INTERVAL_SECONDS = 120;
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    private final boolean enabled;
    private final String baseUrl;
    private final String endpointTemplate;
    private final int intervalSeconds;
    private final int timeoutSeconds;
    private final String authToken;

    public GpsProviderConfig(
        boolean enabled,
        String baseUrl,
        String endpointTemplate,
        int intervalSeconds,
        int timeoutSeconds,
        String authToken
    ) {
        this.enabled = enabled;
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.endpointTemplate = normalizeEndpointTemplate(endpointTemplate);
        this.intervalSeconds = intervalSeconds;
        this.timeoutSeconds = timeoutSeconds;
        this.authToken = authToken == null || authToken.isBlank() ? null : authToken.trim();
    }

    public static GpsProviderConfig fromEnvironment() {
        boolean enabled = Boolean.parseBoolean(System.getenv().getOrDefault("GPS_PROVIDER_ENABLED", String.valueOf(DEFAULT_ENABLED)));
        String baseUrl = System.getenv().getOrDefault("GPS_PROVIDER_BASE_URL", DEFAULT_BASE_URL);
        String endpointTemplate = System.getenv().getOrDefault(
            "GPS_PROVIDER_ENDPOINT_TEMPLATE",
            DEFAULT_ENDPOINT_TEMPLATE
        );
        int intervalSeconds = parseInt(System.getenv("GPS_PROVIDER_INTERVAL_SECONDS"), DEFAULT_INTERVAL_SECONDS);
        int timeoutSeconds = parseInt(System.getenv("GPS_PROVIDER_TIMEOUT_SECONDS"), DEFAULT_TIMEOUT_SECONDS);
        String authToken = System.getenv("GPS_PROVIDER_AUTH_TOKEN");
        return new GpsProviderConfig(enabled, baseUrl, endpointTemplate, intervalSeconds, timeoutSeconds, authToken);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getEndpointTemplate() {
        return endpointTemplate;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public String getAuthToken() {
        return authToken;
    }

    private static String normalizeBaseUrl(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        if (trimmed.endsWith("/")) {
            return trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }

    private static String normalizeEndpointTemplate(String value) {
        if (value == null || value.isBlank()) {
            return DEFAULT_ENDPOINT_TEMPLATE;
        }

        String trimmed = value.trim();
        return trimmed.startsWith("/") ? trimmed : "/" + trimmed;
    }

    private static int parseInt(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }
}
