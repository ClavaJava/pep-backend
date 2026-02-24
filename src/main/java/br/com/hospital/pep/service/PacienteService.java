package br.com.hospital.pep.service;

import br.com.hospital.pep.dto.PacienteRequestDTO;
import br.com.hospital.pep.dto.PacienteResponseDTO;
import br.com.hospital.pep.entity.Paciente;
import br.com.hospital.pep.repository.PacienteRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    // =========================
    // CADASTRAR
    // =========================
    public PacienteResponseDTO cadastrar(PacienteRequestDTO dto) {

        Paciente paciente = new Paciente();
        paciente.setNome(dto.getNome());
        paciente.setCpf(dto.getCpf());
        paciente.setSexo(dto.getSexo());
        paciente.setDataNascimento(dto.getDataNascimento());

        Paciente salvo = pacienteRepository.save(paciente);

        return converterParaDTO(salvo);
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public PacienteResponseDTO buscarPorId(Long id) {

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente não encontrado"
                ));

        return converterParaDTO(paciente);
    }

    // =========================
    // LISTAR TODOS
    // =========================
    public List<PacienteResponseDTO> listarTodos() {

        return pacienteRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // =========================
    // ATUALIZAR
    // =========================
    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto) {

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente não encontrado"
                ));

        paciente.setNome(dto.getNome());
        paciente.setCpf(dto.getCpf());
        paciente.setSexo(dto.getSexo());
        paciente.setDataNascimento(dto.getDataNascimento());

        Paciente atualizado = pacienteRepository.save(paciente);

        return converterParaDTO(atualizado);
    }

    // =========================
    // DELETAR
    // =========================
    public void deletar(Long id) {

        if (!pacienteRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Paciente não encontrado"
            );
        }

        pacienteRepository.deleteById(id);
    }

    // =========================
    // CONVERSOR ENTITY -> DTO
    // =========================
    private PacienteResponseDTO converterParaDTO(Paciente paciente) {

        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getSexo(),
                paciente.getDataNascimento()
        );
    }
}