package com.yuetu.deep.in.java.collection;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

public class DequeCollectionsDemo {
    public static void main(String[] args) {

        Deque<Map<String, Integer>> mapDeques = new LinkedBlockingDeque<>();
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        map.put("A", 1);
        map2.put("A", 2);
        mapDeques.addFirst(map);
        mapDeques.addLast(map2);
        mapDeques.peek();
        mapDeques.stream().forEach(System.out::println);

    }
}
