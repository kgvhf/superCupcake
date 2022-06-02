package mi.superCupcake;

import lombok.Getter;
import lombok.val;
import mi.superCupcake.strategies.CacheStrategy;
import mi.superCupcake.strategies.LFUStrategy;
import mi.superCupcake.strategies.StrategyType;

import java.io.Serializable;

import static java.lang.String.format;

@Getter
public class TwoLevelCache<K, V extends Serializable> implements Cache<K, V> {
    private final MemoryCache<K, V> firstLevelCache;
    private final FileSystemCache<K, V> secondLevelCache;
    private final CacheStrategy<K> strategy;

    public TwoLevelCache(final int memoryCapacity, final int fileCapacity, final StrategyType strategyType) {
        this.firstLevelCache = new MemoryCache<>(memoryCapacity);
        this.secondLevelCache = new FileSystemCache<>(fileCapacity);
        this.strategy = getStrategy(strategyType);
    }

    public TwoLevelCache(final int memoryCapacity, final int fileCapacity) {
        this.firstLevelCache = new MemoryCache<>(memoryCapacity);
        this.secondLevelCache = new FileSystemCache<>(fileCapacity);
        this.strategy = getStrategy(StrategyType.LFU);
    }
    private TwoLevelCache(TwoLevelCacheBuilder builder) {
        this.firstLevelCache = new MemoryCache<>(builder.memoryCapacity);
        this.secondLevelCache = new FileSystemCache<>(builder.fileCapacity);
        this.strategy = getStrategy(builder.strategyType);
    }

    private CacheStrategy<K> getStrategy(StrategyType strategyType) {
        switch (strategyType) {
            case LFU:
            default:
                return new LFUStrategy<>();
        }
    }

    @Override
    public void put(K newKey, V newValue) {
        if (firstLevelCache.containsKey(newKey) || firstLevelCache.hasEmptyPlace()) {
            System.out.println(format("Положим объект с ключом %s на 1 уровень", newKey));
            firstLevelCache.put(newKey, newValue);
            if (secondLevelCache.containsKey(newKey)) {
                secondLevelCache.remove(newKey);
            }
        } else if (secondLevelCache.containsKey(newKey) || secondLevelCache.hasEmptyPlace()) {
            System.out.println(format("Положим объект с ключом %s на 2 уровень", newKey));
            secondLevelCache.put(newKey, newValue);
        } else {
            // Здесь у нас полный кеш, и мы должны заменить какой-то объект новым в соответствии со стратегией кеша
            replaceObject(newKey, newValue);
        }

        if (!strategy.containsKey(newKey)) {
            System.out.println(format("Положим объект с ключом %s в нашу стратегию", newKey));
            strategy.putObject(newKey);
        }
    }

    private void replaceObject(K key, V value) {
        val replacedKey = strategy.getReplacedKey();
        if (firstLevelCache.containsKey(replacedKey)) {
            System.out.println(format("Заменяем объект с ключом %s с 1 уровня", replacedKey));
            firstLevelCache.remove(replacedKey);
            firstLevelCache.put(key, value);
        } else if (secondLevelCache.containsKey(replacedKey)) {
            System.out.println(format("Заменяем объект с ключом %s со 2 уровня", replacedKey));
            secondLevelCache.remove(replacedKey);
            secondLevelCache.put(key, value);
        }
    }

    @Override
    public V get(K key) {
        if (firstLevelCache.containsKey(key)) {
            strategy.putObject(key);
            return firstLevelCache.get(key);
        } else if (secondLevelCache.containsKey(key)) {
            strategy.putObject(key);
            return secondLevelCache.get(key);
        }
        return null;
    }

    @Override
    public void remove(K key) {
        if (firstLevelCache.containsKey(key)) {
            System.out.println(format("Удаляем объект с ключом %s с 1 уровня", key));
            firstLevelCache.remove(key);
        }
        if (secondLevelCache.containsKey(key)) {
            System.out.println(format("Удаляем объект с ключом %s со 2 уровня", key));
            secondLevelCache.remove(key);
        }
        strategy.removeObject(key);
    }

    @Override
    public int size() {
        return firstLevelCache.size() + secondLevelCache.size();
    }

    @Override
    public boolean containsKey(K key) {
        return firstLevelCache.containsKey(key) || secondLevelCache.containsKey(key);
    }

    @Override
    public void clear() {
        firstLevelCache.clear();
        secondLevelCache.clear();
        strategy.clear();
    }

    public boolean hasEmptyPlace() {
        return firstLevelCache.hasEmptyPlace() || secondLevelCache.hasEmptyPlace();
    }

    /**
     * Билдер Класс
     */
    public static class TwoLevelCacheBuilder {
        private int memoryCapacity;
        private int fileCapacity;
        private StrategyType strategyType;

        public TwoLevelCacheBuilder() {
        }
        public TwoLevelCacheBuilder memoryCapacity(final int memoryCapacity) {
            this.memoryCapacity = memoryCapacity;
            return this;
        }
        public TwoLevelCacheBuilder fileCapacity(final int fileCapacity) {
            this.fileCapacity = fileCapacity;
            return this;
        }
        public TwoLevelCacheBuilder strategyType(final StrategyType strategyType) {
            this.strategyType = strategyType;
            return this;
        }

        public TwoLevelCache build() {
            TwoLevelCache twoLevelCache = new TwoLevelCache(this);
            return twoLevelCache;
        }
    }
}
