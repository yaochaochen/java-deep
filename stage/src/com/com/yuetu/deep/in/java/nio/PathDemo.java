package com.yuetu.deep.in.java.nio;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathDemo {

    public static final String USER_DIR_LOCATION = System.getProperty("user.dir");

    public static void main(String[] args) {
//        dispalyPathInfo();
        displayPathNormalize();
    }

    private static void displayPathNormalize() {
            Path path = Paths.get(USER_DIR_LOCATION);
            System.out.println(path.normalize());

    }

    private static void dispalyPathInfo() {

        Path path = Paths.get(USER_DIR_LOCATION);

        System.out.printf("toString: %s/n", path);

        File file = new File(USER_DIR_LOCATION);
        Path pathFromFile = file.toPath();
        Path pathFromURI = Paths.get(pathFromFile.toUri());

        System.out.println("pathFromURL :" + pathFromURI);
        System.out.println("pathFromLocation: " + path);
        System.out.println("pathFromFile: " + pathFromFile);
        System.out.println("pathFromURL == path ?" + pathFromURI.equals(path));
        System.out.println("pathFromFile == path ?" + pathFromFile.equals(path));

    }



}
