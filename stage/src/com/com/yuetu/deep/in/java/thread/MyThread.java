package com.yuetu.deep.in.java.thread;

public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) {

        new MyThread().start();
    }


    public void  joinDemo() throws InterruptedException {
        Thread main = Thread.currentThread();
        System.out.printf("主线程{%d}", main.getName());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.printf("子线程{%d}", Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            }catch (Exception e) {

            }
            }

        });
        thread.start();
        thread.join();
    }
}
