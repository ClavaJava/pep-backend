package br.com.hospital.pep.controller;

import br.com.hospital.pep.dto.InternacaoRequestDTO;
import br.com.hospital.pep.dto.InternacaoResponseDTO;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.service.InternacaoService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internacoes")
public class InternacaoController {

    private final InternacaoService internacaoService;

    public InternacaoController(InternacaoService internacaoService) {
        this.internacaoService = internacaoService;
    }

    @PostMapping("/{pacienteId}")
    public InternacaoResponseDTO internar(
            @PathVariable Long pacienteId,
            @RequestBody @Valid InternacaoRequestDTO dto) {

        return internacaoService.internar(pacienteId, dto);
    }

    @GetMapping("/ativas")
    public List<InternacaoResponseDTO> listarInternados() {
        return internacaoService.listarInternados();
    }

    @PutMapping("/{id}/transferir")
    public ResponseEntity<Void> transferir(
            @PathVariable Long id,
            @RequestParam Setor novoSetor) {

        internacaoService.transferir(id, novoSetor);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/alta")
    public InternacaoResponseDTO darAlta(@PathVariable Long id) {
        return internacaoService.darAlta(id);
    }
}