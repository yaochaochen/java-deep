package com.yuetu.deep.in.java.collection;

import java.util.Collections;
import java.util.List;

public class EmptyCollectionDemo {

    public static List<String> getIdsList(String name) {
        if (name == null || name.length() > 1) {
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }
}
