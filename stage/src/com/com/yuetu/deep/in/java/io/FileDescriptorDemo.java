package com.yuetu.deep.in.java.io;

import java.io.FileDescriptor;
import java.lang.reflect.Field;

public class FileDescriptorDemo {

    public <T> T dispalyFileDescriptor( FileDescriptor fileDescriptor) throws Exception {

        Field field = FileDescriptor.class.getDeclaredField("fd");
        field.setAccessible(true);
        //return field.
        return null;

    }

}
