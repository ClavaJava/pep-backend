package br.com.hospital.pep.entity;

import br.com.hospital.pep.enums.Setor;
import jakarta.persistence.*;

@Entity
public class Leito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numero;

    @Enumerated(EnumType.STRING)
    private Setor setor;

    private boolean ocupado;

    public Leito() {}

    public Long getId() { return id; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public Setor getSetor() { return setor; }
    public void setSetor(Setor setor) { this.setor = setor; }

    public boolean isOcupado() { return ocupado; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }
}