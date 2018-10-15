package com.hs3.exceptions;

public class BaseException
        extends RuntimeException {
    public BaseException() {
    }

    public BaseException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable throwable) {
        super(throwable);
    }
}
