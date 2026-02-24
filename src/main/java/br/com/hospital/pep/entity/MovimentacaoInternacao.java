package br.com.hospital.pep.entity;

import br.com.hospital.pep.enums.Setor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MovimentacaoInternacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Setor setor;

    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "internacao_id")
    private Internacao internacao;

    public MovimentacaoInternacao() {}

    // getters e setters
}