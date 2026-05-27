package com.example.StorePDV.exception;

public class LoginInvalidoException extends StorePDVException {
    public LoginInvalidoException() {
        super("Login ou senha inválidos.");
    }

    public LoginInvalidoException(String mensagem) {
        super(mensagem);
    }
}