package com.lanchonete.producao.domain;

public enum StatusProducao {
    RECEBIDO,
    EM_PREPARO,
    PRONTO,
    FINALIZADO;

    public StatusProducao proximo() {
        return switch (this) {
            case RECEBIDO -> EM_PREPARO;
            case EM_PREPARO -> PRONTO;
            case PRONTO -> FINALIZADO;
            case FINALIZADO -> throw new IllegalStateException(
                    "Pedido já está finalizado, não pode avançar");
        };
    }

    public boolean temProximo() {
        return this != FINALIZADO;
    }
}
