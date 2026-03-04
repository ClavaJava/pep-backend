package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.Leito;
import br.com.hospital.pep.enums.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LeitoRepository extends JpaRepository<Leito, Long> {

    Optional<Leito> findBySetorAndNumero(Setor setor, Integer numero);

    // Ordenado por número — determinístico e auditável
    Optional<Leito> findFirstBySetorAndOcupadoFalseOrderByNumeroAsc(Setor setor);

    // Lista todos os leitos livres (útil na admissão sem setor fixo)
    List<Leito> findAllByOcupadoFalse();

    List<Leito> findAllBySetorAndOcupadoFalseOrderByNumeroAsc(Setor setor);
    // Dashboard: evita duas queries por setor
    @Query("SELECT l.ocupado, COUNT(l) FROM Leito l WHERE l.setor = :setor GROUP BY l.ocupado")
    List<Object[]> countBySetorGroupByOcupado(Setor setor);

    // Mantidos para compatibilidade com código existente
    long countBySetor(Setor setor);
    long countBySetorAndOcupadoTrue(Setor setor);
    long countBySetorAndOcupadoFalse(Setor setor);
}