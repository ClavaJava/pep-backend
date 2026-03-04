package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.MovimentacaoInternacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovimentacaoInternacaoRepository
        extends JpaRepository<MovimentacaoInternacao, Long> {

    // Nome corrigido — sem underscore desnecessário
    List<MovimentacaoInternacao> findByInternacaoIdOrderByDataHoraAsc(Long internacaoId);

    // Última movimentação — leito/status atual sem carregar o histórico completo
    Optional<MovimentacaoInternacao> findTopByInternacaoIdOrderByDataHoraDesc(Long internacaoId);

    // Paginado — para históricos longos
    Page<MovimentacaoInternacao> findByInternacaoId(Long internacaoId, Pageable pageable);
}