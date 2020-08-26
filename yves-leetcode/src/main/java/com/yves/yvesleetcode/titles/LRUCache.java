package com.yves.yvesleetcode.titles;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 运用你所掌握的数据结构，设计和实现一个  LRU (最近最少使用) 缓存机制。它应该支持以下操作： 获取数据 get 和 写入数据 put 。
 * <p>
 * 获取数据 get(key) - 如果关键字 (key) 存在于缓存中，则获取关键字的值（总是正数），否则返回 -1。
 * 写入数据 put(key, value) - 如果关键字已经存在，则变更其数据值；如果关键字不存在，则插入该组「关键字/值」。当缓存容量达到上限时，它应该在写入新数据之前删除最久未使用的数据值，从而为新的数据值留出空间。
 * <p>
 *  
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/lru-cache
 */
public class LRUCache extends LinkedHashMap<Integer, Integer> {
    final int cacheSize;

    /**
     * 思路：用链表存储数据,经常使用的放在表头,当容量不足时,删掉表头的节点
     *
     * @param capacity
     */
    public LRUCache(int capacity) {
        super(capacity, 0.75F, true);
        this.cacheSize = capacity;
    }


    public int get(int key) {
        return super.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        super.put(key, value);
    }

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() > this.cacheSize;
    }
}
