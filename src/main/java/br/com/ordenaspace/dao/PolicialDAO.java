package br.com.ordenaspace.dao;

import br.com.ordenaspace.config.DatabaseConnectionFactory;
import br.com.ordenaspace.model.Policial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Descricao da funcao:
 * Realiza operacoes de cadastro, consulta e disponibilidade de policiais.
 * Parametros e retorno:
 * Recebe entidades ou ids e retorna listas e totais quando aplicavel.
 * Armazenamento e persistencia:
 * Opera sobre a tabela policiais e seus relacionamentos em servicos_ativos.
 * TODO para evolucao online/producao:
 * Adicionar filtros por unidade, patente e turno com paginacao.
 */
public class PolicialDAO {

    /**
     * Descricao da funcao:
     * Persiste um novo policial no cadastro base.
     * Parametros e retorno:
     * Recebe Policial preenchido e nao retorna valor.
     * Armazenamento e persistencia:
     * Insere registro em policiais com rg, graduacao e nome de guerra.
     * TODO para evolucao online/producao:
     * Validar unicidade com diretorio corporativo e dados funcionais oficiais.
     */
    public void save(Policial policial) throws SQLException {
        String sql = """
            INSERT INTO policiais (rg, graduacao, nome_guerra)
            VALUES (?, ?, ?)
            """;

        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, policial.getRg());
            statement.setString(2, policial.getGraduacao());
            statement.setString(3, policial.getNomeGuerra());
            statement.executeUpdate();
        }
    }

    /**
     * Descricao da funcao:
     * Lista todos os policiais em ordem alfabetica operacional.
     * Parametros e retorno:
     * Nao recebe parametros e retorna List<Policial>.
     * Armazenamento e persistencia:
     * Le a tabela policiais sem alterar os dados armazenados.
     * TODO para evolucao online/producao:
     * Acrescentar pesquisa incremental para bases maiores.
     */
    public List<Policial> findAll() throws SQLException {
        String sql = """
            SELECT id, rg, graduacao, nome_guerra
            FROM policiais
            ORDER BY nome_guerra
            """;

        List<Policial> policiais = new ArrayList<>();
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                policiais.add(mapRow(resultSet));
            }
        }
        return policiais;
    }

    /**
     * Descricao da funcao:
     * Lista policiais sem servico ativo em andamento.
     * Parametros e retorno:
     * Nao recebe parametros e retorna List<Policial>.
     * Armazenamento e persistencia:
     * Cruza policiais com servicos_ativos para evitar alocacao simultanea.
     * TODO para evolucao online/producao:
     * Incorporar politicas de descanso, escala e indisponibilidade medica.
     */
    public List<Policial> findAvailableForActivation() throws SQLException {
        String sql = """
            SELECT p.id, p.rg, p.graduacao, p.nome_guerra
            FROM policiais p
            WHERE NOT EXISTS (
                SELECT 1
                FROM servicos_ativos sa
                WHERE sa.status <> 'FINALIZADO'
                  AND (sa.policial_1_id = p.id OR sa.policial_2_id = p.id)
            )
            ORDER BY p.nome_guerra
            """;

        List<Policial> policiais = new ArrayList<>();
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                policiais.add(mapRow(resultSet));
            }
        }
        return policiais;
    }

    /**
     * Descricao da funcao:
     * Exclui um policial pelo id informado.
     * Parametros e retorno:
     * Recebe Long id e nao retorna valor.
     * Armazenamento e persistencia:
     * Executa delete fisico na tabela policiais.
     * TODO para evolucao online/producao:
     * Trocar por inativacao logica quando o historico operacional precisar ser preservado integralmente.
     */
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM policiais WHERE id = ?";
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    /**
     * Descricao da funcao:
     * Conta o total de policiais cadastrados.
     * Parametros e retorno:
     * Nao recebe parametros e retorna inteiro com a quantidade.
     * Armazenamento e persistencia:
     * Le agregacao da tabela policiais.
     * TODO para evolucao online/producao:
     * Expor metrica em endpoint observavel para dashboards externos.
     */
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM policiais";
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private Policial mapRow(ResultSet resultSet) throws SQLException {
        Policial policial = new Policial();
        policial.setId(resultSet.getLong("id"));
        policial.setRg(resultSet.getString("rg"));
        policial.setGraduacao(resultSet.getString("graduacao"));
        policial.setNomeGuerra(resultSet.getString("nome_guerra"));
        return policial;
    }
}
