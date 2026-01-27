package com.lanchonete.producao.application;

import com.lanchonete.producao.domain.ItemPedido;
import com.lanchonete.producao.domain.ItemProducao;
import com.lanchonete.producao.domain.ProducaoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IniciarProducaoUseCaseTest {

    @Mock
    private ProducaoRepositoryPort repository;

    private IniciarProducaoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new IniciarProducaoUseCase(repository);
    }

    @Test
    void deveIniciarProducaoComSucesso() {
        
        String pedidoId = "PEDIDO-123";
        List<ItemPedido> itens = Arrays.asList(
                mock(ItemPedido.class),
                mock(ItemPedido.class)
        );

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.empty());

        
        useCase.executar(pedidoId, itens);

        
        verify(repository, times(1)).buscarPorPedidoId(pedidoId);
        verify(repository, times(1)).salvar(any(ItemProducao.class));
    }

    @Test
    void deveVerificarSeJaEstaEmProducaoAntesDeIniciar() {
        
        String pedidoId = "PEDIDO-123";
        List<ItemPedido> itens = Arrays.asList(mock(ItemPedido.class));

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.empty());

        
        useCase.executar(pedidoId, itens);

        
        verify(repository, times(1)).buscarPorPedidoId(pedidoId);
    }

    @Test
    void deveLancarExcecaoQuandoPedidoJaEstaEmProducao() {
        
        String pedidoId = "PEDIDO-DUPLICADO";
        List<ItemPedido> itens = Arrays.asList(mock(ItemPedido.class));
        ItemProducao itemExistente = mock(ItemProducao.class);

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemExistente));

         
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(pedidoId, itens)
        );

        assertEquals("Pedido já está em produção", exception.getMessage());
        verify(repository, times(1)).buscarPorPedidoId(pedidoId);
        verify(repository, never()).salvar(any(ItemProducao.class));
    }

    @Test
    void deveSalvarItemProducaoComDadosCorretos() {
        
        String pedidoId = "PEDIDO-456";
        List<ItemPedido> itens = Arrays.asList(
                mock(ItemPedido.class),
                mock(ItemPedido.class),
                mock(ItemPedido.class)
        );

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.empty());

        ArgumentCaptor<ItemProducao> captor = ArgumentCaptor.forClass(ItemProducao.class);

        
        useCase.executar(pedidoId, itens);

        
        verify(repository).salvar(captor.capture());
        ItemProducao itemSalvo = captor.getValue();
        assertNotNull(itemSalvo);
    }

    @Test
    void deveIniciarProducaoComListaVaziaDeItens() {
        
        String pedidoId = "PEDIDO-789";
        List<ItemPedido> itensVazios = Collections.emptyList();

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.empty());

        
        useCase.executar(pedidoId, itensVazios);

        
        verify(repository, times(1)).buscarPorPedidoId(pedidoId);
        verify(repository, times(1)).salvar(any(ItemProducao.class));
    }

    @Test
    void deveIniciarProducaoComUmItem() {
        
        String pedidoId = "PEDIDO-001";
        List<ItemPedido> itens = Collections.singletonList(mock(ItemPedido.class));

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.empty());

        
        useCase.executar(pedidoId, itens);

        
        verify(repository, times(1)).salvar(any(ItemProducao.class));
    }

    @Test
    void naoDeveSalvarQuandoPedidoJaExiste() {
        
        String pedidoId = "PEDIDO-EXISTENTE";
        List<ItemPedido> itens = Arrays.asList(mock(ItemPedido.class));
        ItemProducao itemExistente = mock(ItemProducao.class);

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemExistente));

         
        assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(pedidoId, itens));

        verify(repository, never()).salvar(any());
    }
}
