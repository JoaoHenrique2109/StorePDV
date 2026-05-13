package com.example.StorePDV.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "usuarios")
public class Usuario extends Pessoa implements Serializable {

    private String senha;
    private String cargo;
    private double salario;

}
