package com.yuetu.deep.in.java.collection.list;

public class Student {

    private String name;

    private String no;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Student(String name, String no) {
        this.name = name;
        this.no = no;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", no='" + no + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
