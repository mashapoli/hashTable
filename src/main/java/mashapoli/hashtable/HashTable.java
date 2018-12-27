package mashapoli.hashtable;

import java.math.BigInteger;
import java.util.*;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

public class HashTable<K, V> implements Map<K, V> {

    private final int DEFAULT_SIZE = 17;
    private final int RESIZE_FACTOR = 2;
    private final float LOAD_FACTOR = 0.8f;
    private final SimpleEntry<K, V> TOMBSTONE = new SimpleEntry<>(null, null);

    private int size;
    private int occupied;
    private int capacity; // prime number
    private SimpleEntry<K, V> data[];

    public HashTable() {
        init(DEFAULT_SIZE);
    }

    private void init(int capacity) {
        this.capacity = capacity;
        data = new SimpleEntry[this.capacity];
        this.size = 0;
        this.occupied = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return keySet().contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }


    @Override
    public V get(Object key) {
        int index = search((K) key);
        return data[index] != null ? data[index].getValue() : null;
    }

    @Override
    public V put(K key, V value) {
        if(key == null) {
            throw new IllegalArgumentException("Null key isn't supported");
        }
        if( occupied > capacity * LOAD_FACTOR) {
            resize();
        }
        int index = search(key);
        V oldValue = null;
        if (data[index] != null) {
            oldValue = data[index].getValue();
        } else {
            occupied++;
            size++;
        }
        data[index] = new SimpleEntry<>(key, value);
        return oldValue;
    }


    @Override
    public V remove(Object key) {
        V oldValue;
        int index = search((K) key);
        if (data[index] == null) {
            return null;
        } else {
            oldValue = data[index].getValue();
            data[index] = TOMBSTONE;
            size--;
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        init(DEFAULT_SIZE);
    }

    @Override
    public Set<K> keySet() {
        return entrySet().stream().map(Entry::getKey).collect(toCollection(LinkedHashSet::new));
    }

    @Override
    public Collection<V> values() {
        return entrySet().stream().map(Entry::getValue).collect(toCollection(ArrayList::new));
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set set = new LinkedHashSet(size);
        for (SimpleEntry<K, V> entry : data) {
            if (!(entry == null || Objects.equals(entry, TOMBSTONE))) {
                set.add(entry);
            }
        }
        return set;
    }

    private int hash1(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private int hash2(K key) {
        return Math.abs(5 - key.hashCode() % 5);
    }

    private int prob(K key, int x) {
        return x * hash2(key);
    }

    private int search(K key) {
        int x = 1;
        int index = hash1(key);
        while (!(data[index] == null || Objects.equals(data[index].getKey(), key))) {
            index = (hash1(key) + prob(key, x)) % capacity;
            x += 1;
        }
        //         data[index] is null, or data[index] is Entry(key,value)
        return index;
    }

    private void resize() {
        Map<K, V> map = entrySet().stream().collect(toMap(Entry::getKey, Entry::getValue));
        int newCapacity = BigInteger.valueOf(map.size() * RESIZE_FACTOR).nextProbablePrime().intValue();
        init(newCapacity);
        putAll(map);
    }
}
