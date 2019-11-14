package com.yuetu.deep.in.java.beans.customization;

import com.yuetu.deep.in.java.beans.properties.Person;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyEditor;
import java.util.Date;
import java.util.stream.Stream;

public class PersonBeanCustomization {

    public static void main(String[] args) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(Person.class, Object.class);
        Person person = new Person();
        Stream.of(beanInfo.getPropertyDescriptors())
                .filter(propertyDescriptor -> "id".equals(propertyDescriptor.getName())) // 过滤 "id" 属性
                .findFirst()
                .ifPresent(idPropertyDescriptor -> {
                            // 为 "id" 属性描述注册属性修改器
                            idPropertyDescriptor.setPropertyEditorClass(IdPropertyEditor.class);
                            PropertyEditor propertyEditor = idPropertyDescriptor.createPropertyEditor(person);
                            // 添加属性变化事件
                            propertyEditor.addPropertyChangeListener(event -> {
                                person.setId((Long) propertyEditor.getValue());
                            });
                            // 通过 setAsText(String) 方法模拟 <property name="id">1</property>
                            propertyEditor.setAsText("1");
                        }
                );
        Stream.of(beanInfo.getPropertyDescriptors())
                .filter(propertyDescriptor -> "updateTime".equals(propertyDescriptor.getName())) // 过滤 "updateTime" 属性
                .findFirst()
                .ifPresent(propertyDescriptor -> {
                            // 为 "updateTime" 属性描述注册属性修改器
                            propertyDescriptor.setPropertyEditorClass(com.yuetu.deep.in.java.beans.customization.UpdateTimePropertyEditor.class);
                            PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(person);
                            // 添加属性变化事件
                            propertyEditor.addPropertyChangeListener(event -> {
                                person.setUpdateTime((Date) propertyEditor.getValue());
                            });
                            // 通过 setAsText(String) 方法模拟 <property name="updateTime">2019-10-23 23:16:00</property>
                            propertyEditor.setAsText("2019-10-23 23:16:00");
                        }
                );
        System.out.println("当前 person = " + person);


    }

}
