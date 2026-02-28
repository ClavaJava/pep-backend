package br.com.hospital.pep.dto;

import br.com.hospital.pep.enums.Setor;
import java.time.LocalDateTime;

public class MovimentacaoResponseDTO {

    private Setor setorOrigem;
    private Setor setorDestino;
    private LocalDateTime dataHora;

    public MovimentacaoResponseDTO(Setor setorOrigem,
                                   Setor setorDestino,
                                   LocalDateTime dataHora) {
        this.setorOrigem = setorOrigem;
        this.setorDestino = setorDestino;
        this.dataHora = dataHora;
    }

    public Setor getSetorOrigem() { return setorOrigem; }
    public Setor getSetorDestino() { return setorDestino; }
    public LocalDateTime getDataHora() { return dataHora; }
}