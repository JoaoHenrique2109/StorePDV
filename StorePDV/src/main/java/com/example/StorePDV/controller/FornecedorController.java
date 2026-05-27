package com.example.StorePDV.controller;

import com.example.StorePDV.model.Fornecedor;
import com.example.StorePDV.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    // RF-007
    @PostMapping
    public ResponseEntity<Fornecedor> cadastrar(@RequestBody Fornecedor fornecedor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorService.cadastrar(fornecedor));
    }

    // RF-008
    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> alterar(@PathVariable Long id, @RequestBody Fornecedor dados) {
        return ResponseEntity.ok(fornecedorService.alterar(id, dados));
    }

    // RF-009
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        fornecedorService.remover(id);
        return ResponseEntity.noContent().build();
    }

    // RF-010
    @GetMapping
    public ResponseEntity<List<Fornecedor>> listar() {
        return ResponseEntity.ok(fornecedorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.buscarPorId(id));
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<Fornecedor> buscarPorCnpj(@PathVariable String cnpj) {
        return ResponseEntity.ok(fornecedorService.buscarPorCnpj(cnpj));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Fornecedor>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(fornecedorService.buscarPorNome(nome));
    }
}