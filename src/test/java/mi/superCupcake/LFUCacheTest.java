package mi.superCupcake;

import mi.superCupcake.strategies.StrategyType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LFUCacheTest {
    private TwoLevelCache<Integer, String> twoLevelCache;

    @AfterEach
    public void clear() {
        twoLevelCache.clear();
    }

    @Test
    public void shouldMoveObjectFromCacheTest() {
        twoLevelCache = new TwoLevelCache.TwoLevelCacheBuilder()
                .memoryCapacity(2)
                .fileCapacity(2)
                .strategyType(StrategyType.LFU)
                .build();

        twoLevelCache.put(0, "String 0");
        twoLevelCache.get(0);
        twoLevelCache.get(0);
        twoLevelCache.put(1, "String 1");
        twoLevelCache.get(1); // Кандидат на удаление
        twoLevelCache.put(2, "String 2");
        twoLevelCache.get(2);
        twoLevelCache.get(2);
        twoLevelCache.put(3, "String 3");
        twoLevelCache.get(3);
        twoLevelCache.get(3);

        assertTrue(twoLevelCache.containsKey(0));
        assertTrue(twoLevelCache.containsKey(1));
        assertTrue(twoLevelCache.containsKey(2));
        assertTrue(twoLevelCache.containsKey(3));

        twoLevelCache.put(4, "String 4");
        twoLevelCache.get(4);
        twoLevelCache.get(4);

        assertTrue(twoLevelCache.containsKey(0));
        assertFalse(twoLevelCache.containsKey(1)); // Должен быть удален
        assertTrue(twoLevelCache.containsKey(2));
        assertTrue(twoLevelCache.containsKey(3));
        assertTrue(twoLevelCache.containsKey(4));
    }

    @Test
    public void shouldNotRemoveObjectIfNotPresentTest() {
        twoLevelCache = new TwoLevelCache<>(1, 1, StrategyType.LFU);

        twoLevelCache.put(0, "String 0");
        twoLevelCache.put(1, "String 1");

        twoLevelCache.remove(2);

    }
}