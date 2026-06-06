package br.com.ordenaspace.dao;

import br.com.ordenaspace.config.DatabaseConnectionFactory;
import br.com.ordenaspace.config.PasswordUtil;
import br.com.ordenaspace.model.PerfilUsuario;
import br.com.ordenaspace.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Descricao da funcao:
 * Executa operacoes de autenticacao e leitura de usuarios do sistema.
 * Parametros e retorno:
 * Recebe credenciais e retorna Optional<Usuario> quando ha correspondencia valida.
 * Armazenamento e persistencia:
 * Consulta a tabela usuarios para validacao de email, hash de senha e perfil.
 * TODO para evolucao online/producao:
 * Registrar trilha de auditoria de login, lockout e refresh de sessoes.
 */
public class UsuarioDAO {

    /**
     * Descricao da funcao:
     * Valida email e senha informados no login contra o hash persistido.
     * Parametros e retorno:
     * Recebe email e senha em texto puro; retorna Optional<Usuario> autenticado.
     * Armazenamento e persistencia:
     * Le dados persistidos em usuarios sem alterar o estado do banco.
     * TODO para evolucao online/producao:
     * Migrar a comparacao para algoritmo adaptativo e adicionar tentativas limitadas por IP e conta.
     */
    public Optional<Usuario> authenticate(String email, String rawPassword) throws SQLException {
        String sql = """
            SELECT id, email, senha_hash, perfil
            FROM usuarios
            WHERE email = ? AND senha_hash = ?
            """;

        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, PasswordUtil.sha256(rawPassword));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    private Usuario mapRow(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getLong("id"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setSenhaHash(resultSet.getString("senha_hash"));
        usuario.setPerfil(PerfilUsuario.valueOf(resultSet.getString("perfil")));
        return usuario;
    }
}
