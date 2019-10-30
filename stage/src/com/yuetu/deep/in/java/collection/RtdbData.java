package com.yuetu.deep.in.java.collection;

public class RtdbData {

    private String name;

    private String values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public RtdbData(String name, String values) {
        this.name = name;
        this.values = values;
    }
}
