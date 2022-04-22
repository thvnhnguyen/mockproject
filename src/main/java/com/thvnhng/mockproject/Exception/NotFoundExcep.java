package com.thvnhng.mockproject.Exception;

public class NotFoundExcep extends RuntimeException{
    public NotFoundExcep(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
