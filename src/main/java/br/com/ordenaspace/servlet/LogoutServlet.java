package br.com.ordenaspace.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Descricao da funcao:
 * Encerra a sessao atual do usuario autenticado.
 * Parametros e retorno:
 * Processa GET HTTP e nao retorna valor alem do redirecionamento.
 * Armazenamento e persistencia:
 * Remove o estado autenticado mantido na sessao do container.
 * TODO para evolucao online/producao:
 * Registrar logout em auditoria central e invalidar sessao distribuida.
 */
public class LogoutServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("flashType", "info");
        newSession.setAttribute("flashMessage", "Sessao encerrada.");
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
