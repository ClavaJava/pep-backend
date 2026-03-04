package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.Internacao;
import br.com.hospital.pep.enums.StatusInternacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InternacaoRepository extends JpaRepository<Internacao, Long> {

    // Busca internação ativa por status (mais seguro que checar dataAlta nula)
    Optional<Internacao> findByPacienteIdAndStatus(Long pacienteId, StatusInternacao status);

    // Mantido por compatibilidade, mas prefira o método acima
    Optional<Internacao> findByPacienteIdAndDataAltaIsNull(Long pacienteId);

    // Paginado — lista de internações ativas pode crescer muito
    Page<Internacao> findByStatus(StatusInternacao status, Pageable pageable);

    // Histórico completo do paciente (prontuário)
    List<Internacao> findByPacienteId(Long pacienteId);

    // Verifica qual internação ocupa um leito
    Optional<Internacao> findByLeitoId(Long leitoId);
}