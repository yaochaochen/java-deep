package com.yuetu.deep.in.java.apt.proccessor;


import com.yuetu.deep.in.java.apt.compiler.Compiler;

import java.io.File;
import java.io.IOException;

import static com.yuetu.deep.in.java.apt.compiler.CompilerDemo.getClassOutDirectory;

public class RepositoryAnnotationProcessorDemo {
    public static void main(String[] args) throws IOException {
        File sourceDirectory = new File(System.getProperty("user.dir"), "/src/");
        File targetDirectory = getClassOutDirectory();
        // 基于 Compiler
        Compiler compile = new Compiler(sourceDirectory, targetDirectory);
        compile.setProcessors(new RepositoryAnnotationProccessor());
        compile.compile(
                "com/yuetu/deep/in/java/apt/reflection/CrudRepository"

        );
    }

}
