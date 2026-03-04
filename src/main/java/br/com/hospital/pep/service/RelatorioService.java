package br.com.hospital.pep.service;

import br.com.hospital.pep.dto.RelatorioRequestDTO;
import br.com.hospital.pep.dto.RelatorioResponseDTO;
import br.com.hospital.pep.entity.Internacao;
import br.com.hospital.pep.entity.Relatorio;
import br.com.hospital.pep.enums.StatusInternacao;
import br.com.hospital.pep.repository.InternacaoRepository;
import br.com.hospital.pep.repository.RelatorioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;
    private final InternacaoRepository internacaoRepository;

    public RelatorioService(RelatorioRepository relatorioRepository,
                            InternacaoRepository internacaoRepository) {
        this.relatorioRepository = relatorioRepository;
        this.internacaoRepository = internacaoRepository;
    }

    // =========================
    // ADICIONAR RELATÓRIO
    // =========================
    public RelatorioResponseDTO criar(Long internacaoId, RelatorioRequestDTO dto) {

        Internacao internacao = internacaoRepository.findById(internacaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Internação não encontrada"
                ));

        if (internacao.getStatus() != StatusInternacao.INTERNADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Só é possível adicionar relatórios a internações ativas"
            );
        }

        Relatorio relatorio = new Relatorio();
        relatorio.setInternacao(internacao);
        relatorio.setTipo(dto.getTipo());
        relatorio.setDescricao(dto.getDescricao());
        relatorio.setDataHora(LocalDateTime.now());

        return converterParaDTO(relatorioRepository.save(relatorio));
    }

    // =========================
    // LISTAR POR INTERNAÇÃO
    // =========================
    public List<RelatorioResponseDTO> listarPorInternacao(Long internacaoId) {

        if (!internacaoRepository.existsById(internacaoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Internação não encontrada"
            );
        }

        return relatorioRepository
                .findByInternacaoIdOrderByDataHoraDesc(internacaoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // =========================
    // CONVERSOR
    // =========================
    private RelatorioResponseDTO converterParaDTO(Relatorio r) {
        return new RelatorioResponseDTO(
                r.getId(),
                r.getTipo(),
                r.getDescricao(),
                r.getDataHora()
        );
    }
}