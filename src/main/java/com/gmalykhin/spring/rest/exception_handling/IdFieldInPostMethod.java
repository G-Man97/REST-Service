package com.gmalykhin.spring.rest.exception_handling;

public class IdFieldInPostMethod extends IncorrectFieldData {

    public IdFieldInPostMethod() { super("No need to write the id field for the POST method");}
}
