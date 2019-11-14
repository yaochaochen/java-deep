package com.yuetu.deep.in.java.reflection;

import java.lang.reflect.Field;

public class StringReflectionDemo {

    /**
     * java5开始反射可以修改对象属性 即使被final修饰
     * 通过反射改变String value
     * @param args
     */
    public static void main(String[] args) throws Exception {

        String testContent = "Hello, Wold";

        String otherContent = "yaocc";
        System.out.println("反射修改前的 testContent: " + testContent);
        // private final char value[];
        Field valueField = String.class.getDeclaredField("value");
        //设置访问检查
        valueField.setAccessible(true);
        //替换
        valueField.set(testContent, otherContent.toCharArray());
        System.out.println("反射后的 testContent: " + testContent);

    }
}
