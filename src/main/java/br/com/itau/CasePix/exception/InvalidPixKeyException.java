package br.com.itau.CasePix.exception;

public class InvalidPixKeyException extends RuntimeException {
    public InvalidPixKeyException(String message) {
        super(message);
    }
}