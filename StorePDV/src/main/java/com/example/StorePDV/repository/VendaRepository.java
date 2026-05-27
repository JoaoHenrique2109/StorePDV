package com.example.StorePDV.repository;

import com.example.StorePDV.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    // ── SELECTS SIMPLES ──────────────────────────────────────────────

    // Simples 1: vendas por status
    List<Venda> findByStatus(Venda.Status status);

    // Simples 2: vendas de um cliente específico
    List<Venda> findByCliente_Id(Long clienteId);

    // Simples 3: vendas de um operador específico
    List<Venda> findByUsuario_Id(Long usuarioId);

    // ── SELECTS COMPLEXOS ────────────────────────────────────────────

    // Complexo 1: vendas finalizadas num período com cliente e usuário carregados
    @Query("""
        SELECT v FROM Venda v
        LEFT JOIN FETCH v.cliente c
        LEFT JOIN FETCH v.usuario u
        WHERE v.dataVenda BETWEEN :inicio AND :fim
        AND v.status = 'FINALIZADA'
        ORDER BY v.dataVenda DESC
    """)
    List<Venda> findVendasPorPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim")    LocalDateTime fim
    );

    // Complexo 2: faturamento agrupado por dia no período
    @Query("""
        SELECT CAST(v.dataVenda AS date), COUNT(v), SUM(v.total)
        FROM Venda v
        WHERE v.dataVenda BETWEEN :inicio AND :fim
        AND v.status = 'FINALIZADA'
        GROUP BY CAST(v.dataVenda AS date)
        ORDER BY CAST(v.dataVenda AS date) ASC
    """)
    List<Object[]> findFaturamentoPorDia(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim")    LocalDateTime fim
    );

    // Complexo 3: vendas com seus itens e produtos carregados (evita N+1)
    @Query("""
        SELECT DISTINCT v FROM Venda v
        LEFT JOIN FETCH v.cliente
        LEFT JOIN FETCH v.usuario
        WHERE v.id = :id
    """)
    java.util.Optional<Venda> findByIdComDetalhes(@Param("id") Long id);
}
