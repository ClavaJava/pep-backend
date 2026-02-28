package br.com.hospital.pep.dto;

public class OcupacaoDTO {

    private long total;
    private long ocupados;
    private double taxaOcupacao;

    public OcupacaoDTO(long total, long ocupados) {
        this.total = total;
        this.ocupados = ocupados;
        this.taxaOcupacao = total == 0 ? 0 : (ocupados * 100.0) / total;
    }

    public long getTotal() { return total; }
    public long getOcupados() { return ocupados; }
    public double getTaxaOcupacao() { return taxaOcupacao; }
}