package br.com.hospital.pep.service;

import br.com.hospital.pep.entity.Internacao;
import br.com.hospital.pep.entity.Paciente;
import br.com.hospital.pep.repository.InternacaoRepository;
import br.com.hospital.pep.repository.PacienteRepository;
import org.springframework.stereotype.Service;

@Service
public class InternacaoService {

    private final InternacaoRepository internacaoRepository;
    private final PacienteRepository pacienteRepository;

    public InternacaoService(InternacaoRepository internacaoRepository,
                             PacienteRepository pacienteRepository) {
        this.internacaoRepository = internacaoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public Internacao internar(Long pacienteId, Internacao internacao) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        internacao.setPaciente(paciente);
        return internacaoRepository.save(internacao);
    }
}
