package br.com.ordenaspace.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Descricao da funcao:
 * Fornece utilitarios comuns para servlets MVC da aplicacao.
 * Parametros e retorno:
 * Disponibiliza metodos auxiliares para forward, redirect e mensagens temporarias.
 * Armazenamento e persistencia:
 * Nao persiste dados diretamente; move mensagens entre sessao e request para a camada JSP.
 * TODO para evolucao online/producao:
 * Extrair tratamento padrao de erros e telemetria HTTP centralizada.
 */
public abstract class BaseServlet extends HttpServlet {

    /**
     * Descricao da funcao:
     * Encaminha a requisicao para uma JSP interna e expoe mensagens temporarias.
     * Parametros e retorno:
     * Recebe request, response e o nome da view; nao retorna valor.
     * Armazenamento e persistencia:
     * Consome dados transitorios da sessao e os transfere para o request antes do render.
     * TODO para evolucao online/producao:
     * Integrar motor de layout mais robusto e correlacao de erros por request.
     */
    protected void view(HttpServletRequest request, HttpServletResponse response, String viewName)
        throws ServletException, IOException {
        exposeFlashMessage(request);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/" + viewName);
        dispatcher.forward(request, response);
    }

    /**
     * Descricao da funcao:
     * Registra mensagem temporaria e redireciona para rota relativa ao contexto.
     * Parametros e retorno:
     * Recebe request, response, rota, tipo e conteudo da mensagem; nao retorna valor.
     * Armazenamento e persistencia:
     * Usa a sessao HTTP para persistir a flash message entre requests.
     * TODO para evolucao online/producao:
     * Padronizar codigos de feedback e internacionalizacao das mensagens.
     */
    protected void redirectWithFlash(
        HttpServletRequest request,
        HttpServletResponse response,
        String path,
        String flashType,
        String flashMessage
    ) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("flashType", flashType);
        session.setAttribute("flashMessage", flashMessage);
        response.sendRedirect(request.getContextPath() + path);
    }

    /**
     * Descricao da funcao:
     * Redireciona para uma rota interna sem anexar mensagem de retorno.
     * Parametros e retorno:
     * Recebe request, response e rota relativa; nao retorna valor.
     * Armazenamento e persistencia:
     * Nao interage com persistencia, apenas instrui o cliente a abrir outra rota.
     * TODO para evolucao online/producao:
     * Adicionar utilitario com preservacao controlada de query params.
     */
    protected void redirect(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    protected Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    protected String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private void exposeFlashMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }

        Object flashType = session.getAttribute("flashType");
        Object flashMessage = session.getAttribute("flashMessage");
        if (flashMessage != null) {
            request.setAttribute("flashType", flashType);
            request.setAttribute("flashMessage", flashMessage);
            session.removeAttribute("flashType");
            session.removeAttribute("flashMessage");
        }
    }
}
