package br.com.hospital.pep.config;

import br.com.hospital.pep.entity.Leito;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.repository.LeitoRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class LeitoDataLoader implements ApplicationRunner {

    private final LeitoRepository leitoRepository;

    public LeitoDataLoader(LeitoRepository leitoRepository) {
        this.leitoRepository = leitoRepository;
    }

    @Override
    public void run(ApplicationArguments args) {

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
}