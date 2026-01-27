package com.lanchonete.producao.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {
    private Long produtoId;
    private String nomeProduto;
    private Integer quantidade;
}
