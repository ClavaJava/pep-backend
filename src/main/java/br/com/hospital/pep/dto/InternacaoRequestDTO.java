package br.com.hospital.pep.dto;

import br.com.hospital.pep.enums.Setor;
import jakarta.validation.constraints.NotNull;

public class InternacaoRequestDTO {

    @NotNull(message = "Setor é obrigatório")
    private Setor setor;

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }
}