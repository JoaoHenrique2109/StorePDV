package com.example.StorePDV.repository;

import com.example.StorePDV.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProduto_Id(Long id);
}
