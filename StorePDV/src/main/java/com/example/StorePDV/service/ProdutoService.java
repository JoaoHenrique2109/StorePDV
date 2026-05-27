package com.example.StorePDV.service;

import com.example.StorePDV.exception.CampoObrigatorioException;
import com.example.StorePDV.exception.EntidadeNaoEncontradaException;
import com.example.StorePDV.exception.EstoqueInsuficienteException;
import com.example.StorePDV.exception.OperacaoNaoPermitidaException;
import com.example.StorePDV.model.Produto;
import com.example.StorePDV.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto cadastrar(Produto produto) {
        validar(produto);
        return produtoRepository.save(produto);
    }

    public Produto alterar(Long id, Produto dados) {
        Produto produto = buscarPorId(id);
        if (dados.getNome() != null && !dados.getNome().isBlank())
            produto.setNome(dados.getNome());
        if (dados.getDescricao() != null) produto.setDescricao(dados.getDescricao());
        if (dados.getPreco() > 0) produto.setPreco(dados.getPreco());
        if (dados.getQuantidade() >= 0) produto.setQuantidade(dados.getQuantidade());
        if (dados.getEstoqueMinimo() >= 0) produto.setEstoqueMinimo(dados.getEstoqueMinimo());
        if (dados.getPrecoCusto() > 0) produto.setPrecoCusto(dados.getPrecoCusto());
        return produtoRepository.save(produto);
    }

    public void remover(Long id) {
        Produto produto = buscarPorId(id);
        try {
            produtoRepository.delete(produto);
        } catch (Exception e) {
            throw new OperacaoNaoPermitidaException(
                    "Não é possível remover o produto '" + produto.getNome() + "': vinculado a vendas existentes.");
        }
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", id));
    }

    public List<Produto> buscarPorNome(String nome) {
        List<Produto> resultado = produtoRepository.findByNomeContainingIgnoreCase(nome);
        if (resultado.isEmpty())
            throw new EntidadeNaoEncontradaException("Produto", nome);
        return resultado;
    }

    public List<Produto> listarEstoqueBaixo() {
        return produtoRepository.findProdutosComEstoqueBaixo();
    }

    public void verificarEstoque(Produto produto, int quantidadeSolicitada) {
        if (produto.getQuantidade() < quantidadeSolicitada)
            throw new EstoqueInsuficienteException(produto.getNome(), produto.getQuantidade(), quantidadeSolicitada);
    }

    public void abaterEstoque(Produto produto, int quantidade) {
        verificarEstoque(produto, quantidade);
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        produtoRepository.save(produto);
    }

    public void repor(Produto produto, int quantidade) {
        produto.setQuantidade(produto.getQuantidade() + quantidade);
        produtoRepository.save(produto);
    }

    private void validar(Produto p) {
        if (p == null)
            throw new CampoObrigatorioException("produto");
        if (p.getNome() == null || p.getNome().isBlank())
            throw new CampoObrigatorioException("nome");
        if (p.getPreco() <= 0)
            throw new CampoObrigatorioException("preço (deve ser maior que zero)");
    }
}