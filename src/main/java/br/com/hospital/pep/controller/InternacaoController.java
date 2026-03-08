package br.com.hospital.pep.controller;

import br.com.hospital.pep.dto.InternacaoRequestDTO;
import br.com.hospital.pep.dto.InternacaoResponseDTO;
import br.com.hospital.pep.dto.MovimentacaoResponseDTO;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.service.InternacaoService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    // Exemplo de uso:
    // GET /internacoes/ativas             → página 0, 10 itens (default)
    // GET /internacoes/ativas?page=1&size=20
    // GET /internacoes/ativas?page=0&size=5&sort=dataEntrada,desc
    @GetMapping("/ativas")
    public Page<InternacaoResponseDTO> listarInternados(
            @PageableDefault(size = 10, sort = "dataEntrada") Pageable pageable) {

        return internacaoService.listarInternados(pageable);
    }

    @PutMapping("/{id}/transferir")
    public ResponseEntity<Void> transferir(
            @PathVariable Long id,
            @RequestParam Setor novoSetor,
            @RequestParam Integer numeroLeito) {

        internacaoService.transferir(id, novoSetor, numeroLeito);
        return ResponseEntity.noContent().build();
    }
    // Histórico de internações por paciente (usado na tela Internados e Relatórios)
    @GetMapping("/paciente/{pacienteId}")
    public List<InternacaoResponseDTO> historicoPorPaciente(@PathVariable Long pacienteId) {
        return internacaoService.historicoPorPaciente(pacienteId);
    }
    @GetMapping("/{id}/movimentacoes")
    public List<MovimentacaoResponseDTO> listarMovimentacoes(
            @PathVariable Long id) {

        return internacaoService.listarMovimentacoes(id);
    }

    @PutMapping("/{id}/alta")
    public InternacaoResponseDTO darAlta(@PathVariable Long id) {
        return internacaoService.darAlta(id);
    }
}