package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Substitui existsByCpf — Optional já cobre a verificação de existência
    Optional<Paciente> findByCpf(String cpf);

    // Busca por nome parcial (case-insensitive) para telas de pesquisa
    List<Paciente> findByNomeContainingIgnoreCase(String nome);
}