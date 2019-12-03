# HashMap源码分析

## 类结构

- ``@since1.2``

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable
```

- 继承了AbstractMap的抽象类

- 实现了Map接口

  ​	大概意思是实现Map接口，提供了HashMap的基础操作，允许null key和value 

  ```
  Hash table based implementation of the <tt>Map</tt> interface.  This
  * implementation provides all of the optional map operations, and permits
  * <tt>null</tt> values and the <tt>null</tt> key.
  ```

## 整体架构

HashMap的底层数据结构主要组成：数组+链表+红黑树，其中链表的长度大于等于8时链表会转化成红黑树（Java8）

当红黑树的大小小于6时，红黑树会转化成链表，如图

![](/Users/yaochaochen/Desktop/5d5fc7cc0001ec3211040928.jpeg)

## 类注释解释

在类注释大概意思

- 允许null值，是线程不安全的
- load-factor(影响因子) 默认是0.75是均衡时间和空间损耗计算出来的值，较高的值会减少空间开销，但是增加了查找的成本
- 如果很多有很多数据需要存储时，建议HashMap的容量一开始就设置成足够大，这样可以防止在期过程中不断扩容，影响性能
- HashMap是非线程安全的，但是我们可以加入外部锁或者Collections#synchronizedMap来实现线程安全，但是是轻量锁的实现
- 在迭代过程中，如果HashMap的结构被修改，会快速失败。

## 常量

```java
/**
 * The default initial capacity - MUST be a power of two.
 */
//初始容量是16
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

/**
 * The maximum capacity, used if a higher value is implicitly specified
 * by either of the constructors with arguments.
 * MUST be a power of two <= 1<<30.
 */
//最大容量
static final int MAXIMUM_CAPACITY = 1 << 30;

/**
 * The load factor used when none specified in constructor.
 */
//默认负载因子
static final float DEFAULT_LOAD_FACTOR = 0.75f;

/**
 * The bin count threshold for using a tree rather than list for a
 * bin.  Bins are converted to trees when adding an element to a
 * bin with at least this many nodes. The value must be greater
 * than 2 and should be at least 8 to mesh with assumptions in
 * tree removal about conversion back to plain bins upon
 * shrinkage.
 */
//桶上链表长度大于8时，链表转换红黑树
static final int TREEIFY_THRESHOLD = 8;

/**
 * The bin count threshold for untreeifying a (split) bin during a
 * resize operation. Should be less than TREEIFY_THRESHOLD, and at
 * most 6 to mesh with shrinkage detection under removal.
 */
//红黑树长度小于6 红黑树会转换链表
static final int UNTREEIFY_THRESHOLD = 6;

/**
 * The smallest table capacity for which bins may be treeified.
 * (Otherwise the table is resized if too many nodes in a bin.)
 * Should be at least 4 * TREEIFY_THRESHOLD to avoid conflicts
 * between resizing and treeification thresholds.
 */
//数组容量大于64链表装换成红黑树
static final int MIN_TREEIFY_CAPACITY = 64;

 /**
     * The table, initialized on first use, and resized as
     * necessary. When allocated, length is always a power of two.
     * (We also tolerate length zero in some operations to allow
     * bootstrapping mechanics that are currently not needed.)
     */
	//存放链表数据
    transient Node<K,V>[] table;
//Map的长度
  transient int size;
```

## 新增

新增Key,Value步骤：

1. 空数组有无初始化，没有的话初始化
2. 如果通过key的Hash能够找到值，跳转到6，否则到3
3. 如果Hash冲突，解决方法链表or红黑树
4. 如果链表，递归循环，把元素追加队尾
5. 如果红黑树，调用红黑树新增方法
6. 通过2、4、5将元素追加成功，再根据onlyAbsent判断是否覆盖
7. 判断是否需要扩容，需要进行扩容 结束

![](/Users/yaochaochen/Desktop/5d5fc7e200016af809121188.jpg)

### putVal源码

```java
/**
 * Implements Map.put and related methods
 *
 * @param hash hash for key Hash是根据key计算的
 * @param key the key
 * @param value the value to put
 * @param onlyIfAbsent if true, don't change existing value false表示即使key已经存在了，仍然会用新值覆盖原来的值，默认false
 * @param evict if false, the table is in creation mode.
 * @return previous value, or null if none
 */
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
  //n表示数组的长度，i为数组索引下标，p为i下标的位置的Node值
    Node<K,V>[] tab; Node<K,V> p; int n, i;
  //如果数组为空null，会调用resize方法初始化整个table
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
  //当前索引位置为空，直接生成新的节点在当前索引位置上
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
  //如果当前索引位置有值的处理方法，解决Hash冲突
    else {
      //当前节点的临时变量
        Node<K,V> e; K k;
      //如果key的Hash和值都相等，直接把当前下标位置的Node赋值临时变量
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
      //如果红黑树，使用红黑树的方式新增
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
          //链表新增，放在链表尾部
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    // e = p.next 表示从头开始，遍历链表
                  // p.next == null 表明 p 是链表的尾节点
                    p.next = newNode(hash, key, value, null);
                  // 当链表的长度大于等于 8 时，链表转红黑树
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
               // 链表遍历过程中，发现有元素和新增的元素相等，结束循环
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
          // 当 onlyIfAbsent 为 false 时，才会覆盖值 
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
  //如果 HashMap 的实际大小大于扩容的门槛，开始扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

### 链表新增

链表的新增比较简单，就把当前节点追加到链表的尾部，和LinkedList的尾部追加同样的实现

当链表长度大于8时，此时的链表就会转成红黑树，装化方法是treeifyBin,此方法是一个判断，当链表长度大于等于8时，并且整个数组大小大于64时，才会转成红黑树，当数组大小小于64只触发扩容机制，不会转成红黑树的

````

````

```
/*
 * Implementation notes.
 *
 * This map usually acts as a binned (bucketed) hash table, but
 * when bins get too large, they are transformed into bins of
 * TreeNodes, each structured similarly to those in
 * java.util.TreeMap. Most methods try to use normal bins, but
 * relay to TreeNode methods when applicable (simply by checking
 * instanceof a node).  Bins of TreeNodes may be traversed and
 * used like any others, but additionally support faster lookup
 * when overpopulated. However, since the vast majority of bins in
 * normal use are not overpopulated, checking for existence of
 * tree bins may be delayed in the course of table methods.
 *
 * Tree bins (i.e., bins whose elements are all TreeNodes) are
 * ordered primarily by hashCode, but in the case of ties, if two
 * elements are of the same "class C implements Comparable<C>",
 * type then their compareTo method is used for ordering. (We
 * conservatively check generic types via reflection to validate
 * this -- see method comparableClassFor).  The added complexity
 * of tree bins is worthwhile in providing worst-case O(log n)
 * operations when keys either have distinct hashes or are
 * orderable, Thus, performance degrades gracefully under
 * accidental or malicious usages in which hashCode() methods
 * return values that are poorly distributed, as well as those in
 * which many keys share a hashCode, so long as they are also
 * Comparable. (If neither of these apply, we may waste about a
 * factor of two in time and space compared to taking no
 * precautions. But the only known cases stem from poor user
 * programming practices that are already so slow that this makes
 * little difference.)
 *
 * Because TreeNodes are about twice the size of regular nodes, we
 * use them only when bins contain enough nodes to warrant use
 * (see TREEIFY_THRESHOLD). And when they become too small (due to
 * removal or resizing) they are converted back to plain bins.  In
 * usages with well-distributed user hashCodes, tree bins are
 * rarely used.  Ideally, under random hashCodes, the frequency of
 * nodes in bins follows a Poisson distribution
 * (http://en.wikipedia.org/wiki/Poisson_distribution) with a
 * parameter of about 0.5 on average for the default resizing
 * threshold of 0.75, although with a large variance because of
 * resizing granularity. Ignoring variance, the expected
 * occurrences of list size k are (exp(-0.5) * pow(0.5, k) /
 * factorial(k)). The first values are:
 *
 * 0:    0.60653066
 * 1:    0.30326533
 * 2:    0.07581633
 * 3:    0.01263606
 * 4:    0.00157952
 * 5:    0.00015795
 * 6:    0.00001316
 * 7:    0.00000094
 * 8:    0.00000006
 * more: less than 1 in ten million
 *
 * The root of a tree bin is normally its first node.  However,
 * sometimes (currently only upon Iterator.remove), the root might
 * be elsewhere, but can be recovered following parent links
 * (method TreeNode.root()).
 *
 * All applicable internal methods accept a hash code as an
 * argument (as normally supplied from a public method), allowing
 * them to call each other without recomputing user hashCodes.
 * Most internal methods also accept a "tab" argument, that is
 * normally the current table, but may be a new or old one when
 * resizing or converting.
 *
 * When bin lists are treeified, split, or untreeified, we keep
 * them in the same relative access/traversal order (i.e., field
 * Node.next) to better preserve locality, and to slightly
 * simplify handling of splits and traversals that invoke
 * iterator.remove. When using comparators on insertion, to keep a
 * total ordering (or as close as is required here) across
 * rebalancings, we compare classes and identityHashCodes as
 * tie-breakers.
 *
 * The use and transitions among plain vs tree modes is
 * complicated by the existence of subclass LinkedHashMap. See
 * below for hook methods defined to be invoked upon insertion,
 * removal and access that allow LinkedHashMap internals to
 * otherwise remain independent of these mechanics. (This also
 * requires that a map instance be passed to some utility methods
 * that may create new nodes.)
 *
 * The concurrent-programming-like SSA-based coding style helps
 * avoid aliasing errors amid all of the twisty pointer operations.
 */
```

## 红黑树新增

1. 首先判断新增的节点在红黑树是不是已经存在，判断如下
   1.   如果节点没有实现Comparable接口，使用equals进行判断。
   2. 如果节点自己实现了Comparable接口，使用compareTo进行判

2. 新增的节点如果已经在红黑树上，直接返回；不在话，判断新增节点是在当前节点的左边还是右边 左值小，右值大

3.  自旋递归1和2步，直到当前节点在左边还是右边的节点为空时，停止自旋，当前节点即为我们新增的节点的父节点
4. 把新增节点放到当前节点的左边或者右边为空的地方，并与当前节点建立父子节点关系
5. 进行着色和旋转

```java
/**
 * Tree version of putVal.
 */
final TreeNode<K,V> putTreeVal(HashMap<K,V> map, Node<K,V>[] tab,
                               int h, K k, V v) {
    Class<?> kc = null;
    boolean searched = false;
    //找到根节点
    TreeNode<K,V> root = (parent != null) ? root() : this;
   //自旋
    for (TreeNode<K,V> p = root;;) {
        int dir, ph; K pk;
      // p hash 值大于 h，说明 p 在 h 的右边
        if ((ph = p.hash) > h)
            dir = -1;
      // p hash 值小于 h，说明 p 在 h 的左边
        else if (ph < h)
            dir = 1;
      //自己实现的Comparable的话，不能用hashcode比较了，需要用compareTo
        else if ((pk = p.key) == k || (k != null && k.equals(pk)))
            return p;
         
        else if ((kc == null &&
                   //得到key的Class类型，如果key没有实现Comparable就是null
                  (kc = comparableClassFor(k)) == null) ||
                  //当前节点pk和入参k不等
                 (dir = compareComparables(kc, k, pk)) == 0) {
            if (!searched) {
                TreeNode<K,V> q, ch;
                searched = true;
                if (((ch = p.left) != null &&
                     (q = ch.find(h, k, kc)) != null) ||
                    ((ch = p.right) != null &&
                     (q = ch.find(h, k, kc)) != null))
                    return q;
            }
            dir = tieBreakOrder(k, pk);
        }

        TreeNode<K,V> xp = p;
      //找到和当前hashcode值相近的节点(当前节点的左右子节点其中一个为空即可)
        if ((p = (dir <= 0) ? p.left : p.right) == null) {
            Node<K,V> xpn = xp.next;
                //生成新的节点
            TreeNode<K,V> x = map.newTreeNode(h, k, v, xpn);
             //把新节点放在当前子节点为空的位置上
            if (dir <= 0)
                xp.left = x;
            else
                xp.right = x;
            xp.next = x;
            x.parent = x.prev = xp;
            if (xpn != null)
            //着色：新节点总是为红色；如果新节点的父亲是黑色，则不需要重新着色；如果父亲是红色，那么必须
						//通过重新着色或者旋转的方法，再次达到红黑树的5个约束条件
            //旋转： 父亲是红色，叔叔是黑色时，进行旋转
            //如果当前节点是父亲的右节点，则进行左旋
            //如果当前节点是父亲的左节点，则进行右旋
                ((TreeNode<K,V>)xpn).prev = x;
            moveRootToFront(tab, balanceInsertion(root, x));
            return null;
        }
    }
}
```

满足红黑树的原则

- 节点是红色或者是黑色
- 根是黑色
- 所有叶子都是黑色
- 从任一节点到其每个叶子的所有简单路径都包含相同数目的黑色节点
- 从每个叶子到根所有路径不能有2个连续的红色节点

### 红黑树图解

![](/Users/yaochaochen/Desktop/1355319681_6107.png)

[[https://zh.wikipedia.org/wiki/%E7%BA%A2%E9%BB%91%E6%A0%91](https://zh.wikipedia.org/wiki/红黑树)](    //着色：新节点总是为红色；如果新节点的父亲是黑色，则不需要重新着色；如果父亲是红色，那么必须通过重新着色或者旋转的方法，再次达到红黑树的5个约束条件
            //旋转： 父亲是红色，叔叔是黑色时，进行旋转
            //如果当前节点是父亲的右节点，则进行左旋
            //如果当前节点是父亲的左节点，则进行右旋)

