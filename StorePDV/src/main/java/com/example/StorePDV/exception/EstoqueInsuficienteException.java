package com.example.StorePDV.exception;

public class EstoqueInsuficienteException extends StorePDVException {
    public EstoqueInsuficienteException(String nomeProduto, int disponivel, int solicitado) {
        super("Estoque insuficiente para '" + nomeProduto + "'. "
                + "Disponível: " + disponivel + " | Solicitado: " + solicitado);
    }
}