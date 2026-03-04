package br.com.hospital.pep.dto;

import br.com.hospital.pep.enums.TipoRelatorio;
import java.time.LocalDateTime;

public class RelatorioResponseDTO {

    private Long id;
    private TipoRelatorio tipo;
    private String descricao;
    private LocalDateTime dataHora;

    public RelatorioResponseDTO(Long id, TipoRelatorio tipo,
                                String descricao, LocalDateTime dataHora) {
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.dataHora = dataHora;
    }

    public Long getId() { return id; }
    public TipoRelatorio getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getDataHora() { return dataHora; }
}