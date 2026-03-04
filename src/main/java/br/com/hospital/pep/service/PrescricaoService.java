package br.com.hospital.pep.service;

import br.com.hospital.pep.dto.PrescricaoRequestDTO;
import br.com.hospital.pep.dto.PrescricaoResponseDTO;
import br.com.hospital.pep.entity.Internacao;
import br.com.hospital.pep.entity.Prescricao;
import br.com.hospital.pep.enums.StatusInternacao;
import br.com.hospital.pep.repository.InternacaoRepository;
import br.com.hospital.pep.repository.PrescricaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrescricaoService {

    private final PrescricaoRepository prescricaoRepository;
    private final InternacaoRepository internacaoRepository;

    public PrescricaoService(PrescricaoRepository prescricaoRepository,
                             InternacaoRepository internacaoRepository) {
        this.prescricaoRepository = prescricaoRepository;
        this.internacaoRepository = internacaoRepository;
    }

    // =========================
    // ADICIONAR PRESCRIÇÃO
    // =========================
    public PrescricaoResponseDTO criar(Long internacaoId, PrescricaoRequestDTO dto) {

        Internacao internacao = internacaoRepository.findById(internacaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Internação não encontrada"
                ));

        if (internacao.getStatus() != StatusInternacao.INTERNADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Só é possível adicionar prescrições a internações ativas"
            );
        }

        Prescricao prescricao = new Prescricao();
        prescricao.setInternacao(internacao);
        prescricao.setMedicamento(dto.getMedicamento());
        prescricao.setDose(dto.getDose());
        prescricao.setFrequencia(dto.getFrequencia());
        prescricao.setObservacoes(dto.getObservacoes());
        prescricao.setDataHora(LocalDateTime.now());

        return converterParaDTO(prescricaoRepository.save(prescricao));
    }

    // =========================
    // LISTAR POR INTERNAÇÃO
    // =========================
    public List<PrescricaoResponseDTO> listarPorInternacao(Long internacaoId) {

        if (!internacaoRepository.existsById(internacaoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Internação não encontrada"
            );
        }

        return prescricaoRepository
                .findByInternacaoIdOrderByDataHoraDesc(internacaoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // =========================
    // CONVERSOR
    // =========================
    private PrescricaoResponseDTO converterParaDTO(Prescricao p) {
        return new PrescricaoResponseDTO(
                p.getId(),
                p.getMedicamento(),
                p.getDose(),
                p.getFrequencia(),
                p.getObservacoes(),
                p.getDataHora()
        );
    }
}