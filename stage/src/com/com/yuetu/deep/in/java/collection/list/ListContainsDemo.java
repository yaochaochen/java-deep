package com.yuetu.deep.in.java.collection.list;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListContainsDemo {


    public static void main(String[] args) {

        List<Student> list = Lists.newArrayList();
        list.add(new Student("Alice", "1"));// A库存在的Alice
        list.add(new Student("Ahan", "2"));

        list.add(new Student("Merch", "3"));

        List<Student> lista = Lists.newArrayList();
        lista.add(new Student("Alice", "1"));//B库存在Alice
        lista.add(new Student("Blalace", "4"));
        lista.add(new Student("test", "5"));
        //差集
        List<Student> students = list.stream().filter(student -> {
            for (Student stu : lista) {
                if (student.getName().equals(stu.getName())) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        //输出 Student{name='Alice', no='1'} A B 库都存在Alice
        students.parallelStream().forEach(System.out::println);
    }
}
