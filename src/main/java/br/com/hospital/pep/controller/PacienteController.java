package br.com.hospital.pep.controller;

import br.com.hospital.pep.entity.Paciente;
import br.com.hospital.pep.repository.PacienteRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository repository;

    public PacienteController(PacienteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Paciente cadastrar(@RequestBody Paciente paciente) {
        return repository.save(paciente);
    }

    @GetMapping
    public String teste() {
        return "API Pacientes funcionando 🚑";
    }

    @GetMapping("/{id}")
    public Paciente buscarPorId(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping("/todos")
    public List<Paciente> listarTodos() {
        return repository.findAll();
    }
    @PutMapping("/{id}")
    public Paciente atualizar(@PathVariable Long id, @RequestBody Paciente pacienteAtualizado) {
        return repository.findById(id)
                .map(paciente -> {
                    paciente.setNome(pacienteAtualizado.getNome());
                    paciente.setCpf(pacienteAtualizado.getCpf());
                    paciente.setSexo(pacienteAtualizado.getSexo());
                    paciente.setDataNascimento(pacienteAtualizado.getDataNascimento());
                    return repository.save(paciente);
                })
                .orElse(null);
    }
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
