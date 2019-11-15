package com.yuetu.deep.in.java.io;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class DirCommandDemo {

    private final File rootDirectory;

    public DirCommandDemo(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    ///Users/yaochaochen/project
    public void execute() {
        Stream.of(rootDirectory.listFiles()).map(file -> {
            StringBuilder messageBuilder = new StringBuilder();
            long lastModified = file.lastModified();
            String dir = file.isDirectory() ? "<dir>" : "";
            String length = file.isDirectory() ? " " : String.valueOf(file.length());
            String fileName = file.getName();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
            String dateTime = simpleDateFormat.format(new Date(lastModified));
            return messageBuilder.append(dateTime).append(" ").append(dir).append(" ").append(length).append(fileName).toString();
        }).forEach(System.out::println);

    }

    public static void main(String[] args) {
        new DirCommandDemo(new File(System.getProperty("user.home"))).execute();
    }
}

