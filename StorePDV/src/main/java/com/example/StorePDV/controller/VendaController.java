package com.example.StorePDV.controller;

import com.example.StorePDV.model.ItemVenda;
import com.example.StorePDV.model.Venda;
import com.example.StorePDV.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    // RF-015 – inicia venda
    @PostMapping
    public ResponseEntity<Venda> criarVenda(@RequestBody Map<String, Object> body) {

        Venda venda = vendaService.criarVenda(null, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(venda);
    }

    // RF-015 – adiciona item à venda
    @PostMapping("/{id}/itens")
    public ResponseEntity<ItemVenda> adicionarItem(
            @PathVariable Long id,
            @RequestParam Long produtoId,
            @RequestParam int quantidade) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vendaService.adicionarItem(id, produtoId, quantidade));
    }

    // RF-015 – finaliza venda (RF-018 comprovante é gerado no Menu)
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Venda> finalizar(
            @PathVariable Long id,
            @RequestParam(defaultValue = "DINHEIRO") String tipoPagamento) {
        return ResponseEntity.ok(vendaService.finalizarVenda(id, tipoPagamento));
    }

    // RF-016
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Venda> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.cancelarVenda(id));
    }

    // RF-017
    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Venda>> listar() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemVenda>> listarItens(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.listarItens(id));
    }
}