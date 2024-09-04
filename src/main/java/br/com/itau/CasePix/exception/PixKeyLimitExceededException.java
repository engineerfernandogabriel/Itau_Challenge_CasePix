package br.com.itau.CasePix.exception;

public class PixKeyLimitExceededException extends RuntimeException {
    public PixKeyLimitExceededException(String message) {
        super(message);
    }
}
