package com.yves.spring.example;

import com.yves.spring.spring.context.annotation.Component;

@Component(name = "cbean01")
public class CBean {

    private String name;

    public CBean() {
    }

    public CBean(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
