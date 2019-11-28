# CopyOnWriteArrayList源码分析和设计思路

## 为什么会存在

在ArrayList中的注释写道，如果ArrayList作为共享变量，是线程不安全的，推荐使用Collections.synchronizedList方法，其实JDK提供了一种线程安全的List 叫做CopyOnWriteArrayList

## CopyOnWriteArrayList具有的特征

1. 线程安全，多线程直接使用无需加锁
2. 通过锁+数组copy+volatile关键字保证线程安全
3. 每次数组操作，都会copy一份出来，在新的数组中操作，操作成功后然后再赋值回去

## 整体结构

CopyOnWriteArrayList数据结构和ArrayList一致，底层是个数组，CopyOnWriteArrayList在多数组的操作分为四步

1. 加锁
2. 从原数组copy出来新的数组
3. 在新数组操作，并且新数组赋值给数组容器
4. 解锁

除了加锁之外，CopyOnWriteArrayList的底层数组还被volatile关键字修饰，意思是一但被修改，其他线程就能感应到

```java
 /** The array, accessed only via getArray/setArray. */
    private transient volatile Object[] array;
```

### 类注释

` @since 1.5`

```java
/**
 * A thread-safe variant of {@link java.util.ArrayList} in which all mutative
 * operations ({@code add}, {@code set}, and so on) are implemented by
 * making a fresh copy of the underlying array.
 *
 * <p>This is ordinarily too costly, but may be <em>more</em> efficient
 * than alternatives when traversal operations vastly outnumber
 * mutations, and is useful when you cannot or don't want to
 * synchronize traversals, yet need to preclude interference among
 * concurrent threads.  The "snapshot" style iterator method uses a
 * reference to the state of the array at the point that the iterator
 * was created. This array never changes during the lifetime of the
 * iterator, so interference is impossible and the iterator is
 * guaranteed not to throw {@code ConcurrentModificationException}.
 * The iterator will not reflect additions, removals, or changes to
 * the list since the iterator was created.  Element-changing
 * operations on iterators themselves ({@code remove}, {@code set}, and
 * {@code add}) are not supported. These methods throw
 * {@code UnsupportedOperationException}.
 *
 * <p>All elements are permitted, including {@code null}.
 *
 * <p>Memory consistency effects: As with other concurrent
 * collections, actions in a thread prior to placing an object into a
 * {@code CopyOnWriteArrayList}
 * <a href="package-summary.html#MemoryVisibility"><i>happen-before</i></a>
 * actions subsequent to the access or removal of that element from
 * the {@code CopyOnWriteArrayList} in another thread.
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 */
```

1. 所有操作都是线程安全的，因为都是在新的数组上操作的
2. 数组copy虽然有一定成本，单往往比一般的效率高
3. 迭代过程中，不会影响到原来的数组，也不会抛出错

### 新增

新增有很多情况，比如说新增到数组尾部，新增到数组某一个索引位置，批量新增等

```java
 /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
     */
    public boolean add(E e) {
        final ReentrantLock lock = this.lock;
      //加锁
        lock.lock();
        try {
          //得到原数组
            Object[] elements = getArray();
            int len = elements.length;
          //copy到新的数组 新数组的长度+1 因为新增会多一个元素
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            //在新数组中赋值，新元素直接放在数组尾部
          	newElements[len] = e;
          //替换掉原来的数组
            setArray(newElements);
            return true;
        } finally {
          //解锁
            lock.unlock();
        }
    }
```

add的过程中是一直持有锁的状态下进行的，通过加锁来保证一时刻只能有一个线程能够对一个数组进行add操作

除了加锁外，还有从原数组中copy一个新的数组中，但是想想已经加锁了，为啥还要copy数组在新的数组中操作呢？

1. volatile关键字修饰的是数组，如果简单的在原来数组上修改其中某个元素的值，是无法触发可见性的。就必须通过数组的内存地址才行。也就说对数组进行重新赋值才行
2. 在新的数组上进行copy，对老数组没有影响，只有新数组完全拷贝完成才行，外部访问 降低了在赋值过程中，老数组数据变动的影响

### 小结

加锁+copy数组+volatile保证线程安全

1. 加锁：保证每一时刻数组只能被一个线程操作
2. 数组copy:保证数组的内存地址被修改，修改后触发volatile的可见性，其他线程就立马知道数组已经被修改
3. volatile:值被修改后其他线程会被感应到最新值

三个条件缺一不可，缺少任何其中一个都无法保证其特性，如果去掉2 在修改数组某一个值时，不会触发volatile的可见性

### 删除

```java
/**
 * Removes the element at the specified position in this list.
 * Shifts any subsequent elements to the left (subtracts one from their
 * indices).  Returns the element that was removed from the list.
 *
 * @throws IndexOutOfBoundsException {@inheritDoc}
 */
public E remove(int index) {
    final ReentrantLock lock = this.lock;
  	//加锁
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
      //得到老值
        E oldValue = get(elements, index);
        int numMoved = len - index - 1;
      //是否尾部
        if (numMoved == 0)
          //直接删除
            setArray(Arrays.copyOf(elements, len - 1));
        else {
          //设置新数组长度-1
            Object[] newElements = new Object[len - 1];
          //从0copy到新数组位置
            System.arraycopy(elements, 0, newElements, 0, index);
          //从新位置拷贝的尾部
            System.arraycopy(elements, index + 1, newElements, index,
                             numMoved);
            setArray(newElements);
        }
        return oldValue;
    } finally {
        lock.unlock();
    }
}
```

步骤

1. 加锁
2. 判断索引位置 进行不同策略的拷贝
3. 解锁

add和remove方法可看到代码风格统一 锁+try finally +数组拷贝

### 迭代

在迭代过程中即使数组被修改也不抛异常，其根本原因就是数组改动，都会有新的数组，不会影响到老的数组，

从源代码上看

```java
/**
 * Returns an iterator over the elements in this list in proper sequence.
 *
 * <p>The returned iterator provides a snapshot of the state of the list
 * when the iterator was constructed. No synchronization is needed while
 * traversing the iterator. The iterator does <em>NOT</em> support the
 * {@code remove} method.
 *
 * @return an iterator over the elements in this list in proper sequence
 */
public Iterator<E> iterator() {
  //原数组
    return new COWIterator<E>(getArray(), 0);
}
```

```java
static final class COWIterator<E> implements ListIterator<E> {
    /** Snapshot of the array */
  //持有原数组的引用
    private final Object[] snapshot;
    /** Index of element to be returned by subsequent call to next.  */
    private int cursor;

    private COWIterator(Object[] elements, int initialCursor) {
        cursor = initialCursor;
        snapshot = elements;
    }
```

迭代过程中持有原数组的引用，也就说整个迭代过程即使原数组的原值内存底子发生改变，必然影响迭代过程

![20191128180824](/Users/yaochaochen/project/java-deep/stage/src/image/20191128180824.jpg)

![WX20191128-180904](/Users/yaochaochen/project/java-deep/stage/src/image/WX20191128-180904.png)

### 结论

无论什么操作CopyOnWriteArrayList都会对数组拷贝的

