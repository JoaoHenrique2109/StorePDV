package com.example.StorePDV.controller;

import com.example.StorePDV.model.Usuario;
import com.example.StorePDV.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // RF-001
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");
        return ResponseEntity.ok(usuarioService.login(email, senha));
    }

    // RF-002
    @PostMapping("/recuperar-senha")
    public ResponseEntity<Map<String, String>> recuperarSenha(@RequestBody Map<String, String> body) {
        usuarioService.recuperarSenha(body.get("email"));
        return ResponseEntity.ok(Map.of("mensagem", "Link de recuperação enviado para o e-mail informado."));
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
}