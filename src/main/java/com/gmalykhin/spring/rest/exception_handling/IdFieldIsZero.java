package com.gmalykhin.spring.rest.exception_handling;

import com.gmalykhin.spring.rest.entity.BaseEntity;

public class IdFieldIsZero extends IncorrectFieldData {

    public <T extends BaseEntity> IdFieldIsZero (T entity) {
        this(entity.getClass().getSimpleName().toLowerCase());
    }
    private IdFieldIsZero(String className) {
        super("To edit the " + className + " you need write your " + className + " id. The id can not be 0");
    }
}
