package br.com.ordenaspace.model;

/**
 * Descricao da funcao:
 * Define os estados operacionais de um servico ativo em campo.
 * Parametros e retorno:
 * Enum sem parametros; cada constante identifica um estado rastreavel no monitoramento.
 * Armazenamento e persistencia:
 * E persistido como texto em servicos_ativos.status.
 * TODO para evolucao online/producao:
 * Acrescentar estados de comunicacao, bateria critica e perda de sinal com historico temporal.
 */
public enum ServicoStatus {
    AGUARDANDO_GPS,
    NORMAL,
    ALERTA,
    FINALIZADO
}
