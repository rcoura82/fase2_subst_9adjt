package com.biblioteca.exception;

/**
 * Exceção lançada quando há validação de negócio
 */
public class ExcecaoNegocioException extends RuntimeException {
    
    public ExcecaoNegocioException(String mensagem) {
        super(mensagem);
    }

    public ExcecaoNegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
