package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.Prescricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescricaoRepository extends JpaRepository<Prescricao, Long> {

    List<Prescricao> findByInternacaoIdOrderByDataHoraDesc(Long internacaoId);
}