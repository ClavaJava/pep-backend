package br.com.hospital.pep.service;

import br.com.hospital.pep.dto.InternacaoRequestDTO;
import br.com.hospital.pep.dto.InternacaoResponseDTO;
import br.com.hospital.pep.entity.Internacao;
import br.com.hospital.pep.entity.Paciente;
import br.com.hospital.pep.repository.InternacaoRepository;
import br.com.hospital.pep.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import br.com.hospital.pep.enums.StatusInternacao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InternacaoService {

    private final InternacaoRepository internacaoRepository;
    private final PacienteRepository pacienteRepository;

    public InternacaoService(InternacaoRepository internacaoRepository,
                             PacienteRepository pacienteRepository) {
        this.internacaoRepository = internacaoRepository;
        this.pacienteRepository = pacienteRepository;
    }
    Leito leitoDisponivel = leitoRepository
            .findFirstBySetorAndOcupadoFalse(dto.getSetor())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não há leitos disponíveis neste setor"
            ));

leitoDisponivel.setOcupado(true);
leitoRepository.save(leitoDisponivel);

internacao.setLeito(leitoDisponivel);
    // =========================
    // INTERNAR PACIENTE
    // =========================
    public InternacaoResponseDTO internar(Long pacienteId,
                                          InternacaoRequestDTO dto) {

        if (pacienteId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Paciente é obrigatório"
            );
        }

        if (dto.getSetor() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Setor é obrigatório"
            );
        }

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente não encontrado"
                ));

        if (internacaoRepository
                .findByPacienteIdAndDataAltaIsNull(pacienteId)
                .isPresent()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Paciente já possui internação ativa"
            );
        }


        Internacao internacao = new Internacao();
        internacao.setPaciente(paciente);
        internacao.setSetor(dto.getSetor());
        internacao.setStatus(StatusInternacao.INTERNADO);
        internacao.setDataEntrada(LocalDate.now());

        Internacao salva = internacaoRepository.save(internacao);

        return converterParaDTO(salva);
    }

    // =========================
    // DAR ALTA
    // =========================
    public InternacaoResponseDTO darAlta(Long internacaoId) {

        Internacao internacao = internacaoRepository.findById(internacaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Internação não encontrada"
                ));

        if (internacao.getDataAlta() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Internação já possui alta"
            );
        }
        internacao.getLeito().setOcupado(false);
        leitoRepository.save(internacao.getLeito());

        internacao.setDataAlta(LocalDate.now());
        internacao.setStatus(StatusInternacao.ALTA);

        Internacao salva = internacaoRepository.save(internacao);

        return converterParaDTO(salva);

    }

    // =========================
    // LISTAR INTERNADOS
    // =========================
    public List<InternacaoResponseDTO> listarInternados() {

        return internacaoRepository
                .findByStatus(StatusInternacao.INTERNADO)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // CONVERSOR ENTITY -> DTO
    // =========================
    private InternacaoResponseDTO converterParaDTO(Internacao internacao) {

        return new InternacaoResponseDTO(
                internacao.getId(),
                internacao.getDataEntrada(),
                internacao.getDataAlta(),
                internacao.getSetor(),
                internacao.getStatus(),
                internacao.getPaciente().getId(),
                internacao.getPaciente().getNome()
        );
    }
}