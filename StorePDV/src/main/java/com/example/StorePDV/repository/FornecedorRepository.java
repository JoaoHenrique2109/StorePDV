package com.example.StorePDV.repository;

import com.example.StorePDV.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Optional<Fornecedor> findByCnpj(String cnpj);
    List<Fornecedor> findByNomeContainingIgnoreCase(String nome);
}
