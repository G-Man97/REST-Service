package com.gmalykhin.spring.rest.exception_handling;

public class ExceptionWrapper {

    private String info;

    public ExceptionWrapper(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
