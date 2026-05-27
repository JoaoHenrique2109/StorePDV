package com.example.StorePDV.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleNaoEncontrada(EntidadeNaoEncontradaException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleEstoque(EstoqueInsuficienteException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(LoginInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleLogin(LoginInvalidoException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<Map<String, Object>> handleOperacao(OperacaoNaoPermitidaException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CampoObrigatorioException.class)
    public ResponseEntity<Map<String, Object>> handleCampo(CampoObrigatorioException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(StorePDVException.class)
    public ResponseEntity<Map<String, Object>> handleGenerico(StorePDVException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleErroInterno(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor: " + ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String mensagem) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("erro", status.getReasonPhrase());
        body.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(body);
    }
}