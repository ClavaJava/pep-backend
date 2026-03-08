package br.com.hospital.pep.dto;

import br.com.hospital.pep.enums.Setor;
import java.time.LocalDateTime;

public class MovimentacaoResponseDTO {

    private Setor setorOrigem;
    private Setor setorDestino;
    private Integer leitoOrigem;
    private Integer leitoDestino;
    private LocalDateTime dataHora;

    public MovimentacaoResponseDTO(Setor setorOrigem, Setor setorDestino,
                                   Integer leitoOrigem, Integer leitoDestino,
                                   LocalDateTime dataHora) {
        this.setorOrigem = setorOrigem;
        this.setorDestino = setorDestino;
        this.leitoOrigem = leitoOrigem;
        this.leitoDestino = leitoDestino;
        this.dataHora = dataHora;
    }

    public Setor getSetorOrigem() { return setorOrigem; }
    public Setor getSetorDestino() { return setorDestino; }
    public Integer getLeitoOrigem() { return leitoOrigem; }
    public Integer getLeitoDestino() { return leitoDestino; }
    public LocalDateTime getDataHora() { return dataHora; }
}