package br.com.hospital.pep.service;

import br.com.hospital.pep.dto.OcupacaoDTO;
import br.com.hospital.pep.entity.Leito;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.repository.LeitoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

@Service
public class LeitoService {

    private final LeitoRepository leitoRepository;

    public LeitoService(LeitoRepository leitoRepository) {
        this.leitoRepository = leitoRepository;
    }

    // =========================
    // CRIAR LEITO
    // =========================
    public Leito criarLeito(Setor setor, int numero) {

        int limite = getLimiteSetor(setor);
        long total = leitoRepository.countBySetor(setor);

        if (total >= limite) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Limite máximo de leitos atingido para o setor " + setor
            );
        }

        leitoRepository.findBySetorAndNumero(setor, numero)
                .ifPresent(l -> { throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Já existe um leito com número " + numero + " no setor " + setor
                ); });

        Leito leito = new Leito();
        leito.setSetor(setor);
        leito.setNumero(numero);
        leito.setOcupado(false);

        return leitoRepository.save(leito);
    }
    public List<Leito> listarDisponiveisPorSetor(Setor setor) {
        return leitoRepository.findAllBySetorAndOcupadoFalseOrderByNumeroAsc(setor);
    }

    // =========================
    // DASHBOARD
    // =========================
    public Map<Setor, OcupacaoDTO> dashboard() {

        Map<Setor, OcupacaoDTO> resultado = new HashMap<>();

        for (Setor setor : Setor.values()) {

            long total = 0L;
            long ocupados = 0L;

            for (Object[] row : leitoRepository.countBySetorGroupByOcupado(setor)) {
                boolean ocupado = (Boolean) row[0];
                long count = (Long) row[1];
                total += count;
                if (ocupado) ocupados = count;
            }

            resultado.put(setor, new OcupacaoDTO(total, ocupados));
        }

        return resultado;
    }

    // =========================
    // HELPER — limite por setor
    // =========================
    private int getLimiteSetor(Setor setor) {
        return switch (setor) {
            case UTI -> 10;
            case ENFERMARIA -> 20;
            default -> Integer.MAX_VALUE;
        };
    }
}