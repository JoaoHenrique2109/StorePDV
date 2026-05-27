package com.example.StorePDV.repository;

import com.example.StorePDV.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // ── SELECTS SIMPLES ──────────────────────────────────────────────

    // Simples 1: busca por nome
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // Simples 2: lista apenas produtos ativos
    List<Produto> findByAtivoTrue();

    // Simples 3: busca por faixa de preço
    List<Produto> findByPrecoBetween(double precoMin, double precoMax);

    // ── SELECTS COMPLEXOS ────────────────────────────────────────────

    // Complexo 1: produtos com estoque abaixo do mínimo (com JOIN em fornecedor)
    @Query("""
        SELECT p FROM Produto p
        LEFT JOIN FETCH p.fornecedor f
        WHERE p.quantidade <= p.estoqueMinimo
        AND p.ativo = true
        ORDER BY p.quantidade ASC
    """)
    List<Produto> findProdutosComEstoqueBaixo();

    // Complexo 2: produtos mais vendidos (top N por quantidade)
    @Query("""
        SELECT p, SUM(i.quantidade) AS totalVendido
        FROM ItemVenda i
        JOIN i.produto p
        JOIN i.venda v
        WHERE v.status = 'FINALIZADA'
        GROUP BY p.id
        ORDER BY SUM(i.quantidade) DESC
    """)
    List<Object[]> findProdutosMaisVendidos();

    // Complexo 3: produtos nunca vendidos
    @Query("""
        SELECT p FROM Produto p
        WHERE p.id NOT IN (
            SELECT DISTINCT i.produto.id FROM ItemVenda i
        )
        AND p.ativo = true
        ORDER BY p.nome ASC
    """)
    List<Produto> findProdutosNuncaVendidos();

    // Necessário pelo ProdutoService.buscarPorNome
    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Produto> findByNome(@Param("nome") String nome);
}
