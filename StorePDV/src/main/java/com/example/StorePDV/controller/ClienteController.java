package com.example.StorePDV.controller;

import com.example.StorePDV.model.Cliente;
import com.example.StorePDV.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // RF-003
    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.cadastrar(cliente));
    }

    // RF-004
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> alterar(@PathVariable Long id, @RequestBody Cliente dados) {
        return ResponseEntity.ok(clienteService.alterar(id, dados));
    }

    // RF-005
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        clienteService.remover(id);
        return ResponseEntity.noContent().build();
    }

    // RF-006
    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Cliente> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(clienteService.buscarPorCpf(cpf));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(clienteService.buscarPorNome(nome));
    }
}