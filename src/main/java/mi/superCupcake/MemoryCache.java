package mi.superCupcake;

import java.util.HashMap;
import java.util.Map;

public class MemoryCache<K, V> implements Cache<K, V> {
    private final Map<K, V> objectsStorage;
    private final int capacity;

    MemoryCache(int capacity) {
        this.capacity = capacity;
        this.objectsStorage = new HashMap<K, V>(capacity);
    }

    @Override
    public void put(K key, V value) {
        objectsStorage.put(key, value);
    }

    @Override
    public V get(K key) {
        return objectsStorage.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return objectsStorage.containsKey(key);
    }

    @Override
    public void remove(K key) {
        objectsStorage.remove(key);
    }

    @Override
    public void clear() {
        objectsStorage.clear();
    }

    @Override
    public int size() {
        return objectsStorage.size();
    }

    public boolean hasEmptyPlace() {
        return size() < this.capacity;
    }
}
