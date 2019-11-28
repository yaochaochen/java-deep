package com.yuetu.deep.in.java.collection.list;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class IterationDemo {



    public static void main(String[] args) {
        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add("1");
        copyOnWriteArrayList.add("2");
        copyOnWriteArrayList.add("3");
        Iterator<String> iterator = copyOnWriteArrayList.iterator();
        copyOnWriteArrayList.add("4");
        iterator.next();
        copyOnWriteArrayList.add("5");
    }
}
