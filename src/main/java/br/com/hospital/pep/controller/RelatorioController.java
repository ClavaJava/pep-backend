package br.com.hospital.pep.controller;

import br.com.hospital.pep.dto.RelatorioRequestDTO;
import br.com.hospital.pep.dto.RelatorioResponseDTO;
import br.com.hospital.pep.service.RelatorioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @PostMapping("/internacao/{internacaoId}")
    public RelatorioResponseDTO criar(
            @PathVariable Long internacaoId,
            @RequestBody RelatorioRequestDTO dto) {
        return relatorioService.criar(internacaoId, dto);
    }

    @GetMapping("/internacao/{internacaoId}")
    public List<RelatorioResponseDTO> listar(@PathVariable Long internacaoId) {
        return relatorioService.listarPorInternacao(internacaoId);
    }
}