package com.lanchonete.producao.interfaces.dto;

import com.lanchonete.producao.domain.ItemProducao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemProducaoResponseDTO {

    private String pedidoId;
    private List<ItemPedidoDTO> itens;
    private String status;
    private Instant iniciadoEm;
    private Instant atualizadoEm;

    public static ItemProducaoResponseDTO from(ItemProducao item) {
        List<ItemPedidoDTO> itensDTO = null;
        if (item.getItens() != null) {
            itensDTO = item.getItens().stream()
                    .map(i -> new ItemPedidoDTO(
                            i.getProdutoId(),
                            i.getNomeProduto(),
                            i.getQuantidade()
                    ))
                    .collect(Collectors.toList());
        }

        return new ItemProducaoResponseDTO(
                item.getPedidoId(),
                itensDTO,
                item.getStatus().name(),
                item.getIniciadoEm(),
                item.getAtualizadoEm()
        );
    }
}
