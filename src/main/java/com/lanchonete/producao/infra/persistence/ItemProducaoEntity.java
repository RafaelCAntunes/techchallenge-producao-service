package com.lanchonete.producao.infra.persistence;

import com.lanchonete.producao.domain.ItemPedido;
import com.lanchonete.producao.domain.StatusProducao;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.List;

@DynamoDbBean
public class ItemProducaoEntity {

    private String pedidoId;
    private Long timestamp;
    private String statusProducao;
    private Long iniciadoEm;
    private List<ItemPedido> itens;
    private Long atualizadoEm;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("pedidoId")
    public String getPedidoId() { return pedidoId; }
    public void setPedidoId(String pedidoId) { this.pedidoId = pedidoId; }

    @DynamoDbSortKey
    @DynamoDbAttribute("timestamp")
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    @DynamoDbSecondaryPartitionKey(indexNames = "StatusProducaoIndex")
    @DynamoDbAttribute("statusProducao")
    public String getStatusProducao() { return statusProducao; }
    public void setStatusProducao(String statusProducao) {
        this.statusProducao = statusProducao;
    }

    @DynamoDbAttribute("itens")
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }


    @DynamoDbAttribute("iniciadoEm")
    public Long getIniciadoEm() { return iniciadoEm; }
    public void setIniciadoEm(Long iniciadoEm) { this.iniciadoEm = iniciadoEm; }

    @DynamoDbSecondarySortKey(indexNames = "StatusProducaoIndex")
    @DynamoDbAttribute("atualizadoEm")
    public Long getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(Long atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
