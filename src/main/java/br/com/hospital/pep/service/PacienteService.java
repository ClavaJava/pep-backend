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

        // findByCpf substitui existsByCpf — evita query dupla
        pacienteRepository.findByCpf(dto.getCpf())
                .ifPresent(p -> { throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "CPF já cadastrado"
                ); });

        Paciente paciente = new Paciente();
        paciente.setNome(dto.getNome());
        paciente.setCpf(dto.getCpf().replaceAll("[^0-9]", ""));
        paciente.setSexo(dto.getSexo());
        paciente.setDataNascimento(dto.getDataNascimento());

        return converterParaDTO(pacienteRepository.save(paciente));
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public PacienteResponseDTO buscarPorId(Long id) {

        return converterParaDTO(pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente não encontrado"
                )));
    }

    // =========================
    // BUSCAR POR CPF
    // =========================
    public PacienteResponseDTO buscarPorCpf(String cpf) {

        return converterParaDTO(pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente não encontrado"
                )));
    }

    // =========================
    // BUSCAR POR NOME
    // =========================
    public List<PacienteResponseDTO> buscarPorNome(String nome) {

        List<Paciente> resultado = pacienteRepository
                .findByNomeContainingIgnoreCase(nome);

        if (resultado.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nenhum paciente encontrado com esse nome"
            );
        }

        return resultado.stream().map(this::converterParaDTO).toList();
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
                        HttpStatus.NOT_FOUND, "Paciente não encontrado"
                ));

        // Garante que o novo CPF não pertence a outro paciente
        pacienteRepository.findByCpf(dto.getCpf())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> { throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "CPF já pertence a outro paciente"
                ); });

        paciente.setNome(dto.getNome());
        paciente.setCpf(dto.getCpf().replaceAll("[^0-9]", ""));
        paciente.setSexo(dto.getSexo());
        paciente.setDataNascimento(dto.getDataNascimento());

        return converterParaDTO(pacienteRepository.save(paciente));
    }

    // =========================
    // DELETAR
    // =========================
    public void deletar(Long id) {

        if (!pacienteRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Paciente não encontrado"
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