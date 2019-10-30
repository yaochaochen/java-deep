package com.yuetu.deep.in.java.apt.compiler;

import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class Compiler {

    private final File sourceDirectory;

    private final File targetDirectory;

    private final JavaCompiler javaCompiler;

    private final StandardJavaFileManager fileManager;

    private Iterable<? extends Processor> processors;

    public Compiler(File sourceDirectory, File targetDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.javaCompiler = ToolProvider.getSystemJavaCompiler();
        this.fileManager = javaCompiler.getStandardFileManager(null, null, null);
    }

    public void setProcessors(Processor... processors) {
        this.processors = asList(processors);
    }

    public void compile(String... classNames) throws IOException {
        //指定 Java 新生产的Class目录, 输出目录 javac -d
        this.fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(targetDirectory));
        List<File> sourceFiles = Stream.of(classNames)
                .map(name -> name.replace(',', File.separatorChar).concat(".java")) //目录类的源文件
                .map(name -> sourceDirectory.getAbsolutePath() + File.separatorChar + name) //目录类的路径
                .map(File::new) //将路径加载成文件
                .collect(Collectors.toList());
        Iterable<? extends JavaFileObject> compilercationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFiles);
        JavaCompiler.CompilationTask compilationTask = javaCompiler.getTask(new OutputStreamWriter(System.out), fileManager,
                null, null
                , null, compilercationUnits);
        //设置Processor
        compilationTask.setProcessors(processors);
        //执行编译
        compilationTask.call();

    }
}
