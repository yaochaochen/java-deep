package com.yuetu.deep.in.java.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferDemo {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put( (byte) 1);
        dispalyBufferMetadata(buffer);

    }
    private static void dispalyBufferMetadata(Buffer buffer) {
        System.out.printf("当前buffer = [ %s ] [type:%s] position= %s limit = %s capacity = %s",
                buffer.getClass().getName(),
                buffer.getClass().getSimpleName(),
                buffer.position(),
                buffer.limit(),
                buffer.capacity()

                );
    }
}
