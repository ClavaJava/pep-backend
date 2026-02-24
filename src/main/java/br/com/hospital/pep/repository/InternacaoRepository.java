package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.Internacao;
import br.com.hospital.pep.enums.StatusInternacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InternacaoRepository extends JpaRepository<Internacao, Long> {

    Optional<Internacao> findByPacienteIdAndDataAltaIsNull(Long pacienteId);

    List<Internacao> findByStatus(StatusInternacao status);
}