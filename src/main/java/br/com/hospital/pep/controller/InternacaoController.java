package br.com.hospital.pep.controller;

import br.com.hospital.pep.entity.Internacao;
import br.com.hospital.pep.entity.Paciente;
import br.com.hospital.pep.repository.InternacaoRepository;
import br.com.hospital.pep.repository.PacienteRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/internacoes")
public class InternacaoController {

    private final InternacaoRepository internacaoRepository;
    private final PacienteRepository pacienteRepository;

    public InternacaoController(
            InternacaoRepository internacaoRepository,
            PacienteRepository pacienteRepository
    ) {
        this.internacaoRepository = internacaoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @PostMapping
    public Internacao internar(@RequestBody Internacao internacao) {

        if (internacao.getPaciente() == null || internacao.getPaciente().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Paciente é obrigatório"
            );
        }

        Long pacienteId = internacao.getPaciente().getId();

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente não encontrado"
                ));

        var internacaoAtiva =
                internacaoRepository.findByPacienteIdAndDataAltaIsNull(pacienteId);

        if (internacaoAtiva.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Paciente já possui internação ativa"
            );
        }

        internacao.setPaciente(paciente);
        internacao.setStatus("INTERNADO");
        internacao.setDataEntrada(LocalDate.now());

        return internacaoRepository.save(internacao);
    }
}
