package com.lanchonete.producao.infra.persistence;

import com.lanchonete.producao.domain.ItemProducao;
import com.lanchonete.producao.domain.ProducaoRepositoryPort;
import com.lanchonete.producao.domain.StatusProducao;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProducaoRepositoryAdapter implements ProducaoRepositoryPort {

    private final DynamoDbTable<ItemProducaoEntity> table;
    private final DynamoDbIndex<ItemProducaoEntity> statusIndex;
    private final ItemProducaoEntityMapper mapper;

    public ProducaoRepositoryAdapter(
            DynamoDbEnhancedClient enhancedClient,
            ItemProducaoEntityMapper mapper) {
        this.table = enhancedClient.table(
                "techchallenge-producao",
                TableSchema.fromBean(ItemProducaoEntity.class)
        );
        this.statusIndex = table.index("StatusProducaoIndex");
        this.mapper = mapper;
    }

    @Override
    public ItemProducao salvar(ItemProducao item) {
        ItemProducaoEntity entity = mapper.toEntity(item);
        table.putItem(entity);
        return item;
    }

    @Override
    public Optional<ItemProducao> buscarPorPedidoId(String pedidoId) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder()
                        .partitionValue(pedidoId)
                        .build());

        List<ItemProducaoEntity> results = table.query(queryConditional)
                .items()
                .stream()
                .limit(1)
                .collect(Collectors.toList());

        return results.isEmpty()
                ? Optional.empty()
                : Optional.of(mapper.toDomain(results.get(0)));
    }

    @Override
    public List<ItemProducao> listarPorStatus(StatusProducao status) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder()
                        .partitionValue(status.name())
                        .build());

        return statusIndex.query(queryConditional)
                .stream()
                .flatMap(page -> page.items().stream())
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemProducao> listarFilaProducao() {
        // Retorna tudo que não estiver FINALIZADO
        List<ItemProducao> fila = new java.util.ArrayList<>();

        fila.addAll(listarPorStatus(StatusProducao.RECEBIDO));
        fila.addAll(listarPorStatus(StatusProducao.EM_PREPARO));
        fila.addAll(listarPorStatus(StatusProducao.PRONTO));

        // Pega os mais antigos primeiro
        fila.sort((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));

        return fila;
    }

    @Override
    public void atualizarStatus(String pedidoId, StatusProducao novoStatus) {
        buscarPorPedidoId(pedidoId).ifPresent(item -> {
            item.avancarStatus();
            salvar(item);
        });
    }
}
