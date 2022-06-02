package mi.superCupcake.strategies;

import lombok.AllArgsConstructor;
import lombok.val;

import java.util.Comparator;
import java.util.Map;

@AllArgsConstructor
class ComparatorImpl<K> implements Comparator<K> {
    private final Map<K, Long> comparatorMap;

    @Override
    public int compare(K key1, K key2) {
        val key1Long = comparatorMap.get(key1);
        val key2Long = comparatorMap.get(key2);

        return key1Long.compareTo(key2Long);
    }
}
