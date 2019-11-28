package com.yuetu.deep.in.java.beans.event;


import java.util.EventListener;

public interface ApplicationEventListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 事件监听
     * @param event 事件
     */
    void onEvent(E event);

}
