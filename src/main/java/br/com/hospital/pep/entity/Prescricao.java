package br.com.hospital.pep.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Prescricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "internacao_id", nullable = false)
    private Internacao internacao;

    private String medicamento;
    private String dose;
    private String frequencia;
    private String observacoes;
    private LocalDateTime dataHora;

    public Prescricao() {}

    public Long getId() { return id; }

    public Internacao getInternacao() { return internacao; }
    public void setInternacao(Internacao internacao) { this.internacao = internacao; }

    public String getMedicamento() { return medicamento; }
    public void setMedicamento(String medicamento) { this.medicamento = medicamento; }

    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }

    public String getFrequencia() { return frequencia; }
    public void setFrequencia(String frequencia) { this.frequencia = frequencia; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}