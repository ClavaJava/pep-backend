package br.com.hospital.pep.service;

import br.com.hospital.pep.entity.Internacao;
import br.com.hospital.pep.entity.Leito;
import br.com.hospital.pep.entity.MovimentacaoInternacao;
import br.com.hospital.pep.enums.Setor;
import br.com.hospital.pep.enums.StatusInternacao;
import br.com.hospital.pep.repository.InternacaoRepository;
import br.com.hospital.pep.repository.LeitoRepository;
import br.com.hospital.pep.repository.MovimentacaoInternacaoRepository;
import br.com.hospital.pep.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InternacaoServiceTest {

    private InternacaoService service;

    private InternacaoRepository internacaoRepository;
    private PacienteRepository pacienteRepository;
    private LeitoRepository leitoRepository;
    private MovimentacaoInternacaoRepository movimentacaoRepository;

    @BeforeEach
    void setup() {
        internacaoRepository = mock(InternacaoRepository.class);
        pacienteRepository = mock(PacienteRepository.class);
        leitoRepository = mock(LeitoRepository.class);
        movimentacaoRepository = mock(MovimentacaoInternacaoRepository.class);

        service = new InternacaoService(
                internacaoRepository,
                pacienteRepository,
                leitoRepository,
                movimentacaoRepository
        );
    }

    @Test
    void deveTransferirPacienteERegistrarMovimentacao() {

        // Internação existente com leito UTI
        Leito leitoAtual = new Leito();
        leitoAtual.setSetor(Setor.UTI);
        leitoAtual.setOcupado(true);

        Internacao internacao = new Internacao();
        internacao.setStatus(StatusInternacao.INTERNADO);
        internacao.setLeito(leitoAtual);

        when(internacaoRepository.findById(1L)).thenReturn(Optional.of(internacao));

        // Leito disponível na enfermaria
        Leito novoLeito = new Leito();
        novoLeito.setSetor(Setor.ENFERMARIA);
        novoLeito.setOcupado(false);

        when(leitoRepository.findFirstBySetorAndOcupadoFalse(Setor.ENFERMARIA))
                .thenReturn(Optional.of(novoLeito));

        // ação
        service.transferir(1L, Setor.ENFERMARIA);

        // asserts principais
        assertFalse(leitoAtual.isOcupado(), "Leito antigo deveria ser liberado");
        assertTrue(novoLeito.isOcupado(), "Novo leito deveria ser ocupado");
        assertSame(novoLeito, internacao.getLeito(), "Internação deveria apontar para o novo leito");

        // verifica que registrou movimentação
        ArgumentCaptor<MovimentacaoInternacao> captor = ArgumentCaptor.forClass(MovimentacaoInternacao.class);
        verify(movimentacaoRepository).save(captor.capture());

        MovimentacaoInternacao mov = captor.getValue();
        assertSame(internacao, mov.getInternacao());
        assertEquals(Setor.UTI, mov.getSetorOrigem());
        assertEquals(Setor.ENFERMARIA, mov.getSetorDestino());
        assertNotNull(mov.getDataHora());
    }
}