package mi.superCupcake;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MemoryCacheTest {

    private MemoryCache<Integer, String> memoryCache;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        memoryCache = new MemoryCache<>(3);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        memoryCache.clear();
    }

    @Test
    void putGetAndRemoveTest() {
        memoryCache.put(0, "value");

        assertEquals(1, memoryCache.size());
        assertEquals("value", memoryCache.get(0));

        memoryCache.remove(0);
        assertNull(memoryCache.get(0));

    }

    @Test
    public void shouldNotGetObjectFromCacheIfNotExistsTest() {
        memoryCache.put(0, "value1");
        assertEquals("value1", memoryCache.get(0));
        assertNull(memoryCache.get(111));
    }

    @Test
    public void shouldNotRemoveObjectFromCacheIfNotExistsTest() {
        memoryCache.put(0, "value1");
        assertEquals("value1", memoryCache.get(0));
        assertEquals(1, memoryCache.size());

        memoryCache.remove(5);
        assertEquals("value1", memoryCache.get(0));
    }

    @Test
    public void shouldSizeTest() {
        memoryCache.put(0, "value1");
        assertEquals(1, memoryCache.size());

        memoryCache.put(1, "value2");
        assertEquals(2, memoryCache.size());
    }

    @Test
    public void containsKeyTest() {
        assertFalse(memoryCache.containsKey(0));

        memoryCache.put(0, "value1");
        assertTrue(memoryCache.containsKey(0));
    }

    @Test
    public void isEmptyPlaceTest() {
        memoryCache = new MemoryCache<>(5);

        IntStream.range(0, 4).forEach(i -> memoryCache.put(i, "String " + i));

        assertTrue(memoryCache.hasEmptyPlace());
        memoryCache.put(5, "String");
        assertFalse(memoryCache.hasEmptyPlace());
    }

    @Test
    public void shouldClearTest() {
        IntStream.range(0, 3).forEach(i -> memoryCache.put(i, "String " + i));

        assertEquals(3, memoryCache.size());
        memoryCache.clear();
        assertEquals(0, memoryCache.size());
    }
}