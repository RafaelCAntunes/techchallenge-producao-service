package com.lanchonete.producao.application;

import com.lanchonete.producao.domain.ItemProducao;
import com.lanchonete.producao.domain.PedidoNotificacaoPort;
import com.lanchonete.producao.domain.ProducaoRepositoryPort;
import com.lanchonete.producao.domain.StatusProducao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarStatusProducaoUseCaseTest {

    @Mock
    private ProducaoRepositoryPort repository;

    @Mock
    private PedidoNotificacaoPort pedidoNotificacao;

    @Mock
    private ItemProducao itemProducao;

    private AtualizarStatusProducaoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarStatusProducaoUseCase(repository, pedidoNotificacao);
    }

    @Test
    void deveAtualizarStatusComSucesso() {
        
        String pedidoId = "PEDIDO-123";
        StatusProducao novoStatus = StatusProducao.EM_PREPARO;

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemProducao));
        when(itemProducao.podeAvancar()).thenReturn(true);
        when(itemProducao.getStatus()).thenReturn(novoStatus);

        
        useCase.executar(pedidoId);

        
        verify(repository, times(1)).buscarPorPedidoId(pedidoId);
        verify(itemProducao, times(1)).podeAvancar();
        verify(itemProducao, times(1)).avancarStatus();
        verify(repository, times(1)).salvar(itemProducao);
        verify(pedidoNotificacao, times(1)).notificarStatusProducao(pedidoId, novoStatus);
    }

    @Test
    void deveLancarExcecaoQuandoItemNaoEncontrado() {
        
        String pedidoId = "PEDIDO-INEXISTENTE";

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.empty());

        
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(pedidoId)
        );

        assertTrue(exception.getMessage().contains("Item não encontrado"));
        assertTrue(exception.getMessage().contains(pedidoId));
        verify(repository, times(1)).buscarPorPedidoId(pedidoId);
        verify(repository, never()).salvar(any());
        verify(pedidoNotificacao, never()).notificarStatusProducao(any(), any());
    }

    @Test
    void deveLancarExcecaoQuandoNaoPodeAvancar() {
        
        String pedidoId = "PEDIDO-FINALIZADO";

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemProducao));
        when(itemProducao.podeAvancar()).thenReturn(false);

        
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> useCase.executar(pedidoId)
        );

        assertEquals("Pedido já está finalizado e não pode avançar", exception.getMessage());
        verify(repository, times(1)).buscarPorPedidoId(pedidoId);
        verify(itemProducao, times(1)).podeAvancar();
        verify(itemProducao, never()).avancarStatus();
        verify(repository, never()).salvar(any());
        verify(pedidoNotificacao, never()).notificarStatusProducao(any(), any());
    }

    @Test
    void deveExecutarOperacoesNaOrdemCorreta() {
        
        String pedidoId = "PEDIDO-456";
        StatusProducao status = StatusProducao.PRONTO;

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemProducao));
        when(itemProducao.podeAvancar()).thenReturn(true);
        when(itemProducao.getStatus()).thenReturn(status);

        InOrder inOrder = inOrder(repository, itemProducao, pedidoNotificacao);

        
        useCase.executar(pedidoId);

        inOrder.verify(repository).buscarPorPedidoId(pedidoId);
        inOrder.verify(itemProducao).podeAvancar();
        inOrder.verify(itemProducao).avancarStatus();
        inOrder.verify(repository).salvar(itemProducao);
        inOrder.verify(pedidoNotificacao).notificarStatusProducao(pedidoId, status);
    }

    @Test
    void deveNotificarComStatusCorreto() {
        
        String pedidoId = "PEDIDO-789";
        StatusProducao statusEsperado = StatusProducao.EM_PREPARO;

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemProducao));
        when(itemProducao.podeAvancar()).thenReturn(true);
        when(itemProducao.getStatus()).thenReturn(statusEsperado);

        
        useCase.executar(pedidoId);

        
        verify(pedidoNotificacao).notificarStatusProducao(eq(pedidoId), eq(statusEsperado));
    }

    @Test
    void deveSalvarItemAntesDeNotificar() {
        
        String pedidoId = "PEDIDO-001";
        StatusProducao status = StatusProducao.PRONTO;

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemProducao));
        when(itemProducao.podeAvancar()).thenReturn(true);
        when(itemProducao.getStatus()).thenReturn(status);

        InOrder inOrder = inOrder(repository, pedidoNotificacao);

        
        useCase.executar(pedidoId);

        
        inOrder.verify(repository).salvar(itemProducao);
        inOrder.verify(pedidoNotificacao).notificarStatusProducao(pedidoId, status);
    }

    @Test
    void deveAvancarStatusAntesDeObterNovoStatus() {
        
        String pedidoId = "PEDIDO-002";
        StatusProducao statusAposAvanco = StatusProducao.PRONTO;

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemProducao));
        when(itemProducao.podeAvancar()).thenReturn(true);
        when(itemProducao.getStatus()).thenReturn(statusAposAvanco);

        InOrder inOrder = inOrder(itemProducao);

        
        useCase.executar(pedidoId);

        
        inOrder.verify(itemProducao).avancarStatus();
        inOrder.verify(itemProducao).getStatus();
    }

    @Test
    void deveValidarPodeAvancarAntesDeAvancar() {
        
        String pedidoId = "PEDIDO-003";

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemProducao));
        when(itemProducao.podeAvancar()).thenReturn(true);
        when(itemProducao.getStatus()).thenReturn(StatusProducao.EM_PREPARO);

        InOrder inOrder = inOrder(itemProducao);

        
        useCase.executar(pedidoId);

        
        inOrder.verify(itemProducao).podeAvancar();
        inOrder.verify(itemProducao).avancarStatus();
    }

    @Test
    void naoDeveSalvarNemNotificarQuandoNaoPodeAvancar() {
        
        String pedidoId = "PEDIDO-BLOQUEADO";

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemProducao));
        when(itemProducao.podeAvancar()).thenReturn(false);

        
        assertThrows(IllegalStateException.class, () -> useCase.executar(pedidoId));

        verify(repository, never()).salvar(any());
        verify(pedidoNotificacao, never()).notificarStatusProducao(any(), any());
    }

    @Test
    void deveAtualizarParaDiferentesStatus() {
        
        String pedidoId = "PEDIDO-MULTI";
        StatusProducao[] statusSequence = {
                StatusProducao.RECEBIDO,
                StatusProducao.EM_PREPARO,
                StatusProducao.PRONTO
        };

        for (StatusProducao status : statusSequence) {
            when(repository.buscarPorPedidoId(pedidoId))
                    .thenReturn(Optional.of(itemProducao));
            when(itemProducao.podeAvancar()).thenReturn(true);
            when(itemProducao.getStatus()).thenReturn(status);

            
            useCase.executar(pedidoId);

            
            verify(pedidoNotificacao).notificarStatusProducao(pedidoId, status);

            reset(repository, itemProducao, pedidoNotificacao);
        }
    }
}