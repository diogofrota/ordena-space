package br.com.ordenaspace.model;

/**
 * Descricao da funcao:
 * Agrupa indicadores sinteticos exibidos no dashboard inicial.
 * Parametros e retorno:
 * Estrutura mutavel com totais numericos de cadastros e servicos ativos.
 * Armazenamento e persistencia:
 * Nao e persistida diretamente; consolida contagens consultadas no banco via DAO.
 * TODO para evolucao online/producao:
 * Incluir cache curto, series temporais e indicadores de SLA operacional.
 */
public class DashboardMetrics {
    private int totalViaturas;
    private int totalPoliciais;
    private int totalSetores;
    private int servicosAtivos;

    public int getTotalViaturas() {
        return totalViaturas;
    }

    public void setTotalViaturas(int totalViaturas) {
        this.totalViaturas = totalViaturas;
    }

    public int getTotalPoliciais() {
        return totalPoliciais;
    }

    public void setTotalPoliciais(int totalPoliciais) {
        this.totalPoliciais = totalPoliciais;
    }

    public int getTotalSetores() {
        return totalSetores;
    }

    public void setTotalSetores(int totalSetores) {
        this.totalSetores = totalSetores;
    }

    public int getServicosAtivos() {
        return servicosAtivos;
    }

    public void setServicosAtivos(int servicosAtivos) {
        this.servicosAtivos = servicosAtivos;
    }
}
