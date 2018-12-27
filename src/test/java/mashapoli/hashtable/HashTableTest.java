package mashapoli.hashtable;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HashTableTest {

    @Test
    public void putAndGetTest() {

        HashTable<String, String> hashTable = new HashTable<String, String>();
        hashTable.put("key", "value");
        assertEquals(1, hashTable.size());

        String value = hashTable.get("key");
        assertEquals("value", value);
        assertEquals(1, hashTable.size());
    }


    @Test
    public void collisionTest() {
        HashTable<String, String> hashTable = new HashTable<String, String>();
        hashTable.put("key1", "value1");
        hashTable.put("key17", "value17");
        assertEquals(2, hashTable.size());
        assertEquals("value1", hashTable.get("key1"));
        assertEquals("value17", hashTable.get("key17"));
    }


    @Test
    public void containsTest() {
        HashTable<String, String> hashTable = new HashTable<String, String>();
        hashTable.put("key1", "value1");
        hashTable.put("key2", "value2");
        assertEquals(2, hashTable.size());
        assertTrue(hashTable.containsValue("value1"));
        assertTrue(hashTable.containsKey("key2"));
        assertFalse(hashTable.containsKey("key"));
    }


    @Test
    public void putReturnsNullValueOnPut() {
        HashTable<String, String> hashTable = new HashTable<String, String>();
        String oldValue = hashTable.put("key", "value1");
        assertNull(oldValue);
        assertEquals(1, hashTable.size());
    }

    @Test
    public void putReturnsOldValueOnOverride() {
        HashTable<String, String> hashTable = new HashTable<String, String>();
        hashTable.put("key", "value1");
        String oldValue = hashTable.put("key", "value2");
        assertEquals("value1", oldValue);
        assertEquals(1, hashTable.size());
    }

    @Test
    public void removeTest() {
        HashTable<String, String> hashTable = new HashTable<String, String>();
        hashTable.put("key", "value");
        String value = hashTable.remove("key");
        assertEquals("value", value);
        value = hashTable.get("key");
        assertNull(value);
        assertTrue(hashTable.isEmpty());
    }

    @Test
    public void stressAddidngTest() {
        HashTable<String, String> hashTable = new HashTable();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 100000; i++) {
            String key = String.format("key %d", i);
            String value = String.format("value %d", i);
            hashTable.put(key, value);
            map.put(key, value);
        }
        assertEquals(map, hashTable);
    }

    @Test
    public void stressRemovingTest() {
        HashTable<String, String> hashTable = new HashTable();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 100000; i++) {
            String key = String.format("key %d", i);
            String value = String.format("value %d", i);
            hashTable.put(key, value);
            map.put(key, value);
        }
        for (int i = 0; i < 100000; i++) {
            String key = String.format("key %d", i);
            hashTable.remove(key);
            map.remove(key);
        }
        assertEquals(map, hashTable);
    }
}