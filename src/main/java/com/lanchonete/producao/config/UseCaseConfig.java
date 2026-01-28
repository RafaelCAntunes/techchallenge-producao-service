package com.lanchonete.producao.config;

import com.lanchonete.producao.application.AtualizarStatusProducaoUseCase;
import com.lanchonete.producao.application.ConsultarStatusProducaoUseCase;
import com.lanchonete.producao.application.IniciarProducaoUseCase;
import com.lanchonete.producao.application.ListarFilaProducaoUseCase;
import com.lanchonete.producao.domain.PedidoNotificacaoPort;
import com.lanchonete.producao.domain.ProducaoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public AtualizarStatusProducaoUseCase atualizarStatusProducaoUseCase(
            ProducaoRepositoryPort repository,
            PedidoNotificacaoPort pedidoNotificacao) {
        return new AtualizarStatusProducaoUseCase(repository, pedidoNotificacao);
    }

    @Bean
    public ConsultarStatusProducaoUseCase consultarStatusProducaoUseCase(
            ProducaoRepositoryPort repository) {
        return new ConsultarStatusProducaoUseCase(repository);
    }

    @Bean
    public IniciarProducaoUseCase iniciarProducaoUseCase(
            ProducaoRepositoryPort repository) {
        return new IniciarProducaoUseCase(repository);
    }

    @Bean
    public ListarFilaProducaoUseCase listarFilaProducaoUseCase(
            ProducaoRepositoryPort repository) {
        return new ListarFilaProducaoUseCase(repository);
    }


}
