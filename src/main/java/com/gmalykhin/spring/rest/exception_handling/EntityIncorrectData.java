package com.gmalykhin.spring.rest.exception_handling;

public class EntityIncorrectData {

    private String info;

    public EntityIncorrectData() {
    }

    public EntityIncorrectData(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
