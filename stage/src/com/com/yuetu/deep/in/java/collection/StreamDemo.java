package com.yuetu.deep.in.java.collection;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamDemo {

    public static void main(String[] args) {

        List<RtdbData> lists = new ArrayList<>();
        lists.add(new RtdbData("A","1"));
        lists.add(new RtdbData("B", "1"));
        lists.add(new RtdbData("A" , "0"));
        lists.add(new RtdbData("B", "0"));
        Map<String, List<RtdbData>> map = lists.stream().collect(Collectors.groupingBy(RtdbData::getValues));
        System.out.println(map.size());


    }

}
