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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private Paciente criarPaciente(String cpf) {
        Paciente p = new Paciente();
        p.setNome("Paciente Teste");
        p.setCpf(cpf);
        p.setDataNascimento(LocalDate.of(1990, 1, 1));
        p.setSexo("MASCULINO");
        return pacienteRepository.save(p);
    }

    // 🔥 MÉTODO CORRIGIDO (EVITA DUPLICAÇÃO DE LEITO)
    private Leito criarLeito(Setor setor, int numero, boolean ocupado) {

        return leitoRepository
                .findBySetorAndNumero(setor, numero)
                .map(leitoExistente -> {
                    leitoExistente.setOcupado(ocupado);
                    return leitoRepository.save(leitoExistente);
                })
                .orElseGet(() -> {
                    Leito l = new Leito();
                    l.setSetor(setor);
                    l.setNumero(numero);
                    l.setOcupado(ocupado);
                    return leitoRepository.save(l);
                });
    }

    // =========================
    // INTERNAR COM SUCESSO
    // =========================

    @Test
    void deveInternarComSucesso() throws Exception {

        Paciente paciente = criarPaciente("11111111101");
        criarLeito(Setor.UTI, 1, false);

        mockMvc.perform(post("/internacoes/" + paciente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "setor": "UTI",
                          "numeroLeito": 1
                        }
                        """))
                .andExpect(status().isOk());

        assertEquals(1, internacaoRepository.count());

        Internacao internacao = internacaoRepository.findAll().get(0);
        assertEquals(StatusInternacao.INTERNADO, internacao.getStatus());
    }

    // =========================
    // ERRO LEITO OCUPADO
    // =========================

    @Test
    void naoDeveInternarEmLeitoOcupado() throws Exception {

        Paciente paciente = criarPaciente("11111111102");
        criarLeito(Setor.UTI, 1, true);

        mockMvc.perform(post("/internacoes/" + paciente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "setor": "UTI",
                          "numeroLeito": 1
                        }
                        """))
                .andExpect(status().isConflict());
    }

    // =========================
    // ERRO PACIENTE JA INTERNADO
    // =========================

    @Test
    void naoDeveInternarPacienteJaInternado() throws Exception {

        Paciente paciente = criarPaciente("11111111103");
        Leito leito = criarLeito(Setor.UTI, 1, false);

        Internacao internacao = new Internacao();
        internacao.setPaciente(paciente);
        internacao.setLeito(leito);
        internacao.setStatus(StatusInternacao.INTERNADO);
        internacao.setDataEntrada(LocalDate.now());
        internacaoRepository.save(internacao);

        mockMvc.perform(post("/internacoes/" + paciente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "setor": "UTI",
                          "numeroLeito": 1
                        }
                        """))
                .andExpect(status().isConflict());
    }

    // =========================
    // ERRO LEITO INEXISTENTE
    // =========================

    @Test
    void naoDeveInternarEmLeitoInexistente() throws Exception {

        Paciente paciente = criarPaciente("11111111104");

        mockMvc.perform(post("/internacoes/" + paciente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "setor": "UTI",
                          "numeroLeito": 99
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    // =========================
    // TRANSFERIR
    // =========================

    @Test
    void deveTransferirPaciente() throws Exception {

        Paciente paciente = criarPaciente("11111111105");

        Leito leitoUTI = criarLeito(Setor.UTI, 1, true);
        criarLeito(Setor.ENFERMARIA, 1, false);

        Internacao internacao = new Internacao();
        internacao.setPaciente(paciente);
        internacao.setLeito(leitoUTI);
        internacao.setStatus(StatusInternacao.INTERNADO);
        internacao.setDataEntrada(LocalDate.now());
        internacao = internacaoRepository.save(internacao);

        mockMvc.perform(put("/internacoes/" + internacao.getId() + "/transferir")
                        .param("novoSetor", "ENFERMARIA"))
                .andExpect(status().isNoContent());

        var historico = movimentacaoRepository
                .findByInternacao_IdOrderByDataHoraAsc(internacao.getId());

        assertEquals(1, historico.size());
    }

    // =========================
    // NAO TRANSFERIR APOS ALTA
    // =========================

    @Test
    void naoDeveTransferirAposAlta() throws Exception {

        Paciente paciente = criarPaciente("11111111106");
        Leito leito = criarLeito(Setor.UTI, 1, true);

        Internacao internacao = new Internacao();
        internacao.setPaciente(paciente);
        internacao.setLeito(leito);
        internacao.setStatus(StatusInternacao.ALTA);
        internacao.setDataEntrada(LocalDate.now());
        internacao.setDataAlta(LocalDate.now());
        internacao = internacaoRepository.save(internacao);

        mockMvc.perform(put("/internacoes/" + internacao.getId() + "/transferir")
                        .param("novoSetor", "ENFERMARIA"))
                .andExpect(status().isConflict());
    }

    // =========================
    // DAR ALTA
    // =========================

    @Test
    void deveDarAlta() throws Exception {

        Paciente paciente = criarPaciente("11111111107");
        Leito leito = criarLeito(Setor.UTI, 1, true);

        Internacao internacao = new Internacao();
        internacao.setPaciente(paciente);
        internacao.setLeito(leito);
        internacao.setStatus(StatusInternacao.INTERNADO);
        internacao.setDataEntrada(LocalDate.now());
        internacao = internacaoRepository.save(internacao);

        mockMvc.perform(put("/internacoes/" + internacao.getId() + "/alta"))
                .andExpect(status().isOk());

        Internacao atualizada = internacaoRepository
                .findById(internacao.getId())
                .orElseThrow();

        assertEquals(StatusInternacao.ALTA, atualizada.getStatus());
    }
}