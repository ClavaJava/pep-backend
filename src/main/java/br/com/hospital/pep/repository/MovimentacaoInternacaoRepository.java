package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.MovimentacaoInternacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimentacaoInternacaoRepository
        extends JpaRepository<MovimentacaoInternacao, Long> {

    List<MovimentacaoInternacao> findByInternacaoId(Long internacaoId);

}