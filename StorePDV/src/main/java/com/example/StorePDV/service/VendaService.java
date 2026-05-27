package com.example.StorePDV.service;

import com.example.StorePDV.exception.EntidadeNaoEncontradaException;
import com.example.StorePDV.exception.OperacaoNaoPermitidaException;
import com.example.StorePDV.model.*;
import com.example.StorePDV.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {

    @Autowired private VendaRepository vendaRepository;
    @Autowired private ItemVendaRepository itemVendaRepository;
    @Autowired private PagamentoRepository pagamentoRepository;
    @Autowired private PagamentoPixRepository pagamentoPixRepository;
    @Autowired private ProdutoService produtoService;

    @Transactional
    public Venda criarVenda(Usuario usuario, Cliente cliente) {
        Venda venda = new Venda();
        venda.setDataVenda(LocalDateTime.now());
        venda.setUsuario(usuario);
        venda.setCliente(cliente); // pode ser null (consumidor final)
        venda.setStatus(Venda.Status.ABERTA);
        return vendaRepository.save(venda);
    }

    @Transactional
    public ItemVenda adicionarItem(Long vendaId, Long produtoId, int quantidade) {
        Venda venda = buscarPorId(vendaId);

        if (venda.getStatus() != Venda.Status.ABERTA)
            throw new OperacaoNaoPermitidaException("Não é possível adicionar itens a uma venda " + venda.getStatus() + ".");

        Produto produto = produtoService.buscarPorId(produtoId);
        produtoService.verificarEstoque(produto, quantidade);

        ItemVenda item = new ItemVenda();
        item.setVenda(venda);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());

        // RF-019 – abate estoque automaticamente
        produtoService.abaterEstoque(produto, quantidade);

        return itemVendaRepository.save(item);
    }

    @Transactional
    public Venda finalizarVenda(Long vendaId, String tipoPagamento) {
        Venda venda = buscarPorId(vendaId);

        if (venda.getStatus() != Venda.Status.ABERTA)
            throw new OperacaoNaoPermitidaException("Venda já está " + venda.getStatus() + ".");

        List<ItemVenda> itens = itemVendaRepository.findByVenda_Id(vendaId);
        if (itens.isEmpty())
            throw new OperacaoNaoPermitidaException("Não é possível finalizar uma venda sem itens.");

        double total = itens.stream().mapToDouble(ItemVenda::getSubtotal).sum();
        venda.setTotal(total);

        registrarPagamento(venda, total, tipoPagamento);

        venda.setStatus(Venda.Status.FINALIZADA);
        return vendaRepository.save(venda);
    }

    @Transactional
    public Venda cancelarVenda(Long vendaId) {
        Venda venda = buscarPorId(vendaId);

        if (venda.getStatus() == Venda.Status.CANCELADA)
            throw new OperacaoNaoPermitidaException("Esta venda já está cancelada.");
        if (venda.getStatus() == Venda.Status.FINALIZADA)
            throw new OperacaoNaoPermitidaException("Não é possível cancelar uma venda já finalizada.");

        // Reverte estoque de cada item
        List<ItemVenda> itens = itemVendaRepository.findByVenda_Id(vendaId);
        for (ItemVenda item : itens) {
            produtoService.repor(item.getProduto(), item.getQuantidade());
        }

        venda.setStatus(Venda.Status.CANCELADA);
        return vendaRepository.save(venda);
    }

    public Venda vincularCliente(Long vendaId, Cliente cliente) {
        Venda venda = buscarPorId(vendaId);
        if (venda.getStatus() != Venda.Status.ABERTA)
            throw new OperacaoNaoPermitidaException("Apenas vendas abertas podem ser alteradas.");
        venda.setCliente(cliente);
        return vendaRepository.save(venda);
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda", id));
    }

    public List<Venda> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findVendasPorPeriodo(inicio, fim);
    }

    public List<ItemVenda> listarItens(Long vendaId) {
        buscarPorId(vendaId); // garante que a venda existe
        return itemVendaRepository.findByVenda_Id(vendaId);
    }

    private void registrarPagamento(Venda venda, double total, String tipo) {
        if ("PIX".equalsIgnoreCase(tipo)) {
            PagamentoPix pix = new PagamentoPix();
            pix.setTipo("PIX");
            pix.setValor(total);
            pix.setStatus("APROVADO");
            pix.setData(LocalDate.now());
            pix.setVenda(venda);
            pix.setChavePix("storePDV@loja.com");
            pix.setQrCode("QR_SIMULADO_" + System.currentTimeMillis());
            pix.setExpiracao(LocalDateTime.now().plusMinutes(30));
            pagamentoPixRepository.save(pix);
        } else {
            Pagamento pag = new Pagamento();
            pag.setTipo(tipo.toUpperCase());
            pag.setValor(total);
            pag.setStatus("APROVADO");
            pag.setData(LocalDate.now());
            pag.setVenda(venda);
            pagamentoRepository.save(pag);
        }
    }
}
