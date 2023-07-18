package com.gmalykhin.spring.rest.exception_handling;

public class NoSuchEntityException extends RuntimeException{
    public NoSuchEntityException(String message) {
        super(message);
    }
    public NoSuchEntityException(int id) {
        super("There is no such raw with ID = " + id + " in Database");
    }
}