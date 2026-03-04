package br.com.hospital.pep.dto;

import jakarta.validation.constraints.NotBlank;

public class PrescricaoRequestDTO {

    @NotBlank
    private String medicamento;

    @NotBlank
    private String dose;

    private String frequencia;
    private String observacoes;

    public String getMedicamento() { return medicamento; }
    public void setMedicamento(String medicamento) { this.medicamento = medicamento; }

    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }

    public String getFrequencia() { return frequencia; }
    public void setFrequencia(String frequencia) { this.frequencia = frequencia; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}