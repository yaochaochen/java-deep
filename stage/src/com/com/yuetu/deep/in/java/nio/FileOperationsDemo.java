package com.yuetu.deep.in.java.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.yuetu.deep.in.java.nio.PathDemo.USER_DIR_LOCATION;

public class FileOperationsDemo {

    public static void main(String[] args) throws Exception {

        displayFileExists();
        displayFileEqual();
        displayFileAccessibilit();
    }

    private static void displayFileAccessibilit() {
        Path path = Paths.get(USER_DIR_LOCATION);
        System.out.printf("${user.dir} : %s , readable = %s , writable = %s , executable : %s \n",
                path,
                Files.isReadable(path),//可读
                Files.isWritable(path),//可写
                Files.isExecutable(path)//可执行
        );
    }

    private static void displayFileEqual() throws IOException {
        Path path = Paths.get(USER_DIR_LOCATION);
        Path path1 = Paths.get(USER_DIR_LOCATION);
        //比较path
        System.out.println(Files.isSameFile(path, path1));
    }

    private static void displayFileExists() {

        Path path = Paths.get(USER_DIR_LOCATION);
        System.out.printf("${user.dir}: %s, exists = %s\n", path, Files.exists(path));
    }
}
