package com.example.StorePDV.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "clientes")
public class Cliente extends Pessoa implements Serializable {
    public enum NivelFidelidade {
        BRONZE, PRATA, OURO, DIAMANTE
    }
    private NivelFidelidade nivelFidelidade = NivelFidelidade.BRONZE;

    public NivelFidelidade getNivelFidelidade() {
        return nivelFidelidade;
    }

    public void setNivelFidelidade(NivelFidelidade nivelFidelidade) {
        this.nivelFidelidade = nivelFidelidade;
    }
}
