package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.Relatorio;
import br.com.hospital.pep.enums.TipoRelatorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

    List<Relatorio> findByInternacaoIdOrderByDataHoraDesc(Long internacaoId);

    List<Relatorio> findByInternacaoIdAndTipoOrderByDataHoraDesc(Long internacaoId, TipoRelatorio tipo);
}