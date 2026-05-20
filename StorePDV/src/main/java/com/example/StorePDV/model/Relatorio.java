package com.example.StorePDV.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "realatorios")
public class Relatorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private LocalDateTime dataGeracao;
    private LocalDate periodoInicio;
    private LocalDate periodoFim;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public LocalDate getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(LocalDate periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public LocalDate getPeriodoFim() {
        return periodoFim;
    }

    public void setPeriodoFim(LocalDate periodoFim) {
        this.periodoFim = periodoFim;
    }
}
