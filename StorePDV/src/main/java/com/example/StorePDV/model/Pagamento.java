package com.example.StorePDV.model;


import jakarta.persistence.*;

import java.lang.annotation.Inherited;
import java.time.LocalDate;

@Entity
@Table(name = "pagamentos")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private double valor;
    private String status;
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "pagamento_id")
    private Venda venda;

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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }
}
