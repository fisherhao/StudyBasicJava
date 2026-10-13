package com.hao.yu.test.ha;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月19日 星期六 17:18
 */
public class MapTest {

    public static void main(String[] args) throws InterruptedException {
//
//        System.out.println("sdjiogjsog");
//        testLinkedHashMap();

        System.out.println("开始换行了把英德港");
        treeMap();
    }

    private static void treeMap() {

        Map<Character, Integer> treeMap = new TreeMap<>();
        String key = "keykeyfjsojoege";
        char[] charArray = key.toCharArray();
        for (char c : charArray) {
            treeMap.put(c, treeMap.getOrDefault(c, 0) + 1);
        }
        Set<Map.Entry<Character, Integer>> entries = treeMap.entrySet();

        for (Map.Entry<Character, Integer> entry : entries) {
            System.out.println(entry.getKey() + "--" + entry.getValue());
        }
    }

    private static void testLinkedHashMap() {
        LruCache<Integer, String> lruCache = new LruCache<>();
        lruCache.put(1, "one");
        lruCache.put(2, "two");
        lruCache.put(3, "three");
        lruCache.put(4, "four");
        lruCache.put(5, "five");
        System.out.println("开始的吧");
        System.out.println(lruCache.size());
        System.out.println(lruCache.get(1));
        System.out.println(lruCache.get(5));
        System.out.println(lruCache.get(4));
        System.out.println(lruCache.size());
        Set<Map.Entry<Integer, String>> entries = lruCache.entrySet();
        for (Map.Entry<Integer, String> entry : entries) {
            System.out.println(entry.getKey());
        }
    }
}

class LruCache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = -6160394433258631323L;

    public LruCache() {
        super(3, 0.75f, true);
    }

    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > 3;
    }
}
