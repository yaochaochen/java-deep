package com.yuetu.deep.in.java.collection.maps;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TreeMapDemo {


    @Data
    static  class  DTO implements Comparable<DTO> {

        private Integer id;
        public DTO(Integer id) {
            this.id = id;
        }

        @Override
        public int compareTo(DTO o) {
            return id-o.getId();
        }
    }

    public static void main(String[] args) {
        // 第一种排序，从小到大排序，实现 Comparable 的 compareTo 方法进行排序
        List<DTO> list = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            list.add(new DTO(i));
        }
        Collections.sort(list);

        Comparator comparator = (Comparator<DTO>) (o1, o2) -> o2.getId() - o1.getId();
        List<DTO> list2 = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            list2.add(new DTO(i));
        }
        Collections.sort(list,comparator);
    }
}
