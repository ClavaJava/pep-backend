package br.com.hospital.pep.dto;

import br.com.hospital.pep.enums.TipoRelatorio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RelatorioRequestDTO {

    @NotNull
    private TipoRelatorio tipo;

    @NotBlank
    private String descricao;

    public TipoRelatorio getTipo() { return tipo; }
    public void setTipo(TipoRelatorio tipo) { this.tipo = tipo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}