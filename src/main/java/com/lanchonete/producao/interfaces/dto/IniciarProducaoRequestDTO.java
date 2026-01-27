package com.lanchonete.producao.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IniciarProducaoRequestDTO {

    @NotNull(message = "pedidoId é obrigatório")
    private Long pedidoId;

    @NotNull(message = "pedidoId é obrigatório")
    private List<ItemPedidoDTO> itens;
}
