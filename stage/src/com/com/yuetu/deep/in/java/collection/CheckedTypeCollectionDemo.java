package com.yuetu.deep.in.java.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CheckedTypeCollectionDemo {

    public static void main(String[] args) {
        List<Integer>  values = new ArrayList<>(Arrays.asList(1, 2, 3));
        //values.add("A");//编译错误
        List refencedValues = values;
        System.out.println(values == refencedValues);
        refencedValues.add("A");//编译通过 泛型编译时检查，运行时擦写
        for (Object value : values) {
            System.out.println(value);
        }
        // 接口是弥补泛型集合在运行时擦写中的不足
        List<Integer> checkedTypeValues = Collections.checkedList(values, Integer.class);
        //checkedTypeValues.add("A");//编译时错
        refencedValues = checkedTypeValues;
        System.out.println(refencedValues == values);
        System.out.println(refencedValues == checkedTypeValues);
        refencedValues.add("B");

    }
}
