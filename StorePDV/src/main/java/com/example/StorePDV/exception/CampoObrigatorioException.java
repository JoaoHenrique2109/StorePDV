package com.example.StorePDV.exception;

public class CampoObrigatorioException extends RuntimeException {
    public CampoObrigatorioException(String campo) {
        super("Campo obrigatório não informado: " + campo);
    }
}