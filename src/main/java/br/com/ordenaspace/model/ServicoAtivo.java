package br.com.ordenaspace.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Descricao da funcao:
 * Consolida uma ativacao operacional em andamento ou historico recente.
 * Parametros e retorno:
 * Entidade mutavel com referencias a viatura, setor, policiais e coordenadas atuais.
 * Armazenamento e persistencia:
 * Mapeia a tabela servicos_ativos, incluindo estado monitorado e dados de GPS.
 * TODO para evolucao online/producao:
 * Evoluir para historico de trilhas, checkpoints e eventos de telemetria por servico.
 */
public class ServicoAtivo {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Long id;
    private Long viaturaId;
    private Long setorId;
    private Long policial1Id;
    private Long policial2Id;
    private LocalDateTime dataHoraInicio;
    private int duracaoHoras;
    private ServicoStatus status;
    private Double latitudeAtual;
    private Double longitudeAtual;
    private Viatura viatura;
    private Setor setor;
    private Policial policial1;
    private Policial policial2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getViaturaId() {
        return viaturaId;
    }

    public void setViaturaId(Long viaturaId) {
        this.viaturaId = viaturaId;
    }

    public Long getSetorId() {
        return setorId;
    }

    public void setSetorId(Long setorId) {
        this.setorId = setorId;
    }

    public Long getPolicial1Id() {
        return policial1Id;
    }

    public void setPolicial1Id(Long policial1Id) {
        this.policial1Id = policial1Id;
    }

    public Long getPolicial2Id() {
        return policial2Id;
    }

    public void setPolicial2Id(Long policial2Id) {
        this.policial2Id = policial2Id;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public int getDuracaoHoras() {
        return duracaoHoras;
    }

    public void setDuracaoHoras(int duracaoHoras) {
        this.duracaoHoras = duracaoHoras;
    }

    public ServicoStatus getStatus() {
        return status;
    }

    public void setStatus(ServicoStatus status) {
        this.status = status;
    }

    public Double getLatitudeAtual() {
        return latitudeAtual;
    }

    public void setLatitudeAtual(Double latitudeAtual) {
        this.latitudeAtual = latitudeAtual;
    }

    public Double getLongitudeAtual() {
        return longitudeAtual;
    }

    public void setLongitudeAtual(Double longitudeAtual) {
        this.longitudeAtual = longitudeAtual;
    }

    public Viatura getViatura() {
        return viatura;
    }

    public void setViatura(Viatura viatura) {
        this.viatura = viatura;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public Policial getPolicial1() {
        return policial1;
    }

    public void setPolicial1(Policial policial1) {
        this.policial1 = policial1;
    }

    public Policial getPolicial2() {
        return policial2;
    }

    public void setPolicial2(Policial policial2) {
        this.policial2 = policial2;
    }

    public String getDataHoraInicioFormatada() {
        if (dataHoraInicio == null) {
            return "-";
        }
        return dataHoraInicio.format(DATE_TIME_FORMATTER);
    }

    public String getPolicial2Nome() {
        return policial2 == null ? "-" : policial2.getNomeCompletoOperacional();
    }

    public String getLatitudeAtualFormatada() {
        return formatCoordinate(latitudeAtual);
    }

    public String getLongitudeAtualFormatada() {
        return formatCoordinate(longitudeAtual);
    }

    private String formatCoordinate(Double value) {
        return value == null ? "-" : String.format(Locale.US, "%.5f", value);
    }
}
