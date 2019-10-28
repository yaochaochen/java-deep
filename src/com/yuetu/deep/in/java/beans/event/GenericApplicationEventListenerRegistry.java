package com.yuetu.deep.in.java.beans.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class GenericApplicationEventListenerRegistry implements ApplicationEventListenerRegistry{



    private Map<String, List<ApplicationEventListener<?>>> typedListeners
            = new LinkedHashMap<>();


    @Override
    public void addApplicationEventListener(ApplicationEventListener<?> listener) {
        List<ApplicationEventListener<?>> listeners = getListeners(listener);
        listeners.add(listener);

    }

    @Override
    public void removeApplicationEventListener(ApplicationEventListener<?> listener) {
        List<ApplicationEventListener<?>> listeners = getListeners(listener);
        listeners.remove(listener);
    }





    protected List<ApplicationEventListener<?>> getListeners(ApplicationEventListener<?> listener) {
        Class<?> listenerClass = listener.getClass();
        Type[] genericInterfaces = listenerClass.getGenericInterfaces();
        String eventTypeName = Stream.of(genericInterfaces).filter(t -> t instanceof ParameterizedType) //判断接口是否 ParameterizedType类型
        .map(t -> (ParameterizedType) t) //转换类型
                .filter(parameterizedType -> ApplicationEventListener.class.equals(parameterizedType.getRawType()))
                .map(parameterizedType -> {
                    //获取第一个泛型参数
                    return parameterizedType.getActualTypeArguments()[0].getTypeName();
                }).findFirst().orElse(null);
        return typedListeners.computeIfAbsent(eventTypeName, k-> new LinkedList<>());
    }
    @Override
    public ApplicationEventListener[] getApplicationEventListeners() {
        return new ApplicationEventListener[0];
    }

    @Override
    public ApplicationEventListener[] getApplicationEventListeners(Class<? extends ApplicationEvent> eventType) {
        String eventTypeName = eventType.getTypeName();
        return typedListeners.getOrDefault(eventTypeName, emptyList()).toArray(new ApplicationEventListener[0]);

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
