package com.yuetu.deep.in.java.collection.maps;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapAccessOrder {

    public static void main(String[] args) {
        testAccessOrder();
    }
    public static void testAccessOrder() {
        // 新建 LinkedHashMap
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>(4,0.75f,true) {
            {
                put(10, 10);
                put(9, 9);
                put(20, 20);
                put(1, 1);
            }

            @Override
            // 覆写了删除策略的方法，我们设定当节点个数大于 3 时，就开始删除头节点
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return size() > 3;
            }
        };
    }
}
