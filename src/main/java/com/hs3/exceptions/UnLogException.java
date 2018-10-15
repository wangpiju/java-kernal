package com.hs3.exceptions;

public class UnLogException
        extends BaseCheckException {
    private static final long serialVersionUID = 1L;

    public UnLogException() {
    }

    public UnLogException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UnLogException(String message) {
        super(message);
    }

    public UnLogException(Throwable throwable) {
        super(throwable);
    }
}
