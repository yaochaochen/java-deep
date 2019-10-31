//package com.yuetu.deep.in.java.bytecode;
//
//import java.beans.PropertyVetoException;
//import java.util.concurrent.ExecutionException;
//
//import static com.yuetu.deep.in.java.beans.properties.Person.isNumeric;
//import static java.lang.String.format;
//import static java.lang.String.valueOf;
//
//public class PersonDemo {
//
//    public static void main(String[] args) throws PropertyVetoException, ExecutionException, InterruptedException {
//
//        Person person = new Person();
//
//        // 添加 PropertyChangeListener
//        person.addPropertyChangeListener(event -> {
//            // 属性变化通知事件
//            System.out.printf("监听到属性[%s] 内容变化（事件来源：%s），老值：%s，新值：%s\n",
//                    event.getPropertyName(),
//                    event.getSource(),
//                    event.getOldValue(),
//                    event.getNewValue()
//            );
//        });
//
//        // 添加 PropertyVetoListener
//        person.addVetoableChangeListener(event -> {
//            String newValue = valueOf(event.getNewValue());
//            if (isNumeric(newValue)) {
//                throw new PropertyVetoException(
//                        format("当前属性[%s]的新值[%s]不合法，不能为纯数字!", event.getPropertyName(), newValue), event);
//            }
//        });
//
//        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
//            e.printStackTrace();
//        });
//
//        // 修改名称 null -> "yaocc"
//        person.setName("yaocc");
//        System.out.println("当前 person.name = " + person.getName());
//
//        // 修改名称 "yaocc" -> "Hello World"
//        person.setName("Hello World");
//        System.out.println("当前 person.name = " + person.getName());
//
//        // 修改名称为 "Hello World" -> "12345"
//        person.setName("12345");
//        System.out.println("当前 person.name = " + person.getName());
//    }
//}
