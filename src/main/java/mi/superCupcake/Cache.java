package mi.superCupcake;

public interface Cache<K, V> {

    void put(K key, V value);

    V get(K key);

    boolean containsKey(K key);

    void remove(K key);

    void clear();

    int size();
}