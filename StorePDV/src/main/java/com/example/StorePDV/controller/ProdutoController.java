package com.example.StorePDV.controller;

import com.example.StorePDV.model.Produto;
import com.example.StorePDV.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // RF-011
    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.cadastrar(produto));
    }

    // RF-012
    @PutMapping("/{id}")
    public ResponseEntity<Produto> alterar(@PathVariable Long id, @RequestBody Produto dados) {
        return ResponseEntity.ok(produtoService.alterar(id, dados));
    }

    // RF-013
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        produtoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    // RF-014
    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    // RF-019
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Produto>> estoqueBaixo() {
        return ResponseEntity.ok(produtoService.listarEstoqueBaixo());
    }
}