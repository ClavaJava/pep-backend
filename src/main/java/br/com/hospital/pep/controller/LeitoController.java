package br.com.hospital.pep.controller;

import br.com.hospital.pep.dto.OcupacaoDTO;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.service.LeitoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import br.com.hospital.pep.entity.Leito;


import java.util.Map;

@RestController
@RequestMapping("/leitos")
public class LeitoController {

    private final LeitoService leitoService;

    public LeitoController(LeitoService leitoService) {
        this.leitoService = leitoService;
    }
    @GetMapping("/disponiveis")
    public List<Leito> listarDisponiveis(@RequestParam Setor setor) {
        return leitoService.listarDisponiveisPorSetor(setor);
    }

    @GetMapping("/dashboard")
    public Map<Setor, OcupacaoDTO> dashboard() {
        return leitoService.dashboard();
    }
}