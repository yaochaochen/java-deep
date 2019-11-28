package com.yuetu.deep.in.java.nio;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.yuetu.deep.in.java.nio.PathDemo.USER_DIR_LOCATION;

public class FileOperationsUsingByteChannelDemo {
    public static void main(String[] args) {
        Charset charset = Charset.forName("UTF-8");

        Path pomXmlPath = Paths.get(USER_DIR_LOCATION, "pom.xml");
        Path pomCopyXmlPath = Paths.get(USER_DIR_LOCATION, "pom-copy.xml");
        try (SeekableByteChannel sourceByteChannel = Files.newByteChannel(pomXmlPath);
             SeekableByteChannel targetByteChannel  = Files.newByteChannel(pomCopyXmlPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        ) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
            while (sourceByteChannel.read(byteBuffer)>0) {
                byteBuffer.rewind();
                targetByteChannel.write(byteBuffer);
                byteBuffer.flip();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
