package com.example.StorePDV.repository;

import com.example.StorePDV.model.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {
    List<Relatorio> findByTipo(String tipo);
    List<Relatorio> findByPeriodoInicioAndPeriodoFim(LocalDate periodoInicio, LocalDate periodoFim);

}
