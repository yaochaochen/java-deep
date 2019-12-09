package com.yuetu.deep.in.java.queue;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DIYQueueDemo {

    private final static  Queue<String> queue = new DIYQueue<>();

    static class Product implements Runnable {

        private final String message;

        public Product(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            boolean success = queue.put(message);
            if(success) {
                System.out.println("put success " + message);
                return;
            }
        }
    }
    static class Consumer implements Runnable {

        @Override
        public void run() {
            String message = queue.take();
            System.out.println(" consumer message " + message);
        }
    }

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10,10, 0 ,
                TimeUnit.MILLISECONDS , new LinkedBlockingQueue<>());
        for ( int i = 0; i<1000; i++) {
            if( i%2 == 0) {
                executor.submit(new Product(i +""));
                continue;
            }
            executor.submit( new Consumer());
         }
        executor.shutdown();
    }
}
