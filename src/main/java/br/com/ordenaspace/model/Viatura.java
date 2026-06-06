package br.com.ordenaspace.model;

/**
 * Descricao da funcao:
 * Representa a viatura operacional vinculada a um tablet satelital.
 * Parametros e retorno:
 * Entidade mutavel com identificadores administrativos e de telemetria.
 * Armazenamento e persistencia:
 * Mapeia a tabela viaturas, usada em cadastro e ativacao de servico.
 * TODO para evolucao online/producao:
 * Incluir modelo, unidade, status de manutencao e integracao com inventario.
 */
public class Viatura {
    private Long id;
    private String numero;
    private String placa;
    private String tabletSatelital;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTabletSatelital() {
        return tabletSatelital;
    }

    public void setTabletSatelital(String tabletSatelital) {
        this.tabletSatelital = tabletSatelital;
    }
}
