package com.example.StorePDV.repository;

import com.example.StorePDV.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    List<ItemVenda> findByVenda_Id(Long vendaId);

    @Query("SELECT i FROM ItemVenda i JOIN FETCH i.produto WHERE i.venda.id = :vendaId")
    List<ItemVenda> findByVendaIdComProduto(@Param("vendaId") Long vendaId);
}
