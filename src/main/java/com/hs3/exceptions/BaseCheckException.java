package com.hs3.exceptions;

public class BaseCheckException
        extends BaseException {
    public BaseCheckException() {
    }

    public BaseCheckException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BaseCheckException(String message) {
        super(message);
    }

    public BaseCheckException(Throwable throwable) {
        super(throwable);
    }
}
