package com.example.StorePDV.repository;

import com.example.StorePDV.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByVenda_Id(Long id);
}
