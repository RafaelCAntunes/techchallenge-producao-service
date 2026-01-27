package com.lanchonete.producao.infra.persistence;

import com.lanchonete.producao.domain.*;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ItemProducaoEntityMapper {

    public ItemProducaoEntity toEntity(ItemProducao item) {
        ItemProducaoEntity entity = new ItemProducaoEntity();
        entity.setPedidoId(item.getPedidoId());
        entity.setTimestamp(item.getTimestamp());
        entity.setItens(item.getItens());
        entity.setStatusProducao(item.getStatus().name());
        entity.setIniciadoEm(item.getIniciadoEm().getEpochSecond());
        entity.setAtualizadoEm(item.getAtualizadoEm().getEpochSecond());
        return entity;
    }

    public ItemProducao toDomain(ItemProducaoEntity entity) {
        return new ItemProducao(
                entity.getPedidoId(),
                entity.getTimestamp(),
                entity.getItens(),
                StatusProducao.valueOf(entity.getStatusProducao()),
                Instant.ofEpochSecond(entity.getIniciadoEm()),
                Instant.ofEpochSecond(entity.getAtualizadoEm())
        );
    }
}