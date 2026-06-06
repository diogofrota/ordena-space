package br.com.ordenaspace.servlet;

import br.com.ordenaspace.dao.UsuarioDAO;
import br.com.ordenaspace.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Descricao da funcao:
 * Controla o fluxo de exibicao e submissao do login do sistema.
 * Parametros e retorno:
 * Processa GET e POST HTTP e nao retorna valores diretos alem da resposta web.
 * Armazenamento e persistencia:
 * Valida credenciais na tabela usuarios e armazena o usuario autenticado em sessao.
 * TODO para evolucao online/producao:
 * Incluir captcha adaptativo, MFA e politicas de expiracao renovavel de sessao.
 */
public class LoginServlet extends BaseServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            redirect(request, response, "/dashboard");
            return;
        }

        request.setAttribute("pageTitle", "Login");
        view(request, response, "login.jsp");
    }

    /**
     * Descricao da funcao:
     * Valida as credenciais enviadas pelo formulario e cria a sessao autenticada.
     * Parametros e retorno:
     * Recebe request/response HTTP e nao retorna valor.
     * Armazenamento e persistencia:
     * Consulta usuarios para autenticacao e grava o usuario logado na sessao do container.
     * TODO para evolucao online/producao:
     * Acrescentar trilha de auditoria e invalidacao global de sessoes comprometidas.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = trim(request.getParameter("email")).toLowerCase();
        String senha = trim(request.getParameter("senha"));

        if (email.isBlank() || senha.isBlank()) {
            request.setAttribute("error", "Informe email e senha para acessar o painel.");
            request.setAttribute("email", email);
            request.setAttribute("pageTitle", "Login");
            view(request, response, "login.jsp");
            return;
        }

        try {
            Optional<Usuario> authenticatedUser = usuarioDAO.authenticate(email, senha);
            if (authenticatedUser.isEmpty()) {
                request.setAttribute("error", "Credenciais invalidas.");
                request.setAttribute("email", email);
                request.setAttribute("pageTitle", "Login");
                view(request, response, "login.jsp");
                return;
            }

            HttpSession existingSession = request.getSession(false);
            if (existingSession != null) {
                existingSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(30 * 60);
            session.setAttribute("currentUser", authenticatedUser.get());
            redirectWithFlash(request, response, "/dashboard", "success", "Autenticacao realizada com sucesso.");
        } catch (SQLException exception) {
            throw new ServletException("Falha ao autenticar usuario.", exception);
        }
    }
}
