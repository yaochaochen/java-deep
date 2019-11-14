package com.yuetu.deep.in.java.io;

import java.io.File;
import java.util.stream.Stream;

public class DirDemo {

    private final File fileDir;

    public DirDemo(File fileDir) {
        this.fileDir = fileDir;
    }

    public long getSpace() {
        long space = 0L;
        if (fileDir.isFile()) {
            return fileDir.length();
        } else if (fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            space += Stream.of(files).filter(File::isDirectory).map(File::length).reduce(Long::sum).orElse(0L);
            return space += Stream.of(files).filter(File::isFile).map(File::length).reduce(Long::sum).orElse(0L);
        }
        return 0L;
    }
}
