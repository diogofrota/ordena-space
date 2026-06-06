package br.com.ordenaspace.filter;

import br.com.ordenaspace.model.Usuario;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

/**
 * Descricao da funcao:
 * Protege rotas internas exigindo sessao autenticada para acesso ao painel.
 * Parametros e retorno:
 * Intercepta requests/responses HTTP e delega ao proximo filtro apenas quando autorizado.
 * Armazenamento e persistencia:
 * Consulta a sessao HTTP para localizar o usuario autenticado; nao persiste dados no banco.
 * TODO para evolucao online/producao:
 * Expandir para autorizacao por perfil, expiracao robusta e integracao SSO.
 */
public class AuthFilter extends HttpFilter implements Filter {
    private static final Set<String> PUBLIC_PATHS = Set.of(
        "/",
        "/login",
        "/logout"
    );

    /**
     * Descricao da funcao:
     * Decide se a rota atual pode seguir anonimamente ou precisa de autenticacao.
     * Parametros e retorno:
     * Recebe request, response e filter chain; nao retorna valor.
     * Armazenamento e persistencia:
     * Usa a sessao em memoria do container para verificar o usuario logado.
     * TODO para evolucao online/producao:
     * Adicionar trilha de tentativas bloqueadas e regras para APIs de dispositivos.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        Usuario currentUser = session == null ? null : (Usuario) session.getAttribute("currentUser");
        if (currentUser != null) {
            applyAuthenticatedSecurityHeaders(httpResponse);
            chain.doFilter(request, response);
            return;
        }

        httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.contains(path) || path.startsWith("/assets/") || "/api/gps".equals(path);
    }

    private void applyAuthenticatedSecurityHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Referrer-Policy", "same-origin");
    }
}
