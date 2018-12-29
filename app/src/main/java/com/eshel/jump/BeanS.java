package com.eshel.jump;

import java.io.Serializable;

public class BeanS implements Serializable {
    public BeanS(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String name;
    public int age;

    @Override
    public String toString() {
        return "BeanS{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
