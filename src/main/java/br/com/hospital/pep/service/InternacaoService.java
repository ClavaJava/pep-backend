package br.com.hospital.pep.service;

import br.com.hospital.pep.dto.InternacaoRequestDTO;
import br.com.hospital.pep.dto.InternacaoResponseDTO;
import br.com.hospital.pep.dto.MovimentacaoResponseDTO;
import br.com.hospital.pep.entity.*;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.enums.StatusInternacao;
import br.com.hospital.pep.repository.*;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InternacaoService {

    private final InternacaoRepository internacaoRepository;
    private final PacienteRepository pacienteRepository;
    private final LeitoRepository leitoRepository;
    private final MovimentacaoInternacaoRepository movimentacaoRepository;

    public InternacaoService(InternacaoRepository internacaoRepository,
                             PacienteRepository pacienteRepository,
                             LeitoRepository leitoRepository,
                             MovimentacaoInternacaoRepository movimentacaoRepository) {

        this.internacaoRepository = internacaoRepository;
        this.pacienteRepository = pacienteRepository;
        this.leitoRepository = leitoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    // =========================
    // INTERNAR PACIENTE EM LEITO ESPECÍFICO
    // =========================
    @Transactional
    public InternacaoResponseDTO internar(Long pacienteId,
                                          InternacaoRequestDTO dto) {

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

        if (dto.getNumeroLeito() == null || dto.getNumeroLeito() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Número do leito inválido"
            );
        }

        Leito leito = leitoRepository
                .findBySetorAndNumero(dto.getSetor(), dto.getNumeroLeito())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Leito não encontrado neste setor"
                ));

        if (leito.isOcupado()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Leito já está ocupado"
            );
        }

        leito.setOcupado(true);
        leitoRepository.save(leito);

        Internacao internacao = new Internacao();
        internacao.setPaciente(paciente);
        internacao.setLeito(leito);
        internacao.setStatus(StatusInternacao.INTERNADO);
        internacao.setDataEntrada(LocalDate.now());

        Internacao salva = internacaoRepository.save(internacao);

        return converterParaDTO(salva);
    }

    // =========================
    // TRANSFERIR (continua automático)
    // =========================
    @Transactional
    public void transferir(Long internacaoId, Setor novoSetor) {

        Internacao internacao = internacaoRepository.findById(internacaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Internação não encontrada"
                ));

        if (internacao.getStatus() != StatusInternacao.INTERNADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Só é possível transferir internações ativas"
            );
        }

        Leito leitoAtual = internacao.getLeito();
        Setor setorOrigem = leitoAtual.getSetor();

        if (setorOrigem == novoSetor) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Paciente já está neste setor"
            );
        }

        leitoAtual.setOcupado(false);
        leitoRepository.save(leitoAtual);

        Leito novoLeito = leitoRepository
                .findFirstBySetorAndOcupadoFalse(novoSetor)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Não há leitos disponíveis no setor destino"
                ));

        novoLeito.setOcupado(true);
        leitoRepository.save(novoLeito);

        internacao.setLeito(novoLeito);

        MovimentacaoInternacao movimentacao = new MovimentacaoInternacao();
        movimentacao.setInternacao(internacao);
        movimentacao.setSetorOrigem(setorOrigem);
        movimentacao.setSetorDestino(novoSetor);
        movimentacao.setDataHora(LocalDateTime.now());

        movimentacaoRepository.save(movimentacao);
    }
    private void validarCapacidadeSetor(Setor setor) {

        long totalLeitos = leitoRepository.countBySetor(setor);

        int limite;

        if (setor == Setor.UTI) {
            limite = 10;
        } else if (setor == Setor.ENFERMARIA) {
            limite = 20;
        } else {
            return; // outros setores sem limite fixo
        }

        if (totalLeitos >= limite) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Limite de leitos atingido para o setor " + setor
            );
        }
    }
    // =========================
    // DAR ALTA
    // =========================
    @Transactional
    public InternacaoResponseDTO darAlta(Long internacaoId) {

        Internacao internacao = internacaoRepository.findById(internacaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Internação não encontrada"
                ));

        if (internacao.getStatus() != StatusInternacao.INTERNADO) {
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
                .toList();
    }

    // =========================
    // LISTAR MOVIMENTAÇÕES
    // =========================
    public List<MovimentacaoResponseDTO> listarMovimentacoes(Long internacaoId) {

        Internacao internacao = internacaoRepository.findById(internacaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Internação não encontrada"
                ));

        return movimentacaoRepository
                .findByInternacao_IdOrderByDataHoraAsc(internacao.getId())
                .stream()
                .map(m -> new MovimentacaoResponseDTO(
                        m.getSetorOrigem(),
                        m.getSetorDestino(),
                        m.getDataHora()
                ))
                .toList();
    }

    // =========================
    // CONVERSOR
    // =========================
    private InternacaoResponseDTO converterParaDTO(Internacao internacao) {

        return new InternacaoResponseDTO(
                internacao.getId(),
                internacao.getDataEntrada(),
                internacao.getDataAlta(),
                internacao.getLeito().getSetor(),
                internacao.getStatus(),
                internacao.getPaciente().getId(),
                internacao.getPaciente().getNome()
        );
    }
}