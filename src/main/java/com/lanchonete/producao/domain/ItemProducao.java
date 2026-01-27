package com.lanchonete.producao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemProducao {

    private String pedidoId;
    private Long timestamp;
    private List<ItemPedido> itens;
    private StatusProducao status;
    private Instant iniciadoEm;
    private Instant atualizadoEm;

    public ItemProducao(String pedidoId, List<ItemPedido> itens) {
        this.pedidoId = pedidoId;
        this.timestamp = Instant.now().toEpochMilli();
        this.itens = itens;
        this.status = StatusProducao.RECEBIDO;
        this.iniciadoEm = Instant.now();
        this.atualizadoEm = Instant.now();
    }

    public void avancarStatus() {
        this.status = this.status.proximo();
        this.atualizadoEm = Instant.now();
    }

    public boolean podeAvancar() {
        return this.status.temProximo();
    }
}
