package br.com.hospital.pep.controller;

import br.com.hospital.pep.dto.PacienteRequestDTO;
import br.com.hospital.pep.dto.PacienteResponseDTO;
import br.com.hospital.pep.service.PacienteService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public PacienteResponseDTO cadastrar(@RequestBody PacienteRequestDTO dto) {
        return pacienteService.cadastrar(dto);
    }

    @GetMapping("/{id}")
    public PacienteResponseDTO buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id);
    }

    @GetMapping
    public List<PacienteResponseDTO> listarTodos() {
        return pacienteService.listarTodos();
    }

    // Busca por CPF (usado na tela Internar)
    @GetMapping("/cpf/{cpf}")
    public PacienteResponseDTO buscarPorCpf(@PathVariable String cpf) {
        return pacienteService.buscarPorCpf(cpf);
    }

    // Busca por nome (usado na tela Pacientes)
    @GetMapping("/buscar")
    public List<PacienteResponseDTO> buscarPorNome(@RequestParam String nome) {
        return pacienteService.buscarPorNome(nome);
    }

    @PutMapping("/{id}")
    public PacienteResponseDTO atualizar(@PathVariable Long id,
                                         @RequestBody PacienteRequestDTO dto) {
        return pacienteService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
    }
}