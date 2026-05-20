package com.example.StorePDV.repository;

import com.example.StorePDV.model.PagamentoPix;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface PagamentoPixRepository extends JpaRepository<PagamentoPix, Long> {
    Optional<PagamentoPix> findByChavePix(String chavePix);
}
