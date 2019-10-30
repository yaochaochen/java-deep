package com.yuetu.deep.in.java.process;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.time.Instant;

public class ProcessInfoDemo {
    public static void main(String[] args) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

        Instant instant = Instant.ofEpochMilli(runtimeMXBean.getStartTime());
        System.out.println("当前进程启动时间：" + instant.getEpochSecond());
        System.out.println("当前进程上线时间：" + runtimeMXBean.getUptime());
        System.out.println("当前进程线程数量：" + threadMXBean.getThreadCount());

        ManagementFactory.getMemoryManagerMXBeans().forEach(memoryManagerMXBean -> {

        });

        System.exit(9);

    }
}
