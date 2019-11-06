package com.yuetu.deep.in.java.collection;

import java.util.Arrays;
import java.util.List;

public class ArrayListDemo {

    public static void main(String[] args) {
        /**
         *https://bugs.java.com/bugdatabase/view_bug.do?bug_id=626065
         */
        List<String> list = Arrays.asList("hello, world");
        Object[] objects = list.toArray();
        System.out.println(objects.getClass().getSimpleName());
        objects[0] = new Object();
    }

    class SortDTO {
        private String sortTarget;

        public SortDTO(String sortTarget) {
            this.sortTarget = sortTarget;
        }

    }
}
