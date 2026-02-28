package br.com.hospital.pep.config;

import br.com.hospital.pep.entity.Leito;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.repository.LeitoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LeitoDataLoader implements CommandLineRunner {

    private final LeitoRepository leitoRepository;

    public LeitoDataLoader(LeitoRepository leitoRepository) {
        this.leitoRepository = leitoRepository;
    }

    @Override
    public void run(String... args) {

        if (leitoRepository.count() > 0) return;

        // 10 UTIs
        for (int i = 1; i <= 10; i++) {
            Leito leito = new Leito();
            leito.setNumero(i);
            leito.setSetor(Setor.UTI);
            leito.setOcupado(false);
            leitoRepository.save(leito);
        }

        // 20 Enfermarias
        for (int i = 1; i <= 20; i++) {
            Leito leito = new Leito();
            leito.setNumero(i);
            leito.setSetor(Setor.ENFERMARIA);
            leito.setOcupado(false);
            leitoRepository.save(leito);
        }
    }
}