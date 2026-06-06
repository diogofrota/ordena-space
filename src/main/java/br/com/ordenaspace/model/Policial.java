package br.com.ordenaspace.model;

/**
 * Descricao da funcao:
 * Representa o agente policial apto a compor uma guarnicao em servico.
 * Parametros e retorno:
 * Entidade mutavel com dados funcionais basicos para selecao em escala.
 * Armazenamento e persistencia:
 * Mapeia a tabela policiais e participa de relacionamentos com servicos_ativos.
 * TODO para evolucao online/producao:
 * Adicionar matricula, lotacao, especialidade e disponibilidade operacional em tempo real.
 */
public class Policial {
    private Long id;
    private String rg;
    private String graduacao;
    private String nomeGuerra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getGraduacao() {
        return graduacao;
    }

    public void setGraduacao(String graduacao) {
        this.graduacao = graduacao;
    }

    public String getNomeGuerra() {
        return nomeGuerra;
    }

    public void setNomeGuerra(String nomeGuerra) {
        this.nomeGuerra = nomeGuerra;
    }

    public String getNomeCompletoOperacional() {
        return graduacao + " " + nomeGuerra;
    }
}
