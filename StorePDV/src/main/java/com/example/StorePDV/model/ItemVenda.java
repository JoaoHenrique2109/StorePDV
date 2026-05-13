package com.example.StorePDV.model;
import jakarta.persistence.*;

@Entity
@Table(name = "itens_venda")


public class ItemVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Venda venda;
    @ManyToOne
    private Produto produto;

    private int quantidade;
    private double precoUnitario;
}
