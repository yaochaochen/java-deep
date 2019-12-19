package com.yuetu.deep.in.java.collection.list;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListContainsDemo {


    public static void main(String[] args) {

        List<Student> list = Lists.newArrayList();
        list.add(new Student("Alice", "1"));
        list.add(new Student("Ahan", "2"));

        list.add(new Student("Merch", "3"));

        List<Student> lista = Lists.newArrayList();
        lista.add(new Student("Alice", "1"));
        lista.add(new Student("Blalace", "4"));
        lista.add(new Student("test", "5"));
        //差集
      List<Student> students = list.stream().filter(new Predicate<Student>() {
          @Override
          public boolean test(Student student) {
             for (Student stu : lista) {
                 if(student.getName().equals(stu.getName())){
                     return true;
                 }
             }
             return false;
          }
      }).collect(Collectors.toList());

        students.parallelStream().forEach(System.out::println);
    }
}
