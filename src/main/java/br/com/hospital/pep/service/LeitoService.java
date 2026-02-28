package br.com.hospital.pep.service;

import br.com.hospital.pep.dto.OcupacaoDTO;
import br.com.hospital.pep.entity.Leito;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.repository.LeitoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class LeitoService {

    private final LeitoRepository leitoRepository;

    public LeitoService(LeitoRepository leitoRepository) {
        this.leitoRepository = leitoRepository;
    }

    // 🔥 AGORA ESTÁ NO LUGAR CERTO
    @PostConstruct
    public void inicializarLeitos() {

        if (leitoRepository.count() > 0) return;

        for (int i = 1; i <= 10; i++) {
            Leito leito = new Leito();
            leito.setSetor(Setor.UTI);
            leito.setNumero(i);
            leito.setOcupado(false);
            leitoRepository.save(leito);
        }

        for (int i = 1; i <= 20; i++) {
            Leito leito = new Leito();
            leito.setSetor(Setor.ENFERMARIA);
            leito.setNumero(i);
            leito.setOcupado(false);
            leitoRepository.save(leito);
        }
    }

    public Leito criarLeito(Setor setor, int numero) {

        int limite;

        if (setor == Setor.UTI) {
            limite = 10;
        } else if (setor == Setor.ENFERMARIA) {
            limite = 20;
        } else {
            limite = Integer.MAX_VALUE;
        }

        long total = leitoRepository.countBySetor(setor);

        if (total >= limite) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Limite máximo de leitos atingido para o setor " + setor
            );
        }

        Leito leito = new Leito();
        leito.setSetor(setor);
        leito.setNumero(numero);
        leito.setOcupado(false);

        return leitoRepository.save(leito);
    }

    public Map<Setor, OcupacaoDTO> dashboard() {

        Map<Setor, OcupacaoDTO> resultado = new HashMap<>();

        for (Setor setor : Setor.values()) {

            long total = leitoRepository.countBySetor(setor);
            long ocupados = leitoRepository.countBySetorAndOcupadoTrue(setor);

            resultado.put(setor, new OcupacaoDTO(total, ocupados));
        }

        return resultado;
    }
}