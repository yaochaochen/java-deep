package com.yuetu.deep.in.java.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class FutureTaskDemo {


    @Test
    public void initCallable() throws ExecutionException, InterruptedException {

        FutureTask futureTask = new FutureTask<>(new Callable<String>() {


            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                return "Hello World";
            }
        });
        futureTask.run();
        String result = (String) futureTask.get();
        log.info("取值 {}", result);
    }

    @Test
    public void initRunnable() throws ExecutionException, InterruptedException {

        FutureTask futureTask = new FutureTask<String>(new Runnable() {
            @Override
            public void run() {
                log.info("{} is run ", Thread.currentThread().getName());
            }
        }, null);
        futureTask.run();
        String result = (String) futureTask.get();
        log.info(" 拿到值了 {}", result);
    }

}
