package com.example.StorePDV.controller;

import com.example.StorePDV.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    // RF-021
    @GetMapping("/vendas")
    public ResponseEntity<Map<String, Object>> vendas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(relatorioService.gerarRelatorioVendasPorPeriodo(inicio, fim));
    }

    @GetMapping("/resumo-dia")
    public ResponseEntity<Map<String, Object>> resumoDia() {
        return ResponseEntity.ok(relatorioService.gerarResumoDia());
    }

    @GetMapping("/estoque")
    public ResponseEntity<Map<String, Object>> estoque() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioEstoque());
    }
}