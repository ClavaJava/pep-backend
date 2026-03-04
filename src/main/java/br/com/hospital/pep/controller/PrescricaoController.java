package br.com.hospital.pep.controller;

import br.com.hospital.pep.dto.PrescricaoRequestDTO;
import br.com.hospital.pep.dto.PrescricaoResponseDTO;
import br.com.hospital.pep.service.PrescricaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescricoes")
public class PrescricaoController {

    private final PrescricaoService prescricaoService;

    public PrescricaoController(PrescricaoService prescricaoService) {
        this.prescricaoService = prescricaoService;
    }

    @PostMapping("/internacao/{internacaoId}")
    public PrescricaoResponseDTO criar(
            @PathVariable Long internacaoId,
            @RequestBody @Valid PrescricaoRequestDTO dto) {

        return prescricaoService.criar(internacaoId, dto);
    }

    @GetMapping("/internacao/{internacaoId}")
    public List<PrescricaoResponseDTO> listar(@PathVariable Long internacaoId) {
        return prescricaoService.listarPorInternacao(internacaoId);
    }
}