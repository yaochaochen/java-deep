package com.yuetu.deep.in.java.beans.event;

import java.util.EventObject;

public class ApplicationEvent extends EventObject {

    private final Long timetemp;

    public ApplicationEvent(Object source) {
        super(source);
        this.timetemp = System.currentTimeMillis();
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Long getTimetemp() {
        return timetemp;
    }
}
