package com.yuetu.deep.in.java.beans.event;

public class MyEventListener implements ApplicationEventListener<MyEvent> {
    @Override
    public void onEvent(MyEvent event) {
        System.out.println("MyEventListener1 处理事件" + event);
    }
}
