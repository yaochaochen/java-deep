package com.yuetu.deep.in.java.apt;

import java.io.Serializable;

@Repository
public class UserRepository implements Comparable<UserRepository>, CrudRepository<User>, Serializable {

        @Override
        public int compareTo(UserRepository o) {
            return 0;
        }
    }