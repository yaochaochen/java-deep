package com.yuetu.deep.in.java.queue;


/**
 * 定义出队列的接口
 */
public interface Queue<T> {

    /**
     * 新增
     * @param item 参数
     * @return 是否
     */
    boolean put(T item);

    /**
     * 取数据
     * @return 数据
     */
    T take();

    class Node<T> {
        //数据本身
        T item;

        //下一个元素
        Node<T> next;

        public Node(T item) {
            this.item = item;
        }
    }

}
