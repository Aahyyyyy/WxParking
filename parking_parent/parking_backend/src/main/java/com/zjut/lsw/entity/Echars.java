package com.zjut.lsw.entity;

public class Echars {
    private String name;
    private Integer num;

    public Echars(String name, Integer num) {
        super();
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public Integer getNum() {
        return num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
