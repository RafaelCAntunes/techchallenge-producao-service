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

        entity.setStatusProducao(item.getStatus().name());
        entity.setIniciadoEm(item.getIniciadoEm().getEpochSecond());
        entity.setAtualizadoEm(item.getAtualizadoEm().getEpochSecond());

        entity.setItens(
                item.getItens().stream()
                        .map(this::toItemPedidoEntity)
                        .toList()
        );

        return entity;
    }

    public ItemProducao toDomain(ItemProducaoEntity entity) {
        return new ItemProducao(
                entity.getPedidoId(),
                entity.getTimestamp(),
                entity.getItens().stream()
                        .map(this::toItemPedidoDomain)
                        .toList(),
                StatusProducao.valueOf(entity.getStatusProducao()),
                Instant.ofEpochSecond(entity.getIniciadoEm()),
                Instant.ofEpochSecond(entity.getAtualizadoEm())
        );
    }

    private ItemPedidoEntity toItemPedidoEntity(ItemPedido item) {
        ItemPedidoEntity entity = new ItemPedidoEntity();
        entity.setProdutoId(item.getProdutoId());
        entity.setNomeProduto(item.getNomeProduto());
        entity.setQuantidade(item.getQuantidade());
        return entity;
    }

    private ItemPedido toItemPedidoDomain(ItemPedidoEntity entity) {
        return new ItemPedido(
                entity.getProdutoId(),
                entity.getNomeProduto(),
                entity.getQuantidade()
        );
    }

}