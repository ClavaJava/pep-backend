package br.com.hospital.pep.entity;

import br.com.hospital.pep.enums.TipoRelatorio;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "internacao_id", nullable = false)
    private Internacao internacao;

    @Enumerated(EnumType.STRING)
    private TipoRelatorio tipo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private LocalDateTime dataHora;

    public Relatorio() {}

    public Long getId() { return id; }

    public Internacao getInternacao() { return internacao; }
    public void setInternacao(Internacao internacao) { this.internacao = internacao; }

    public TipoRelatorio getTipo() { return tipo; }
    public void setTipo(TipoRelatorio tipo) { this.tipo = tipo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}