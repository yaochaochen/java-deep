package com.yuetu.deep.in.java.queue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class DIYQueue<T> implements Queue<T> {


    //头部队列
    private volatile Node<T> head;

    //队尾
    private volatile Node<T> tail;

    public DIYQueue() {
        capacity = Integer.MAX_VALUE;
        head = tail = new DIYNode(null);
    }

    /**
     * 有参构造
     * @param capacity 容量大小
     */
    public DIYQueue(Integer capacity) {
        if(null == capacity || capacity < 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = Integer.MAX_VALUE;
        head = tail = new DIYNode(null);
    }

    /**
     * 自定义队列元素
     */
    class DIYNode extends Node<T> {
        public DIYNode(T item) {
            super(item);
        }
    }
    //队列大小
    private AtomicInteger size = new AtomicInteger();
    //容量
    private final  Integer capacity;

    //放数据锁
    private ReentrantLock putLock = new ReentrantLock();

    //拿数据锁
    private ReentrantLock takeLock = new ReentrantLock();



    @Override
    public boolean put(T item) {

        if(null == item) {
            return false;
        }
        try {
            //加锁
            boolean lockSuccess = putLock.tryLock(300, TimeUnit.MILLISECONDS);
            if(!lockSuccess) {
                return false;
            }
            //校验队列
            if(size.get() >= capacity) {
                return false;
            }
            //追加到尾部
            tail = tail.next = new DIYNode(item);
            size.incrementAndGet();
            return true;

        }catch ( InterruptedException e){
            return false;
        }finally {
            putLock.unlock();
        }
    }

    @Override
    public T take() {
        if (size.get() == 0) {
            return null;
        }
        try {
            boolean lockSuccess = takeLock.tryLock(200, TimeUnit.MILLISECONDS);
            if (!lockSuccess) {
                throw new RuntimeException("加锁失败");
            }
            Node expectHead = head.next;
            T result = head.item;
            //置空防止GC
            head.item = null;
            head = expectHead;
            size.decrementAndGet();
            return result;
        }catch ( InterruptedException e) {

        }finally {
             takeLock.unlock();
        }
        return null;
    }
}
