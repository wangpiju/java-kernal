package com.hs3.exceptions;

public class BaseDaoException
        extends BaseException {
    public BaseDaoException() {
    }

    public BaseDaoException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BaseDaoException(String message) {
        super(message);
    }

    public BaseDaoException(Throwable throwable) {
        super(throwable);
    }
}
