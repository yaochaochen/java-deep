package com.yuetu.deep.in.java.sort;

public interface Sort<T extends  Comparable<T>> {

    void sort(T[] values);

    static <T> T[] of(T... values){
        return values;
    }
}
