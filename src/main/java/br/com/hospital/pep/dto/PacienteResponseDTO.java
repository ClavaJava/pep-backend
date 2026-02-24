package br.com.hospital.pep.dto;

import java.time.LocalDate;

public class PacienteResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String sexo;
    private LocalDate dataNascimento;

    public PacienteResponseDTO(Long id,
                               String nome,
                               String cpf,
                               String sexo,
                               LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSexo() {
        return sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
}