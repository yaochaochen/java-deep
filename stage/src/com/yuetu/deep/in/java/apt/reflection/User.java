package com.yuetu.deep.in.java.apt.reflection;

import java.io.Serializable;

public class User implements Serializable {

        private String name;

        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }