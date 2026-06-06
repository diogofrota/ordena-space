package br.com.ordenaspace.dao;

import br.com.ordenaspace.config.DatabaseConnectionFactory;
import br.com.ordenaspace.model.Policial;
import br.com.ordenaspace.model.ServicoAtivo;
import br.com.ordenaspace.model.ServicoStatus;
import br.com.ordenaspace.model.Setor;
import br.com.ordenaspace.model.Viatura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Descricao da funcao:
 * Realiza ativacao, monitoramento e atualizacao de servicos operacionais.
 * Parametros e retorno:
 * Recebe entidades, ids e dados de GPS; retorna listas, totais e registros encontrados.
 * Armazenamento e persistencia:
 * Opera sobre a tabela servicos_ativos e consulta tabelas relacionadas para enriquecer a exibicao.
 * TODO para evolucao online/producao:
 * Separar historico, trilha GPS e eventos em tabelas especializadas de maior volume.
 */
public class ServicoAtivoDAO {
    private final SetorDAO setorDAO = new SetorDAO();

    /**
     * Descricao da funcao:
     * Cria um novo servico em estado aguardando GPS.
     * Parametros e retorno:
     * Recebe ServicoAtivo preenchido e nao retorna valor.
     * Armazenamento e persistencia:
     * Insere registro em servicos_ativos com inicio, duracao e alocacoes correntes.
     * TODO para evolucao online/producao:
     * Adicionar validacao transacional contra conflito de escala e ativacao duplicada.
     */
    public void create(ServicoAtivo servicoAtivo) throws SQLException {
        String sql = """
            INSERT INTO servicos_ativos (
                viatura_id,
                setor_id,
                policial_1_id,
                policial_2_id,
                data_hora_inicio,
                duracao_horas,
                status,
                latitude_atual,
                longitude_atual
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, servicoAtivo.getViaturaId());
            statement.setLong(2, servicoAtivo.getSetorId());
            statement.setLong(3, servicoAtivo.getPolicial1Id());
            if (servicoAtivo.getPolicial2Id() == null) {
                statement.setNull(4, java.sql.Types.NUMERIC);
            } else {
                statement.setLong(4, servicoAtivo.getPolicial2Id());
            }
            statement.setTimestamp(5, Timestamp.valueOf(servicoAtivo.getDataHoraInicio()));
            statement.setInt(6, servicoAtivo.getDuracaoHoras());
            statement.setString(7, servicoAtivo.getStatus().name());
            if (servicoAtivo.getLatitudeAtual() == null) {
                statement.setNull(8, java.sql.Types.NUMERIC);
            } else {
                statement.setDouble(8, servicoAtivo.getLatitudeAtual());
            }
            if (servicoAtivo.getLongitudeAtual() == null) {
                statement.setNull(9, java.sql.Types.NUMERIC);
            } else {
                statement.setDouble(9, servicoAtivo.getLongitudeAtual());
            }
            statement.executeUpdate();
        }
    }

    /**
     * Descricao da funcao:
     * Lista os servicos nao finalizados com dados completos para telas operacionais.
     * Parametros e retorno:
     * Nao recebe parametros e retorna List<ServicoAtivo>.
     * Armazenamento e persistencia:
     * Le servicos_ativos com joins em viaturas, setores e policiais.
     * TODO para evolucao online/producao:
     * Acrescentar filtros por status, periodo e unidade.
     */
    public List<ServicoAtivo> findOpenWithDetails() throws SQLException {
        String sql = baseDetailsSql() + """
            WHERE sa.status <> 'FINALIZADO'
            ORDER BY sa.data_hora_inicio DESC
            """;
        return runDetailsQuery(sql, null);
    }

    /**
     * Descricao da funcao:
     * Busca um servico ativo pela identificacao do tablet satelital da viatura.
     * Parametros e retorno:
     * Recebe o codigo do tablet e retorna ServicoAtivo ou null.
     * Armazenamento e persistencia:
     * Le servicos_ativos e viaturas para localizar o contexto atual de monitoramento.
     * TODO para evolucao online/producao:
     * Exigir autenticacao do dispositivo e assinatura da carga enviada.
     */
    public ServicoAtivo findOpenByTabletSatelital(String tabletSatelital) throws SQLException {
        String sql = """
            SELECT *
            FROM (
            """ + baseDetailsSql() + """
                WHERE sa.status <> 'FINALIZADO'
                  AND v.tablet_satelital = ?
                ORDER BY sa.data_hora_inicio DESC
            )
            WHERE ROWNUM = 1
            """;

        List<ServicoAtivo> services = runDetailsQuery(sql, tabletSatelital);
        if (services.isEmpty()) {
            return null;
        }

        ServicoAtivo service = services.get(0);
        Setor setor = setorDAO.findByIdWithPoints(service.getSetorId());
        service.setSetor(setor);
        return service;
    }

    /**
     * Descricao da funcao:
     * Atualiza a localizacao e o status operacional resultante de uma leitura GPS.
     * Parametros e retorno:
     * Recebe id do servico, latitude, longitude e novo status; nao retorna valor.
     * Armazenamento e persistencia:
     * Atualiza latitude_atual, longitude_atual e status em servicos_ativos.
     * TODO para evolucao online/producao:
     * Persistir cada leitura em trilha separada e gerar eventos de alerta assicronos.
     */
    public void updateLocationAndStatus(Long serviceId, double latitude, double longitude, ServicoStatus status)
        throws SQLException {
        String sql = """
            UPDATE servicos_ativos
            SET latitude_atual = ?, longitude_atual = ?, status = ?
            WHERE id = ?
            """;

        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, latitude);
            statement.setDouble(2, longitude);
            statement.setString(3, status.name());
            statement.setLong(4, serviceId);
            statement.executeUpdate();
        }
    }

    /**
     * Descricao da funcao:
     * Finaliza manualmente um servico em andamento.
     * Parametros e retorno:
     * Recebe id do servico e nao retorna valor.
     * Armazenamento e persistencia:
     * Atualiza o campo status para FINALIZADO em servicos_ativos.
     * TODO para evolucao online/producao:
     * Registrar usuario responsavel, horario efetivo de encerramento e motivo.
     */
    public void finalizeService(Long serviceId) throws SQLException {
        String sql = "UPDATE servicos_ativos SET status = 'FINALIZADO' WHERE id = ?";
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, serviceId);
            statement.executeUpdate();
        }
    }

    /**
     * Descricao da funcao:
     * Conta quantos servicos seguem em aberto.
     * Parametros e retorno:
     * Nao recebe parametros e retorna inteiro total.
     * Armazenamento e persistencia:
     * Le agregacao da tabela servicos_ativos filtrando status finalizado.
     * TODO para evolucao online/producao:
     * Segmentar a metrica por area e prioridade operacional.
     */
    public int countOpenServices() throws SQLException {
        String sql = "SELECT COUNT(*) FROM servicos_ativos WHERE status <> 'FINALIZADO'";
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private String baseDetailsSql() {
        return """
            SELECT sa.id,
                   sa.viatura_id,
                   sa.setor_id,
                   sa.policial_1_id,
                   sa.policial_2_id,
                   sa.data_hora_inicio,
                   sa.duracao_horas,
                   sa.status,
                   sa.latitude_atual,
                   sa.longitude_atual,
                   v.numero AS viatura_numero,
                   v.placa AS viatura_placa,
                   v.tablet_satelital,
                   s.nome AS setor_nome,
                   p1.rg AS policial_1_rg,
                   p1.graduacao AS policial_1_graduacao,
                   p1.nome_guerra AS policial_1_nome,
                   p2.rg AS policial_2_rg,
                   p2.graduacao AS policial_2_graduacao,
                   p2.nome_guerra AS policial_2_nome
            FROM servicos_ativos sa
            INNER JOIN viaturas v ON v.id = sa.viatura_id
            INNER JOIN setores s ON s.id = sa.setor_id
            INNER JOIN policiais p1 ON p1.id = sa.policial_1_id
            LEFT JOIN policiais p2 ON p2.id = sa.policial_2_id
            """;
    }

    private List<ServicoAtivo> runDetailsQuery(String sql, String tabletSatelital) throws SQLException {
        List<ServicoAtivo> services = new ArrayList<>();

        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (tabletSatelital != null) {
                statement.setString(1, tabletSatelital);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    services.add(mapDetailedRow(resultSet));
                }
            }
        }

        return services;
    }

    private ServicoAtivo mapDetailedRow(ResultSet resultSet) throws SQLException {
        ServicoAtivo servicoAtivo = new ServicoAtivo();
        servicoAtivo.setId(resultSet.getLong("id"));
        servicoAtivo.setViaturaId(resultSet.getLong("viatura_id"));
        servicoAtivo.setSetorId(resultSet.getLong("setor_id"));
        servicoAtivo.setPolicial1Id(resultSet.getLong("policial_1_id"));

        long police2Id = resultSet.getLong("policial_2_id");
        servicoAtivo.setPolicial2Id(resultSet.wasNull() ? null : police2Id);

        Timestamp startedAt = resultSet.getTimestamp("data_hora_inicio");
        servicoAtivo.setDataHoraInicio(startedAt == null ? LocalDateTime.now() : startedAt.toLocalDateTime());
        servicoAtivo.setDuracaoHoras(resultSet.getInt("duracao_horas"));
        servicoAtivo.setStatus(ServicoStatus.valueOf(resultSet.getString("status")));

        double latitudeAtual = resultSet.getDouble("latitude_atual");
        servicoAtivo.setLatitudeAtual(resultSet.wasNull() ? null : latitudeAtual);

        double longitudeAtual = resultSet.getDouble("longitude_atual");
        servicoAtivo.setLongitudeAtual(resultSet.wasNull() ? null : longitudeAtual);

        Viatura viatura = new Viatura();
        viatura.setId(servicoAtivo.getViaturaId());
        viatura.setNumero(resultSet.getString("viatura_numero"));
        viatura.setPlaca(resultSet.getString("viatura_placa"));
        viatura.setTabletSatelital(resultSet.getString("tablet_satelital"));
        servicoAtivo.setViatura(viatura);

        Setor setor = new Setor();
        setor.setId(servicoAtivo.getSetorId());
        setor.setNome(resultSet.getString("setor_nome"));
        servicoAtivo.setSetor(setor);

        Policial policial1 = new Policial();
        policial1.setId(servicoAtivo.getPolicial1Id());
        policial1.setRg(resultSet.getString("policial_1_rg"));
        policial1.setGraduacao(resultSet.getString("policial_1_graduacao"));
        policial1.setNomeGuerra(resultSet.getString("policial_1_nome"));
        servicoAtivo.setPolicial1(policial1);

        if (servicoAtivo.getPolicial2Id() != null) {
            Policial policial2 = new Policial();
            policial2.setId(servicoAtivo.getPolicial2Id());
            policial2.setRg(resultSet.getString("policial_2_rg"));
            policial2.setGraduacao(resultSet.getString("policial_2_graduacao"));
            policial2.setNomeGuerra(resultSet.getString("policial_2_nome"));
            servicoAtivo.setPolicial2(policial2);
        }

        return servicoAtivo;
    }
}
