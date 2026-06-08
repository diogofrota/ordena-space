package br.com.ordenaspace.dao;

import br.com.ordenaspace.config.DatabaseConnectionFactory;
import br.com.ordenaspace.model.Viatura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Descricao da funcao:
 * Centraliza CRUD e consultas operacionais relacionadas a viaturas.
 * Parametros e retorno:
 * Recebe entidades Viatura ou identificadores e retorna listas, totais ou efetivacao de escrita.
 * Armazenamento e persistencia:
 * Opera sobre a tabela viaturas e participa de consultas com servicos_ativos.
 * TODO para evolucao online/producao:
 * Adicionar busca paginada, filtros dinamicos e validacao de integridade com frota externa.
 */
public class ViaturaDAO {

    /**
     * Descricao da funcao:
     * Persiste uma nova viatura no banco.
     * Parametros e retorno:
     * Recebe Viatura preenchida e nao retorna valor.
     * Armazenamento e persistencia:
     * Insere um registro em viaturas com numero, placa e tablet satelital.
     * TODO para evolucao online/producao:
     * Incluir update, versionamento otimista e rastreabilidade de alteracoes cadastrais.
     */
    public void save(Viatura viatura) throws SQLException {
        String sql = """
            INSERT INTO viaturas (numero, placa, tablet_satelital)
            VALUES (?, ?, ?)
            """;

        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, viatura.getNumero());
            statement.setString(2, viatura.getPlaca());
            statement.setString(3, viatura.getTabletSatelital());
            statement.executeUpdate();
        }
    }

    /**
     * Descricao da funcao:
     * Lista todas as viaturas cadastradas em ordem operacional.
     * Parametros e retorno:
     * Nao recebe parametros e retorna List<Viatura>.
     * Armazenamento e persistencia:
     * Le a tabela viaturas sem modificar o armazenamento relacional.
     * TODO para evolucao online/producao:
     * Acrescentar paginacao server-side para grandes volumes de frota.
     */
    public List<Viatura> findAll() throws SQLException {
        String sql = """
            SELECT id, numero, placa, tablet_satelital
            FROM viaturas
            ORDER BY numero
            """;

        List<Viatura> viaturas = new ArrayList<>();
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                viaturas.add(mapRow(resultSet));
            }
        }
        return viaturas;
    }

    /**
     * Descricao da funcao:
     * Lista viaturas livres para nova ativacao, excluindo servicos ainda abertos.
     * Parametros e retorno:
     * Nao recebe parametros e retorna List<Viatura>.
     * Armazenamento e persistencia:
     * Consulta viaturas cruzando com servicos_ativos para impedir dupla alocacao.
     * TODO para evolucao online/producao:
     * Bloquear a disponibilidade tambem por regras de manutencao e janela de turno.
     */
    public List<Viatura> findAvailableForActivation() throws SQLException {
        String sql = """
            SELECT v.id, v.numero, v.placa, v.tablet_satelital
            FROM viaturas v
            WHERE NOT EXISTS (
                SELECT 1
                FROM servicos_ativos sa
                WHERE sa.viatura_id = v.id
                  AND sa.status <> 'FINALIZADO'
            )
            ORDER BY v.numero
            """;

        List<Viatura> viaturas = new ArrayList<>();
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                viaturas.add(mapRow(resultSet));
            }
        }
        return viaturas;
    }

    /**
     * Descricao da funcao:
     * Busca uma viatura especifica pelo identificador primario.
     * Parametros e retorno:
     * Recebe Long id e retorna Viatura ou null.
     * Armazenamento e persistencia:
     * Le um unico registro da tabela viaturas para compor fluxos operacionais.
     * TODO para evolucao online/producao:
     * Acrescentar cache leve e metadados de rastreamento por dispositivo.
     */
    public Viatura findById(Long id) throws SQLException {
        String sql = """
            SELECT id, numero, placa, tablet_satelital
            FROM viaturas
            WHERE id = ?
            """;

        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }

        return null;
    }

    /**
     * Descricao da funcao:
     * Remove uma viatura pelo identificador primario.
     * Parametros e retorno:
     * Recebe o id da viatura e nao retorna valor.
     * Armazenamento e persistencia:
     * Executa delete fisico na tabela viaturas.
     * TODO para evolucao online/producao:
     * Substituir por exclusao logica quando houver integracao com historico operacional.
     */
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM viaturas WHERE id = ?";
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    /**
     * Descricao da funcao:
     * Conta o total de viaturas para exibicao em dashboard.
     * Parametros e retorno:
     * Nao recebe parametros e retorna total inteiro.
     * Armazenamento e persistencia:
     * Le agregacao sobre a tabela viaturas.
     * TODO para evolucao online/producao:
     * Cachear metricas de leitura frequente com invalidacao por evento.
     */
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM viaturas";
        return count(sql);
    }

    private int count(String sql) throws SQLException {
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private Viatura mapRow(ResultSet resultSet) throws SQLException {
        Viatura viatura = new Viatura();
        viatura.setId(resultSet.getLong("id"));
        viatura.setNumero(resultSet.getString("numero"));
        viatura.setPlaca(resultSet.getString("placa"));
        viatura.setTabletSatelital(resultSet.getString("tablet_satelital"));
        return viatura;
    }
}
