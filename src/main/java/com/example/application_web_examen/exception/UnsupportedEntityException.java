package com.example.application_web_examen.exception;

public class UnsupportedEntityException extends IllegalArgumentException {
    public UnsupportedEntityException(String message) {
        super(message);
    }
}