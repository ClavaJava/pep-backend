package br.com.hospital.pep.dto;

import java.time.LocalDateTime;

public class PrescricaoResponseDTO {

    private Long id;
    private String medicamento;
    private String dose;
    private String frequencia;
    private String observacoes;
    private LocalDateTime dataHora;

    public PrescricaoResponseDTO(Long id, String medicamento, String dose,
                                 String frequencia, String observacoes,
                                 LocalDateTime dataHora) {
        this.id = id;
        this.medicamento = medicamento;
        this.dose = dose;
        this.frequencia = frequencia;
        this.observacoes = observacoes;
        this.dataHora = dataHora;
    }

    public Long getId() { return id; }
    public String getMedicamento() { return medicamento; }
    public String getDose() { return dose; }
    public String getFrequencia() { return frequencia; }
    public String getObservacoes() { return observacoes; }
    public LocalDateTime getDataHora() { return dataHora; }
}