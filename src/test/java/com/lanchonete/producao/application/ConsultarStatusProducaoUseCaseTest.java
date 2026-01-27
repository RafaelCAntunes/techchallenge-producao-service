package com.lanchonete.producao.application;

import com.lanchonete.producao.domain.ItemProducao;
import com.lanchonete.producao.domain.ProducaoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarStatusProducaoUseCaseTest {

    @Mock
    private ProducaoRepositoryPort repository;

    private ConsultarStatusProducaoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConsultarStatusProducaoUseCase(repository);
    }

    @Test
    void deveConsultarStatusProducaoComSucesso() {
        
        String pedidoId = "PEDIDO-123";
        ItemProducao itemProducao = mock(ItemProducao.class);

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemProducao));

        
        ItemProducao resultado = useCase.executar(pedidoId);

        
        assertNotNull(resultado);
        assertEquals(itemProducao, resultado);
        verify(repository, times(1)).buscarPorPedidoId(pedidoId);
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

        assertTrue(exception.getMessage().contains("Item de produção não encontrado"));
        assertTrue(exception.getMessage().contains(pedidoId));
        verify(repository, times(1)).buscarPorPedidoId(pedidoId);
    }

    @Test
    void deveValidarChamadaAoRepositorio() {
        
        String pedidoId = "PEDIDO-456";
        ItemProducao itemProducao = mock(ItemProducao.class);

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemProducao));

        
        useCase.executar(pedidoId);

        
        verify(repository, times(1)).buscarPorPedidoId(pedidoId);
        verify(repository, never()).buscarPorPedidoId(argThat(id -> !id.equals(pedidoId)));
    }

    @Test
    void deveRetornarMesmoObjetoDoRepositorio() {
        
        String pedidoId = "PEDIDO-789";
        ItemProducao itemEsperado = mock(ItemProducao.class);

        when(repository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(itemEsperado));

        
        ItemProducao resultado = useCase.executar(pedidoId);

        
        assertSame(itemEsperado, resultado, "Deve retornar a mesma instância do repositório");
    }
}
