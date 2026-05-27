package com.example.StorePDV.repository;

import com.example.StorePDV.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // ── SELECTS SIMPLES ──────────────────────────────────────────────

    // Simples 1: busca exata por CPF
    Optional<Cliente> findByCpf(String cpf);

    // Simples 2: busca por nome (contendo, ignorando maiúsculas)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    // Simples 3: busca por email
    Optional<Cliente> findByEmail(String email);

    // ── SELECTS COMPLEXOS ────────────────────────────────────────────

    // Complexo 1: clientes que já realizaram pelo menos uma venda finalizada
    // com JOIN entre Cliente e Venda
    @Query("""
        SELECT DISTINCT c FROM Cliente c
        JOIN Venda v ON v.cliente = c
        WHERE v.status = 'FINALIZADA'
        ORDER BY c.nome ASC
    """)
    List<Cliente> findClientesComVendas();

    // Complexo 2: ranking de clientes pelo total gasto
    // retorna [nome, qtdVendas, totalGasto]
    @Query("""
        SELECT c.nome, COUNT(v), SUM(v.total)
        FROM Venda v
        JOIN v.cliente c
        WHERE v.status = 'FINALIZADA'
        GROUP BY c.id, c.nome
        ORDER BY SUM(v.total) DESC
    """)
    List<Object[]> findRankingClientes();

    // Complexo 3: clientes sem compras num período
    @Query("""
        SELECT c FROM Cliente c
        WHERE c.id NOT IN (
            SELECT DISTINCT v.cliente.id FROM Venda v
            WHERE v.dataVenda BETWEEN :inicio AND :fim
            AND v.cliente IS NOT NULL
        )
        ORDER BY c.nome ASC
    """)
    List<Cliente> findClientesSemComprasNoPeriodo(
            @Param("inicio") java.time.LocalDateTime inicio,
            @Param("fim")    java.time.LocalDateTime fim
    );
}
