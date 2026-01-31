package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.Internacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InternacaoRepository extends JpaRepository<Internacao, Long> {

    Optional<Internacao> findByPacienteIdAndDataAltaIsNull(Long pacienteId);

}
