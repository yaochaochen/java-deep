package com.yuetu.deep.in.java.collection.list;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArrayListDemo {


    @Test
    public void testRemove() {
        List<String> list = new ArrayList<String>(){{
           add("2");
           add("3");
           add("3");
           add("3");
           add("4");
        }};
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals("3")) {
                list.remove(i);
            }
        }
    }
}
