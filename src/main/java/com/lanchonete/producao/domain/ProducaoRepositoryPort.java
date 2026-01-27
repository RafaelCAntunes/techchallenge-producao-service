package com.lanchonete.producao.domain;

import java.util.List;
import java.util.Optional;

public interface ProducaoRepositoryPort {
    ItemProducao salvar(ItemProducao item);
    Optional<ItemProducao> buscarPorPedidoId(String pedidoId);
    List<ItemProducao> listarPorStatus(StatusProducao status);
    List<ItemProducao> listarFilaProducao();
    void atualizarStatus(String pedidoId, StatusProducao novoStatus);
}