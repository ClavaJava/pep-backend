package br.com.hospital.pep.repository;

import br.com.hospital.pep.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
