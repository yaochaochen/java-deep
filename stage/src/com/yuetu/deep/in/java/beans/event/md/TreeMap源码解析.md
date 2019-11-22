# 			TreeMap源码解析

## 前置知识点

​	

```java
@Data
static  class  DTO implements Comparable<DTO> {

    private Integer id;
    public DTO(Integer id) {
        this.id = id;
    }

    @Override
    public int compareTo(DTO o) {
        return id-o.getId();
    }
}

public static void main(String[] args) {
    // 第一种排序，从小到大排序，实现 Comparable 的 compareTo 方法进行排序
    List<DTO> list = new ArrayList<>();
    for (int i = 5; i > 0; i--) {
        list.add(new DTO(i));
    }
    Collections.sort(list);

    Comparator comparator = (Comparator<DTO>) (o1, o2) -> o2.getId() - o1.getId();
    List<DTO> list2 = new ArrayList<>();
    for (int i = 5; i > 0; i--) {
        list2.add(new DTO(i));
    }
    Collections.sort(list2,comparator);
}
```

上述代码是compare和Comparator的实现排序，对于TreeMap就是利用这个原理实现的。从而实现了对key的排序。

## TreeMap类结构

````java
 @see Map
 * @see HashMap
 * @see Hashtable
 * @see Comparable
 * @see Comparator
 * @see Collection
 * @since 1.2
````

```java
public class TreeMap<K,V>
    extends AbstractMap<K,V>
    implements NavigableMap<K,V>, Cloneable, java.io.Serializable
```

NavigableMap继承了SortedMap的接口说明它具有排序能力

## 整体架构

TreeMap底层的数据结构就是红黑树，恰是红黑树TreeMap利用了左节点小，右节点小的性质，进行key的排序 使得每个元素能够插入到红黑树大小的适合位置。

对于containsKey get put remove 等方法的时间复杂度是log(n)

## 常量

```java
/**
 * The comparator used to maintain order in this tree map, or
 * null if it uses the natural ordering of its keys.
 *
 * @serial
 */
//比较器
private final Comparator<? super K> comparator;
//红黑树的根节点
private transient Entry<K,V> root;

/**
 * The number of entries in the tree
 */
//已有元素大小
private transient int size = 0;

/**
 * The number of structural modifications to the tree.
 */
//变化版本，fast-fail
private transient int modCount = 0;

//红黑树的节点
static final class Entry<K,V> implements Map.Entry<K,V> {}
```

## 新增节点

### 步骤

1. 判断红黑树的节点是否为空，为空的话，新增的节点直接作为根节点，

   ```java
   Entry<K,V> t = root;
   //如果为空直接根节点
   if (t == null) {
       compare(key, key); // type (and possibly null) check
   
       root = new Entry<>(key, value, null);
       size = 1;
       modCount++;
       return null;
   }
   ```

2. 根据红黑树左小右大的特性，进行判断，找到应该新增节点的父节点

   ```java
   int cmp;
   Entry<K,V> parent;
   // split comparator and comparable paths
   Comparator<? super K> cpr = comparator;
   if (cpr != null) {
     //自旋找到key,然后新增到位置
       do {
         //比对parent上一次的比过对象
           parent = t;
         //通过key判断
           cmp = cpr.compare(key, t.key);
         //小于赋予左边
           if (cmp < 0)
               t = t.left;
         //大于赋予右边
           else if (cmp > 0)
               t = t.right;
           else
             //相等的话，覆盖原来的值
               return t.setValue(value);
       } while (t != null);
   }
   else {
       if (key == null)
           throw new NullPointerException();
       @SuppressWarnings("unchecked")
           Comparable<? super K> k = (Comparable<? super K>) key;
       do {
           parent = t;
           cmp = k.compareTo(t.key);
           if (cmp < 0)
               t = t.left;
           else if (cmp > 0)
               t = t.right;
           else
               return t.setValue(value);
       } while (t != null);
   }
   
   ```

3.在父节点新增

```java
//如果小于0代表e在上一节点的左边
if (cmp < 0)
    parent.left = e;
else
  //大于0代表e在上一节点的右边
    parent.right = e;
fixAfterInsertion(e);
size++;
modCount++;
return null;
```

4. 着色旋转，达到平衡

#### 源码总结

- 新增节点时，就是利用红黑树的左小右大 从根节点不断往下查找，直到找到节点为空为止，节点为空说明达到叶子结点
- 如果key已经存在，value值直接覆盖
- TreeMap是禁止key为null的