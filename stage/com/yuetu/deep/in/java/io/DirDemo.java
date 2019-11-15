package com.yuetu.deep.in.java.io;

import java.io.File;
import java.io.FileFilter;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DirDemo {

    private final File fileDir;
    private Predicate<File> filter;

    public DirDemo(File fileDir, FileFilter... filters) {
        this.fileDir = fileDir;
        this.filter = new FilePredicate(filters);
    }

    private class FilePredicate implements Predicate<File> {

        private final FileFilter[] filters;

        private FilePredicate(FileFilter[] filters) {
            this.filters = filters;
        }

        @Override
        public boolean test(File file) {
            for (FileFilter filter :filters) {
                if(!filter.accept(file)) {
                    return false;
                }
            }
            return true;
        }
    }

    public long getSpace() {
        FileFilter fileFilter = null;
        if (fileDir.isFile()) {
            return fileDir.length();
        } else if (fileDir.isDirectory()) {
            File[] subFiles = fileDir.listFiles();
            long space = 0L;
            // 累加当前目录的文件
            space += Stream.of(subFiles)
                    .filter(File::isFile)
                    .filter(filter)
                    .map(File::length)
                    .reduce(Long::sum)
                    .orElse(0L);

            // 递归当前子目录
            space += Stream.of(subFiles)
                    .filter(File::isDirectory)
                    .filter(filter)
                    .map(DirDemo::new)
                    .map(DirDemo::getSpace)
                    .reduce(Long::sum)
                    .orElse(0L);

            return space;
        }
        return -1L;
    }
    public static void main(String[] args) {
        System.out.println(new DirDemo(new File("/Users/yaochaochen/project")).getSpace() / 1024);
    }
}
