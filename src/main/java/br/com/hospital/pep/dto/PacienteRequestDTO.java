package br.com.hospital.pep.dto;

import java.time.LocalDate;

public class PacienteRequestDTO {

    private String nome;
    private String cpf;
    private String sexo;
    private LocalDate dataNascimento;

    public PacienteRequestDTO() {}

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