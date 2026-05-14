package com.example.StorePDV.repository;

import com.example.StorePDV.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("SELECT v FROM Venda v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim")
    List<Venda> findVendasPorPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT SUM(v.valorTotal) v FROM Venda v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim")
    BigDecimal sumVendasPorPeriodo(@Param("dataInicio")  LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT COUNT(v) FROM Venda v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim")
    Long countVendasPorPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

}
