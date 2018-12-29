package com.eshel.jump;

public class Bean {
    public Bean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String name;
    public int age;

    @Override
    public String toString() {
        return "Bean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
