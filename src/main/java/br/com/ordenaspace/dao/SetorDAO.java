package br.com.ordenaspace.dao;

import br.com.ordenaspace.config.DatabaseConnectionFactory;
import br.com.ordenaspace.model.Setor;
import br.com.ordenaspace.model.SetorPontoGps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Descricao da funcao:
 * Administra o cadastro de setores e seus vertices GPS.
 * Parametros e retorno:
 * Recebe entidades Setor completas ou ids e retorna listas, totais e registros carregados.
 * Armazenamento e persistencia:
 * Opera de forma transacional sobre setores e setor_pontos_gps.
 * TODO para evolucao online/producao:
 * Incluir edicao de poligonos, soft delete e historico versionado de perimetros.
 */
public class SetorDAO {

    /**
     * Descricao da funcao:
     * Persiste setor e pontos GPS em uma unica transacao atomica.
     * Parametros e retorno:
     * Recebe Setor com lista de pontos e nao retorna valor.
     * Armazenamento e persistencia:
     * Insere um registro em setores e ate seis registros relacionados em setor_pontos_gps.
     * TODO para evolucao online/producao:
     * Validar auto-intersecao do poligono e suportar edicao com controle de concorrencia.
     */
    public void save(Setor setor) throws SQLException {
        String setorSql = "INSERT INTO setores (nome) VALUES (?)";
        String pontoSql = """
            INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement setorStatement = connection.prepareStatement(setorSql, new String[] {"id"})) {
                    setorStatement.setString(1, setor.getNome());
                    setorStatement.executeUpdate();

                    try (ResultSet generatedKeys = setorStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            setor.setId(generatedKeys.getLong(1));
                        } else {
                            throw new SQLException("Nao foi possivel obter o id gerado para o setor.");
                        }
                    }
                }

                try (PreparedStatement pointStatement = connection.prepareStatement(pontoSql)) {
                    for (SetorPontoGps point : setor.getPontos()) {
                        pointStatement.setLong(1, setor.getId());
                        pointStatement.setInt(2, point.getOrdemPonto());
                        pointStatement.setDouble(3, point.getLatitude());
                        pointStatement.setDouble(4, point.getLongitude());
                        pointStatement.addBatch();
                    }
                    pointStatement.executeBatch();
                }

                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }

    /**
     * Descricao da funcao:
     * Lista todos os setores com seus pontos ordenados.
     * Parametros e retorno:
     * Nao recebe parametros e retorna List<Setor>.
     * Armazenamento e persistencia:
     * Le dados consolidados de setores e setor_pontos_gps.
     * TODO para evolucao online/producao:
     * Acrescentar filtros por area operacional e paginacao.
     */
    public List<Setor> findAllWithPoints() throws SQLException {
        String sql = """
            SELECT s.id AS setor_id,
                   s.nome AS setor_nome,
                   p.id AS ponto_id,
                   p.ordem_ponto,
                   p.latitude,
                   p.longitude
            FROM setores s
            LEFT JOIN setor_pontos_gps p ON p.setor_id = s.id
            ORDER BY s.nome, p.ordem_ponto
            """;

        Map<Long, Setor> sectorsById = new LinkedHashMap<>();
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long setorId = resultSet.getLong("setor_id");
                Setor setor = sectorsById.computeIfAbsent(setorId, currentId -> {
                    Setor current = new Setor();
                    current.setId(currentId);
                    try {
                        current.setNome(resultSet.getString("setor_nome"));
                    } catch (SQLException exception) {
                        throw new IllegalStateException(exception);
                    }
                    return current;
                });

                long pointId = resultSet.getLong("ponto_id");
                if (!resultSet.wasNull()) {
                    SetorPontoGps point = new SetorPontoGps();
                    point.setId(pointId);
                    point.setSetorId(setorId);
                    point.setOrdemPonto(resultSet.getInt("ordem_ponto"));
                    point.setLatitude(resultSet.getDouble("latitude"));
                    point.setLongitude(resultSet.getDouble("longitude"));
                    setor.getPontos().add(point);
                }
            }
        }
        return new ArrayList<>(sectorsById.values());
    }

    /**
     * Descricao da funcao:
     * Busca um setor especifico com seu poligono completo.
     * Parametros e retorno:
     * Recebe o id do setor e retorna Setor ou null.
     * Armazenamento e persistencia:
     * Le dados persistidos em setores e setor_pontos_gps.
     * TODO para evolucao online/producao:
     * Cachear perimetros frequentemente utilizados pelo monitoramento GPS.
     */
    public Setor findByIdWithPoints(Long setorId) throws SQLException {
        String sql = """
            SELECT s.id AS setor_id,
                   s.nome AS setor_nome,
                   p.id AS ponto_id,
                   p.ordem_ponto,
                   p.latitude,
                   p.longitude
            FROM setores s
            LEFT JOIN setor_pontos_gps p ON p.setor_id = s.id
            WHERE s.id = ?
            ORDER BY p.ordem_ponto
            """;

        Setor setor = null;
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, setorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    if (setor == null) {
                        setor = new Setor();
                        setor.setId(resultSet.getLong("setor_id"));
                        setor.setNome(resultSet.getString("setor_nome"));
                    }

                    long pointId = resultSet.getLong("ponto_id");
                    if (!resultSet.wasNull()) {
                        SetorPontoGps point = new SetorPontoGps();
                        point.setId(pointId);
                        point.setSetorId(setorId);
                        point.setOrdemPonto(resultSet.getInt("ordem_ponto"));
                        point.setLatitude(resultSet.getDouble("latitude"));
                        point.setLongitude(resultSet.getDouble("longitude"));
                        setor.getPontos().add(point);
                    }
                }
            }
        }
        return setor;
    }

    /**
     * Descricao da funcao:
     * Remove um setor e seus pontos pelo id informado.
     * Parametros e retorno:
     * Recebe Long id e nao retorna valor.
     * Armazenamento e persistencia:
     * Executa delete em setores; os pontos relacionados sao removidos por cascade.
     * TODO para evolucao online/producao:
     * Transformar em desativacao logica para manter historico juridico de area.
     */
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM setores WHERE id = ?";
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    /**
     * Descricao da funcao:
     * Conta o total de setores cadastrados.
     * Parametros e retorno:
     * Nao recebe parametros e retorna inteiro com a quantidade.
     * Armazenamento e persistencia:
     * Le agregacao da tabela setores.
     * TODO para evolucao online/producao:
     * Expor indicador de cobertura cartografica por batalhao.
     */
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM setores";
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
}
