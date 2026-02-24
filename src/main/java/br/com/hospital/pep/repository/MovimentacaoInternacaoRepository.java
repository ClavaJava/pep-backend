package br.com.hospital.pep.repository;

public interface MovimentacaoInternacaoRepository
        extends JpaRepository<MovimentacaoInternacao, Long> {

    List<MovimentacaoInternacao> findByInternacaoId(Long internacaoId);
}