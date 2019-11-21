

# LinkedList的源码解析

### `整体架构`

` @since 1.2`

LinkedList的底层数据结构是一个双向链表

![](/Users/yaochaochen/Desktop/5d5fc67a0001f59212400288.jpeg)

- 链表的每个节点叫做Node，Node有prev属性，代表前一个节点位置，next属性代表后一个节点的位置
- fist是双向链表的头节点，它的前一个节点是null
- last是双向链表的尾节点，它的后一个节点是null
- 当链表没有数据时，fist和last是同一个节点前后都会指向null
- LinkedList是双向链表不限制大小

#### 链表中元素

​	链表中的元素其实是Node元素 其中Node组成：

```java
private static class Node<E> {
    E item;//节点值
    Node<E> next;//指向下一个节点
    Node<E> prev;//指向上一个节点
		//按顺序初始化
    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```

### LinkedList类的继承和实现

```Java
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
{}
```

特别注意的一点实现了Deque的接口，这是一个双向队列的接口 `@since1.6`

### 源码解析

#### 追加

追加节点时，我们可以选择追加到链表头部还是追加到链表尾部 add默认是从尾部开始追加的 addFist是从头部追加的

```java
/**
 * Links e as last element.
 */
void linkLast(E e) {
  //把尾部节点暂时存起来，开辟了一个新内存空间
    final Node<E> l = last;
  //初始化新节点
  // l是新节点的前一个节点，当前尾部节点值 e 表示当前新增节点 
    final Node<E> newNode = new Node<>(l, e, null);
  //新建节点赋值到暂存的节点的位置
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;
    size++;//重置大小
    modCount++;//重置版本
}
```

尾部追加从源码可以看出来指向位置修改而已 从而可以得到LinkedList的删除和新增速度很快

#### 头部追加（addFist）

```java
private void linkFirst(E e) {
  //头部Node临时变量
    final Node<E> f = first;
    final Node<E> newNode = new Node<>(null, e, f);
    first = newNode;
    if (f == null)
      //头部为空就是链表为空，头尾部是一个节点
      //将上一个节点指向当前节点
        last = newNode;
    else
        f.prev = newNode;
    size++;
    modCount++;
}
```

头部追加当前者移动到prev指向，后者移动尾部的next指向

#### 删除节点

删除节点和追加类似也可以从头部删除也可以从尾部删除，删除节点值，前后指向的节点都会置null 帮助GC回收

- 从尾部删除

```java
 /**
     * Unlinks non-null first node f.
     */
    private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;
        final E element = f.item;//当前节点
        final Node<E> next = f.next;//当前节点的下一个节点
        f.item = null;
        f.next = null; // help GC
        first = next;
      //如果next为空代表整个链表都是为null
        if (next == null)
            last = null;
        else
          //当前的前一个节点置null
            next.prev = null;
        size--;//重置size-1
        modCount++;
        return element;
    }
```

#### 节点查询

```java
/**
 * Returns the (non-null) Node at the specified element index.
 */
Node<E> node(int index) {
    // assert isElementIndex(index);
	//如果查询的值在整个链表的前半部分从头开始查找
    if (index < (size >> 1)) {
        Node<E> x = first;
      //循环到index的当前一个node
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {//如果在后半部分 从尾部开始
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}
```

我很好奇LinkedList并没有从头循环到尾部的做法，而是采用一个二分法，先看看index是否在链表的前半部分还是后半部分，这样实现会使循环次数下降一半的复杂度

#### 对Deque实现的理解

在LinkedList实现双向链表访问。对Deque接口对新增删除查找都存在头部尾部的2中方式，但是链表为空会抛异常的

#### 迭代器

LinkedList是双向链表单纯一个Iterator的接口肯定不行的，因为只能支持从头到尾的访问，所以Java新增一个Litlterator,这个接口提供了向前向后的迭代方法

- 尾部到头部的迭代-`hasPrevious` 、` previous` `previousIndex`
- 从头部到尾部迭代- `hasNext` `next` `nextIndex`

```java
private class ListItr implements ListIterator<E> {
    private Node<E> lastReturned;//上次执行next或者previos的当前节点位置
    private Node<E> next;//下一个节点
    private int nextIndex;//下一个节点索引
    private int expectedModCount = modCount;

    ListItr(int index) {
        // assert isPositionIndex(index);
        next = (index == size) ? null : node(index);
        nextIndex = index;
    }
```

```java
//判断是否存在下一节点
public boolean hasNext() {
    return nextIndex < size;如果下一个节点索引小于整个链表大小就说明有
}
```

```java
public E next() {
  //检查版本号是否变化
    checkForComodification();
    if (!hasNext())
        throw new NoSuchElementException();
		//next是当前节点，在上一次执行next()的方法被赋值
    lastReturned = next;
  //当前下一个节点，为下一次迭代准备吧
    next = next.next;
    nextIndex++;
    return lastReturned.item;
}
```

```java
//如果上一次节点索引大于0就有节点可以迭代
public boolean hasPrevious() {
    return nextIndex > 0;
}

public E previous() {
    checkForComodification();
    if (!hasPrevious())
        throw new NoSuchElementException();
	//next为空场景：1.说明是第一次迭代，取尾部节点last 2上次操作把尾部节点删除
  //next不为空 说明已经迭代 直接取前一个节点即可
    lastReturned = next = (next == null) ? last : next.prev;
    nextIndex--;//重置索引位置
    return lastReturned.item;
}
```

