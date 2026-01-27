package com.lanchonete.producao.application;

import com.lanchonete.producao.domain.*;

import java.util.List;
import java.util.Optional;


public class IniciarProducaoUseCase {

    private final ProducaoRepositoryPort repository;

    public IniciarProducaoUseCase(ProducaoRepositoryPort repository) {
        this.repository = repository;
    }

    public void executar(String pedidoId, List<ItemPedido> itens) {

        // Verifica se já está em produção
        Optional<ItemProducao> existente = repository.buscarPorPedidoId(pedidoId);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Pedido já está em produção");
        }

        ItemProducao item = new ItemProducao(pedidoId, itens);

        repository.salvar(item);

    }
}