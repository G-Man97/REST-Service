package com.gmalykhin.spring.rest.exception_handling;

public class IncorrectFieldData extends RuntimeException{

    public IncorrectFieldData(String message) {
        super(message);
    }
}
