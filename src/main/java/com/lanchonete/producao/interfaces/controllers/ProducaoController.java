package com.lanchonete.producao.interfaces.controllers;

import com.lanchonete.producao.application.*;
import com.lanchonete.producao.domain.*;
import com.lanchonete.producao.interfaces.dto.IniciarProducaoRequestDTO;
import com.lanchonete.producao.interfaces.dto.ItemProducaoResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/producao")
public class ProducaoController {

    private final IniciarProducaoUseCase iniciarProducaoUseCase;
    private final AtualizarStatusProducaoUseCase atualizarStatusUseCase;
    private final ListarFilaProducaoUseCase listarFilaUseCase;
    private final ConsultarStatusProducaoUseCase consultarStatusUseCase;

    public ProducaoController(
            IniciarProducaoUseCase iniciarProducaoUseCase,
            AtualizarStatusProducaoUseCase atualizarStatusUseCase,
            ListarFilaProducaoUseCase listarFilaUseCase,
            ConsultarStatusProducaoUseCase consultarStatusUseCase) {
        this.iniciarProducaoUseCase = iniciarProducaoUseCase;
        this.atualizarStatusUseCase = atualizarStatusUseCase;
        this.listarFilaUseCase = listarFilaUseCase;
        this.consultarStatusUseCase = consultarStatusUseCase;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Void> iniciar(@RequestBody IniciarProducaoRequestDTO request) {

        List<com.lanchonete.producao.domain.ItemPedido> itens = request.getItens().stream()
                .map(dto -> new com.lanchonete.producao.domain.ItemPedido(
                        dto.getProdutoId(),
                        dto.getNomeProduto(),
                        dto.getQuantidade()
                ))
                .collect(java.util.stream.Collectors.toList());

        iniciarProducaoUseCase.executar(request.getPedidoId().toString(), itens);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{pedidoId}/avancar")
    public ResponseEntity<ItemProducaoResponseDTO> avancarStatus(
            @PathVariable String pedidoId) {

        // Avança para próximo status
        atualizarStatusUseCase.executar(pedidoId);

       ItemProducao item = consultarStatusUseCase.executar(pedidoId);

        return ResponseEntity.ok(ItemProducaoResponseDTO.from(item));
    }

    @GetMapping("/fila")
    public ResponseEntity<List<ItemProducaoResponseDTO>> listarFila(
            @RequestParam(required = false) String status) {

        List<ItemProducao> itens = status != null
                ? listarFilaUseCase.executarPorStatus(StatusProducao.valueOf(status))
                : listarFilaUseCase.executar();

        List<ItemProducaoResponseDTO> response = itens.stream()
                .map(ItemProducaoResponseDTO::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<ItemProducaoResponseDTO> consultar(@PathVariable String pedidoId) {
        ItemProducao item = consultarStatusUseCase.executar(pedidoId);

        return ResponseEntity.ok(ItemProducaoResponseDTO.from(item));
    }
}
