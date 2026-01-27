package com.lanchonete.producao.application;

import com.lanchonete.producao.domain.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AtualizarStatusProducaoUseCase {

    private final ProducaoRepositoryPort repository;
    private final PedidoNotificacaoPort pedidoNotificacao;

    public AtualizarStatusProducaoUseCase(
            ProducaoRepositoryPort repository,
            PedidoNotificacaoPort pedidoNotificacao) {
        this.repository = repository;
        this.pedidoNotificacao = pedidoNotificacao;
    }

    public void executar(String pedidoId) {
        ItemProducao item = repository.buscarPorPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Item não encontrado: " + pedidoId));

        // Validar se está em um status intermediário
        if (!item.podeAvancar()) {
            throw new IllegalStateException(
                    "Pedido já está finalizado e não pode avançar");
        }

        item.avancarStatus();

        repository.salvar(item);

        // Notifica serviço de Pedidos em toda mudança de status
        pedidoNotificacao.notificarStatusProducao(pedidoId, item.getStatus());

    }
}