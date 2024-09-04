package br.com.itau.CasePix.exception;

public class PixKeyAlreadyExistsException extends RuntimeException {
    public PixKeyAlreadyExistsException(String message) {
        super(message);
    }
}
