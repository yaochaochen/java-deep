package com.yuetu.deep.in.java.apt.compiler;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

public class CompilerDemo {

    public static void main(String[] args) throws IOException {
        //获取JavaCompiler
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, null, null);
        //指定JavaSource code路径
        Class<?> targetClass = CompilerDemo.class;
        // 目标类的源文件（相对路径）：com/deep/in/java/compiler/CompilerDemo/java
        String sourceFileRelativePath = targetClass.getName().replace('.', '/').concat(".java");
        // 目标类的源文件（Maven 路径）：${user.dir}/src/main/java + 目标类的源文件（相对路径）
        String sourceFilePath = System.getProperty("user.dir") + "/src/" + sourceFileRelativePath;
        File sourceFile = new File(sourceFilePath);
        //指定Java 新生的class输出目录 Java-d
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(getClassOutDirectory()));
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
        JavaCompiler.CompilationTask compilationTask = javaCompiler.getTask(new OutputStreamWriter(System.out), fileManager, null, null, null, compilationUnits);
        // 执行编译
        compilationTask.call();

    }

    public static File getClassOutDirectory() {
        File currentClassPath = new File(findClassPath());
        File targetDirectory = currentClassPath.getParentFile();
        File classOutputDirectory = new File(targetDirectory, "new-classes");
        // 生成类输出目录
        classOutputDirectory.mkdir();
        return classOutputDirectory;

    }

    public static String findClassPath() {
        String classPath = System.getProperty("java.class.path");
        return Stream.of(classPath.split(File.pathSeparator))
                .map(File::new)
                .filter(File::isDirectory)
                .filter(File::canRead)
                .filter(File::canWrite)
                .map(File::getAbsolutePath)
                .findFirst()
                .orElse(null);
    }
}
