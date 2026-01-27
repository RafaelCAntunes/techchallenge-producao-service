package com.lanchonete.producao.application;

import com.lanchonete.producao.domain.ItemProducao;
import com.lanchonete.producao.domain.ProducaoRepositoryPort;

public class ConsultarStatusProducaoUseCase {

    private final ProducaoRepositoryPort repository;

    public ConsultarStatusProducaoUseCase(ProducaoRepositoryPort repository) {
        this.repository = repository;
    }

    public ItemProducao executar(String pedidoId) {
        return repository.buscarPorPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Item de produção não encontrado para pedido numero: " + pedidoId));
    }
}

