package com.example.StorePDV.repository;

import com.example.StorePDV.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    List<ItemVenda> findByVenda_Id(Long vendaId);
}
