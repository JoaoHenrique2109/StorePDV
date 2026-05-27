package com.example.StorePDV.service;

import com.example.StorePDV.model.Relatorio;
import com.example.StorePDV.model.Venda;
import com.example.StorePDV.repository.RelatorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    @Autowired private RelatorioRepository relatorioRepository;
    @Autowired private VendaService vendaService;
    @Autowired private ProdutoService produtoService;

    public Map<String, Object> gerarRelatorioVendasPorPeriodo(LocalDate inicio, LocalDate fim) {
        LocalDateTime dtInicio = inicio.atStartOfDay();
        LocalDateTime dtFim = fim.atTime(23, 59, 59);

        List<Venda> vendas = vendaService.buscarPorPeriodo(dtInicio, dtFim);
        double totalGeral = vendas.stream().mapToDouble(Venda::getTotal).sum();
        long totalVendas = vendas.size();

        // Salva o relatório gerado
        Relatorio relatorio = new Relatorio();
        relatorio.setTipo("VENDAS_PERIODO");
        relatorio.setDataGeracao(LocalDateTime.now());
        relatorio.setPeriodoInicio(inicio);
        relatorio.setPeriodoFim(fim);
        relatorioRepository.save(relatorio);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("periodo", inicio + " a " + fim);
        resultado.put("totalVendas", totalVendas);
        resultado.put("valorTotal", totalGeral);
        resultado.put("ticketMedio", totalVendas > 0 ? totalGeral / totalVendas : 0);
        resultado.put("vendas", vendas);
        return resultado;
    }

    public Map<String, Object> gerarResumoDia() {
        LocalDate hoje = LocalDate.now();
        return gerarRelatorioVendasPorPeriodo(hoje, hoje);
    }

    public Map<String, Object> gerarRelatorioEstoque() {
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("todos", produtoService.listarTodos());
        resultado.put("estoqueBaixo", produtoService.listarEstoqueBaixo());
        resultado.put("dataGeracao", LocalDateTime.now().toString());
        return resultado;
    }
}
