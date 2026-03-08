package br.com.hospital.pep.dto;

import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.enums.StatusInternacao;

import java.time.LocalDate;

public class InternacaoResponseDTO {

    private Long id;
    private LocalDate dataEntrada;
    private LocalDate dataAlta;
    private Setor setor;
    private Integer numeroLeito;
    private StatusInternacao status;
    private Long pacienteId;
    private String nomePaciente;

    public InternacaoResponseDTO(Long id,
                                 LocalDate dataEntrada,
                                 LocalDate dataAlta,
                                 Setor setor,
                                 Integer numeroLeito,
                                 StatusInternacao status,
                                 Long pacienteId,
                                 String nomePaciente) {
        this.id = id;
        this.dataEntrada = dataEntrada;
        this.dataAlta = dataAlta;
        this.setor = setor;
        this.numeroLeito = numeroLeito;
        this.status = status;
        this.pacienteId = pacienteId;
        this.nomePaciente = nomePaciente;
    }

    public Long getId() { return id; }
    public LocalDate getDataEntrada() { return dataEntrada; }
    public LocalDate getDataAlta() { return dataAlta; }
    public Setor getSetor() { return setor; }
    public Integer getNumeroLeito() { return numeroLeito; }
    public StatusInternacao getStatus() { return status; }
    public Long getPacienteId() { return pacienteId; }
    public String getNomePaciente() { return nomePaciente; }
}