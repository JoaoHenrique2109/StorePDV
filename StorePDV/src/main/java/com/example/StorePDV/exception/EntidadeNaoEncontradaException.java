package com.example.StorePDV.exception;

public class EntidadeNaoEncontradaException extends StorePDVException {
    public EntidadeNaoEncontradaException(String entidade, String identificador) {
        super(entidade + " não encontrado(a): " + identificador);
    }

    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(entidade + " não encontrado(a) com ID: " + id);
    }
}