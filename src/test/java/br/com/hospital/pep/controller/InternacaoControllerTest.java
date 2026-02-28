package br.com.hospital.pep.controller;

import br.com.hospital.pep.entity.Internacao;
import br.com.hospital.pep.entity.Leito;
import br.com.hospital.pep.entity.Paciente;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.enums.StatusInternacao;
import br.com.hospital.pep.repository.InternacaoRepository;
import br.com.hospital.pep.repository.LeitoRepository;
import br.com.hospital.pep.repository.MovimentacaoInternacaoRepository;
import br.com.hospital.pep.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InternacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InternacaoRepository internacaoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private LeitoRepository leitoRepository;

    @Autowired
    private MovimentacaoInternacaoRepository movimentacaoRepository;

    @Test
    void deveTransferirPacienteViaEndpoint() throws Exception {

        // 1) Paciente (ajuste os campos conforme sua Entity Paciente)
        Paciente paciente = new Paciente();
        paciente.setNome("João Teste");
        paciente.setCpf("99999999901"); // precisa ser único e não nulo
        paciente.setDataNascimento(LocalDate.of(1995, 5, 10));
        paciente.setSexo("MASCULINO"); // se seu campo for enum, ajuste aqui
        paciente = pacienteRepository.save(paciente);

        // 2) Leito atual na UTI (ocupado)
        Leito leitoUTI = new Leito();
        leitoUTI.setNumero(1);
        leitoUTI.setSetor(Setor.UTI);
        leitoUTI.setOcupado(true);
        leitoUTI = leitoRepository.save(leitoUTI);

        // 3) Leito disponível na ENFERMARIA
        Leito leitoEnf = new Leito();
        leitoEnf.setNumero(1);
        leitoEnf.setSetor(Setor.ENFERMARIA);
        leitoEnf.setOcupado(false);
        leitoRepository.save(leitoEnf);

        // 4) Internação ativa apontando pro leito UTI
        Internacao internacao = new Internacao();
        internacao.setPaciente(paciente);
        internacao.setLeito(leitoUTI);
        internacao.setStatus(StatusInternacao.INTERNADO);
        internacao.setDataEntrada(LocalDate.now());
        internacao = internacaoRepository.save(internacao);

        // 5) Chamar endpoint de transferir
        mockMvc.perform(
                put("/internacoes/" + internacao.getId() + "/transferir")
                        .param("novoSetor", "ENFERMARIA")
        ).andExpect(status().isNoContent()); // 204

        // 6) Verifica que gerou histórico
        // (se quiser garantir mais, dá pra checar tamanho e valores)
        var historico = movimentacaoRepository.findByInternacaoId(internacao.getId());
        org.junit.jupiter.api.Assertions.assertFalse(historico.isEmpty());
    }
}