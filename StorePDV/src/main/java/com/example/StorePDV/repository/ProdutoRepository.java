package com.example.StorePDV.repository;

import com.example.StorePDV.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT p from Produto p WHERE p.quantidade <= p.estoqueMinimo")
    List<Produto> findProdutosComEstoqueBaixo();
}
