package com.lanchonete.producao.infra.external;

import com.lanchonete.producao.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PedidoNotificacaoAdapter implements PedidoNotificacaoPort {

    private final WebClient webClient;

    public PedidoNotificacaoAdapter(
            WebClient.Builder webClientBuilder,
            @Value("${services.pedidos.url}") String pedidosUrl) {
        this.webClient = webClientBuilder
                .baseUrl(pedidosUrl)
                .build();
    }

    @Override
    public void notificarStatusProducao(String pedidoId, StatusProducao status) {

        webClient.post()
                .uri("/callback/{id}/producao", pedidoId)
                .bodyValue(status)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}

