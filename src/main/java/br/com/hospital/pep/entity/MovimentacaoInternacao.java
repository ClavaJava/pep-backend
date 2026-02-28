package br.com.hospital.pep.entity;

import br.com.hospital.pep.enums.Setor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MovimentacaoInternacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "internacao_id")
    private Internacao internacao;

    @Enumerated(EnumType.STRING)
    private Setor setorOrigem;

    @Enumerated(EnumType.STRING)
    private Setor setorDestino;

    private LocalDateTime dataHora;
}