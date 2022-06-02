package mi.superCupcake.strategies;

import java.util.Map;
import java.util.TreeMap;

public abstract class CacheStrategy<K> {
    private final Map<K, Long> objectsStorage;
    private final TreeMap<K, Long> sortedObjectsStorage;

    CacheStrategy() {
        this.objectsStorage = new TreeMap<>();
        this.sortedObjectsStorage = new TreeMap<>(new ComparatorImpl<>(objectsStorage));
    }

    public Map<K, Long> getObjectsStorage() {
        return this.objectsStorage;
    }

    public TreeMap<K, Long> getSortedObjectsStorage() {
        return this.sortedObjectsStorage;
    }

    public abstract void putObject(K key);

    public void removeObject(K key) {
        if (containsKey(key)) {
            objectsStorage.remove(key);
        }
    }

    public boolean containsKey(K key) {
        return objectsStorage.containsKey(key);
    }

    public K getReplacedKey() {
        sortedObjectsStorage.putAll(objectsStorage);
        return sortedObjectsStorage.firstKey();
    }

    public void clear() {
        objectsStorage.clear();
    }
}
