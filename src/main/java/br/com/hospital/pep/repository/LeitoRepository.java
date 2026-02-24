package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.Leito;
import br.com.hospital.pep.enums.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeitoRepository extends JpaRepository<Leito, Long> {

    Optional<Leito> findFirstBySetorAndOcupadoFalse(Setor setor);

    long countBySetorAndOcupadoFalse(Setor setor);
}