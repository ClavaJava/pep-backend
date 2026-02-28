package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.Leito;
import br.com.hospital.pep.enums.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeitoRepository extends JpaRepository<Leito, Long> {

    // Buscar leito específico por setor e número
    Optional<Leito> findBySetorAndNumero(Setor setor, Integer numero);

    // Buscar primeiro leito livre em um setor (usado na transferência)
    Optional<Leito> findFirstBySetorAndOcupadoFalse(Setor setor);

    // Dashboard
    long countBySetor(Setor setor);
    long countBySetorAndOcupadoTrue(Setor setor);


    long countBySetorAndOcupadoFalse(Setor setor);
}