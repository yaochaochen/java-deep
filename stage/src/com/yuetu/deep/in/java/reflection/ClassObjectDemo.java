package com.yuetu.deep.in.java.reflection;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.AbstractList;

public class ClassObjectDemo {

    public static void main(String[] args) {
        //具体类对象
        System.out.println(!Modifier.isAbstract(Object.class.getModifiers()));
        //抽象类
        System.out.println(Modifier.isAbstract(AbstractList.class.getModifiers()));
        //接口类
        System.out.println(Serializable.class.isInterface());
        //注解类
        System.out.println(ConstructorProperties.class.isAnnotation());
        //枚举类
        System.out.println(Color.class.isEnum());
        //原生类
        System.out.println(int.class.isPrimitive());
        //数组类
        System.out.println(int[].class.isArray());
        //void
        System.out.println(void.class);


    }

}
