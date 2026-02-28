package br.com.hospital.pep.dto;

import br.com.hospital.pep.enums.Setor;
import jakarta.validation.constraints.NotNull;

public class InternacaoRequestDTO {

    @NotNull
    private Setor setor;

    @NotNull
    private Integer numeroLeito;

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public Integer getNumeroLeito() {
        return numeroLeito;
    }

    public void setNumeroLeito(Integer numeroLeito) {
        this.numeroLeito = numeroLeito;
    }
}