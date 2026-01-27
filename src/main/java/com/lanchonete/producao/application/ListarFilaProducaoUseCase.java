package com.lanchonete.producao.application;

import com.lanchonete.producao.domain.*;

import java.util.List;

public class ListarFilaProducaoUseCase {

    private final ProducaoRepositoryPort repository;

    public ListarFilaProducaoUseCase(ProducaoRepositoryPort repository) {
        this.repository = repository;
    }

    public List<ItemProducao> executar() {
        return repository.listarFilaProducao();
    }

    public List<ItemProducao> executarPorStatus(StatusProducao status) {
        return repository.listarPorStatus(status);
    }
}
