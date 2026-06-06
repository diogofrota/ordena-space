package br.com.ordenaspace.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Descricao da funcao:
 * Centraliza a abertura de conexoes JDBC com Oracle a partir de variaveis de ambiente.
 * Parametros e retorno:
 * Nao recebe parametros e retorna uma conexao JDBC pronta para uso.
 * Armazenamento e persistencia:
 * Nao persiste dados diretamente; apenas viabiliza o acesso transacional aos dados em Oracle Database.
 * TODO para evolucao online/producao:
 * Migrar para pool de conexoes com HikariCP ou datasource JNDI no container de aplicacao.
 */
public final class DatabaseConnectionFactory {

    private DatabaseConnectionFactory() {
    }

    /**
     * Descricao da funcao:
     * Le as configuracoes de ambiente e abre uma conexao com o banco Oracle.
     * Parametros e retorno:
     * Nao recebe parametros e retorna java.sql.Connection.
     * Armazenamento e persistencia:
     * A conexao retornada sera utilizada pelos DAOs para leitura e escrita em tabelas relacionais.
     * TODO para evolucao online/producao:
     * Adicionar observabilidade, retries controlados e suporte a rotacao segura de credenciais.
     */
    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (isBlank(url) || isBlank(user) || isBlank(password)) {
            throw new SQLException(
                "As variaveis de ambiente DB_URL, DB_USER e DB_PASSWORD precisam estar configuradas."
            );
        }

        return DriverManager.getConnection(url, user, password);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
