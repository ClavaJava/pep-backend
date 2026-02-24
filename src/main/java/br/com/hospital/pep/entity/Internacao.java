package br.com.hospital.pep.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.enums.StatusInternacao;

@Entity
public class Internacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataEntrada;
    private LocalDate dataAlta;

    @Enumerated(EnumType.STRING)
    private Setor setor;

    @Enumerated(EnumType.STRING)
    private StatusInternacao status;

    @ManyToOne
    @JoinColumn(name = "leito_id")
    private Leito leito;

    public Internacao() {}

    public Long getId() {
        return id;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataAlta() {
        return dataAlta;
    }

    public void setDataAlta(LocalDate dataAlta) {
        this.dataAlta = dataAlta;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public StatusInternacao getStatus() {
        return status;
    }

    public void setStatus(StatusInternacao status) {
        this.status = status;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}