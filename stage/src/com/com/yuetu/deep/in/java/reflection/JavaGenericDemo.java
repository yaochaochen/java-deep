package com.yuetu.deep.in.java.reflection;

import java.io.File;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGenericDemo {

    public static void main(String[] args) throws ClassNotFoundException {
        // 模仿 Spring 类扫描
        // 通过标准方式 - Java 反射
        // 通过 ASM -

        Class<?> scanBasePackageClass = JavaGenericDemo.class;
        String scanBasePackage = scanBasePackageClass.getPackage().getName();
        // 类所在的 class path 物理路径
        String classPath = getClassPath(scanBasePackageClass);
        File classPathDirectory = new File(classPath);
        File scanBasePackageDirectory = new File(classPathDirectory, scanBasePackage.replace('.', '/'));
        // 获取所有的 Class 文件 -> *.class
        File[] classFiles = scanBasePackageDirectory.listFiles(file -> {
            return file.isFile() && file.getName().endsWith(".class");
        });
        System.out.println("class path : " + classPath);
        System.out.println("scan base package : " + scanBasePackage);
        System.out.println("scan class files :" + Stream.of(classFiles).map(File::getName).collect(Collectors.joining(",")));

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        List<Class<?>> targetClasses = new LinkedList<>();

        for (File classFile : classFiles) {
            String simepClassName = classFile.getName().substring(0, classFile.getName().lastIndexOf("."));
            String className = scanBasePackage + "." + simepClassName;
            //加载类
            Class<?> loadedClass = classLoader.loadClass(className);
            if (loadedClass.isAnnotationPresent(Repository.class)) {
                targetClasses.add(loadedClass);
            }
        }

        targetClasses.stream().filter(JavaGenericDemo::isConcrete) //筛选具体类
        .filter(JavaGenericDemo::isCrudRepositoryType) //筛选isCrudRepositoryType实现类
        .forEach(type -> {
            Type[] superInterfaces = type.getGenericInterfaces();
            Stream.of(superInterfaces).filter(t -> t instanceof ParameterizedType) //判断是否ParameterizedType类型
            .map(t -> (ParameterizedType)t).filter(parameterizedType -> CrudRepository.class.equals(parameterizedType.getRawType()))
                    .forEach(parameterizedType -> {
                        //获取第一个泛型参数
                        String argumentTypeName = parameterizedType.getActualTypeArguments()[0].getTypeName();
                        try {
                            System.out.println("泛型参数:" + classLoader.loadClass(argumentTypeName));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
        });

    }


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Repository {

        String value() default "";
    }


    class User implements Serializable {

        private String name;

        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Repository
    class UserRepository implements Comparable<UserRepository>, CrudRepository<User>, Serializable {

        @Override
        public int compareTo(UserRepository o) {
            return 0;
        }
    }


    interface CrudRepository<E extends Serializable> {

    }

    private static boolean isCrudRepositoryType(Class<?> type) {
        return  CrudRepository.class.isAssignableFrom(type);
    }

    private static String getClassPath(Class<?> type) {
        return type.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    private static boolean isConcrete(Class<?> type) {
        return !Modifier.isAbstract(type.getModifiers());
    }

}
