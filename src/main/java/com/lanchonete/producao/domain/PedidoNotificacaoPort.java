package com.lanchonete.producao.domain;

public interface PedidoNotificacaoPort {
    void notificarStatusProducao(String pedidoId, StatusProducao status);
}