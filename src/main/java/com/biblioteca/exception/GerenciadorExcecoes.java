package com.biblioteca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manipulador global de exceções da aplicação
 */
@ControllerAdvice
public class GerenciadorExcecoes {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex, WebRequest request) {
        
        Map<String, Object> resposta = criarRespostaErro(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request
        );
        
        return new ResponseEntity<>(resposta, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExcecaoNegocioException.class)
    public ResponseEntity<Map<String, Object>> tratarExcecaoNegocio(
            ExcecaoNegocioException ex, WebRequest request) {
        
        Map<String, Object> resposta = criarRespostaErro(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request
        );
        
        return new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarExcecaoGeral(
            Exception ex, WebRequest request) {
        
        Map<String, Object> resposta = criarRespostaErro(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno do servidor",
            request
        );
        
        return new ResponseEntity<>(resposta, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> criarRespostaErro(HttpStatus status, String mensagem, WebRequest request) {
        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", status.value());
        resposta.put("erro", status.getReasonPhrase());
        resposta.put("mensagem", mensagem);
        resposta.put("caminho", request.getDescription(false).replace("uri=", ""));
        return resposta;
    }
}
