package com.yuetu.deep.in.java.beans.event;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * {@link ApplicationEvent} 事件注册中心
 */
public class SimpleApplicationEventListenerRegistry implements ApplicationEventListenerRegistry {

    private Set<ApplicationEventListener<?>> listeners = new TreeSet<>(new ApplicationEventListenerComparator());

    @Override
    public void addApplicationEventListener(ApplicationEventListener<?> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeApplicationEventListener(ApplicationEventListener<?> listener) {
        listeners.remove(listener);
    }

    @Override
    public ApplicationEventListener[] getApplicationEventListeners() {
        return listeners.toArray(new ApplicationEventListener[0]);
    }

    @Override
    public ApplicationEventListener[] getApplicationEventListeners(Class<? extends ApplicationEvent> eventType) {
        return new ApplicationEventListener[0];
    }

    /**
     * 实现类去重
     */
    static class ApplicationEventListenerComparator implements Comparator<ApplicationEventListener> {
        @Override
        public int compare(ApplicationEventListener o1, ApplicationEventListener o2) {
            String oneClassName = o1.getClass().getName();
            String anotherClassName = o2.getClass().getName();
            return oneClassName.compareTo(anotherClassName);
        }
    }
}
