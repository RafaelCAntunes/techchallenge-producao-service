package com.lanchonete.producao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    private Long produtoId;
    private String nomeProduto;
    private Integer quantidade;
}