package br.com.ordenaspace.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Descricao da funcao:
 * Representa o setor geografico patrulhado por viaturas em campo.
 * Parametros e retorno:
 * Entidade mutavel com nome e lista de vertices GPS do poligono.
 * Armazenamento e persistencia:
 * Mapeia a tabela setores e agrega registros filhos de setor_pontos_gps.
 * TODO para evolucao online/producao:
 * Acrescentar metadata cartografica, vigencia e versionamento de poligonos.
 */
public class Setor {
    private Long id;
    private String nome;
    private List<SetorPontoGps> pontos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<SetorPontoGps> getPontos() {
        return pontos;
    }

    public void setPontos(List<SetorPontoGps> pontos) {
        this.pontos = pontos;
    }

    public String getPonto1Resumo() {
        return getResumoByOrder(1);
    }

    public String getPonto2Resumo() {
        return getResumoByOrder(2);
    }

    public String getPonto3Resumo() {
        return getResumoByOrder(3);
    }

    public String getPonto4Resumo() {
        return getResumoByOrder(4);
    }

    public String getPonto5Resumo() {
        return getResumoByOrder(5);
    }

    public String getPonto6Resumo() {
        return getResumoByOrder(6);
    }

    private String getResumoByOrder(int order) {
        for (SetorPontoGps point : pontos) {
            if (point.getOrdemPonto() == order) {
                return point.getCoordenadaFormatada();
            }
        }
        return "-";
    }
}
