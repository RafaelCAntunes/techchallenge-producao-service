package com.lanchonete.producao.application;

import com.lanchonete.producao.domain.ItemProducao;
import com.lanchonete.producao.domain.ProducaoRepositoryPort;
import com.lanchonete.producao.domain.StatusProducao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarFilaProducaoUseCaseTest {

    @Mock
    private ProducaoRepositoryPort repository;

    private ListarFilaProducaoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarFilaProducaoUseCase(repository);
    }

    @Test
    void deveListarTodaFilaDeProducao() {
      
        List<ItemProducao> itensEsperados = Arrays.asList(
                mock(ItemProducao.class),
                mock(ItemProducao.class),
                mock(ItemProducao.class)
        );

        when(repository.listarFilaProducao())
                .thenReturn(itensEsperados);

        
        List<ItemProducao> resultado = useCase.executar();

        
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals(itensEsperados, resultado);
        verify(repository, times(1)).listarFilaProducao();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverItens() {
      
        when(repository.listarFilaProducao())
                .thenReturn(Collections.emptyList());

        
        List<ItemProducao> resultado = useCase.executar();

        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).listarFilaProducao();
    }

    @Test
    void deveListarItensPorStatusRecebido() {
      
        StatusProducao status = StatusProducao.RECEBIDO;
        List<ItemProducao> itensRecebidos = Arrays.asList(
                mock(ItemProducao.class),
                mock(ItemProducao.class)
        );

        when(repository.listarPorStatus(status))
                .thenReturn(itensRecebidos);

        
        List<ItemProducao> resultado = useCase.executarPorStatus(status);

        
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(itensRecebidos, resultado);
        verify(repository, times(1)).listarPorStatus(status);
    }

    @Test
    void deveListarItensPorStatusEmPreparacao() {
      
        StatusProducao status = StatusProducao.EM_PREPARO;
        List<ItemProducao> itensEmPreparacao = Collections.singletonList(
                mock(ItemProducao.class)
        );

        when(repository.listarPorStatus(status))
                .thenReturn(itensEmPreparacao);

        
        List<ItemProducao> resultado = useCase.executarPorStatus(status);

        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, times(1)).listarPorStatus(status);
    }

    @Test
    void deveListarItensPorStatusPronto() {
      
        StatusProducao status = StatusProducao.PRONTO;
        List<ItemProducao> itensProntos = Arrays.asList(
                mock(ItemProducao.class),
                mock(ItemProducao.class),
                mock(ItemProducao.class),
                mock(ItemProducao.class)
        );

        when(repository.listarPorStatus(status))
                .thenReturn(itensProntos);

        
        List<ItemProducao> resultado = useCase.executarPorStatus(status);

        
        assertNotNull(resultado);
        assertEquals(4, resultado.size());
        assertEquals(itensProntos, resultado);
        verify(repository, times(1)).listarPorStatus(status);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverItensPorStatus() {
      
        StatusProducao status = StatusProducao.FINALIZADO;

        when(repository.listarPorStatus(status))
                .thenReturn(Collections.emptyList());

        
        List<ItemProducao> resultado = useCase.executarPorStatus(status);

        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).listarPorStatus(status);
    }

    @Test
    void deveRetornarMesmaListaDoRepositorio() {
      
        List<ItemProducao> listaEsperada = Arrays.asList(
                mock(ItemProducao.class),
                mock(ItemProducao.class)
        );

        when(repository.listarFilaProducao())
                .thenReturn(listaEsperada);

        
        List<ItemProducao> resultado = useCase.executar();

        
        assertSame(listaEsperada, resultado, "Deve retornar a mesma instância do repositório");
    }

    @Test
    void deveRetornarMesmaListaPorStatusDoRepositorio() {
      
        StatusProducao status = StatusProducao.EM_PREPARO;
        List<ItemProducao> listaEsperada = Collections.singletonList(
                mock(ItemProducao.class)
        );

        when(repository.listarPorStatus(status))
                .thenReturn(listaEsperada);

        
        List<ItemProducao> resultado = useCase.executarPorStatus(status);

        
        assertSame(listaEsperada, resultado, "Deve retornar a mesma instância do repositório");
    }

    @Test
    void deveValidarChamadaApenasParaMetodoCorreto() {
      
        when(repository.listarFilaProducao())
                .thenReturn(Collections.emptyList());

        
        useCase.executar();

        
        verify(repository, times(1)).listarFilaProducao();
        verify(repository, never()).listarPorStatus(any());
    }

    @Test
    void deveValidarChamadaApenasParaStatusCorreto() {
      
        StatusProducao status = StatusProducao.RECEBIDO;
        when(repository.listarPorStatus(status))
                .thenReturn(Collections.emptyList());

        
        useCase.executarPorStatus(status);

        
        verify(repository, times(1)).listarPorStatus(status);
        verify(repository, never()).listarFilaProducao();
    }
}
