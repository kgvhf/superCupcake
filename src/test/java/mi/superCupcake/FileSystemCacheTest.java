package mi.superCupcake;

import org.junit.jupiter.api.*;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class FileSystemCacheTest {
    
    private FileSystemCache<Integer, String> fileSystemCache;

    @BeforeEach
    public void init() {
        fileSystemCache = new FileSystemCache<>();
    }

    @AfterEach
    public void clear() {
        fileSystemCache.clear();
    }

    @Test
    public void shouldPutGetAndRemoveObjectTest() {
        fileSystemCache.put(0, "value1");
        assertEquals("value1", fileSystemCache.get(0));
        assertEquals(1, fileSystemCache.size());

        fileSystemCache.remove(0);
        assertNull(fileSystemCache.get(0));
    }

    @Test
    public void shouldNotGetObjectFromCacheIfNotExistsTest() {
        fileSystemCache.put(0, "value1");
        assertEquals("value1", fileSystemCache.get(0));
        assertNull(fileSystemCache.get(111));
    }

    @Test
    public void shouldNotRemoveObjectFromCacheIfNotExistsTest() {
        fileSystemCache.put(0, "value1");
        assertEquals("value1", fileSystemCache.get(0));
        assertEquals(1, fileSystemCache.size());

        fileSystemCache.remove(5);
        assertEquals("value1", fileSystemCache.get(0));
    }

    @Test
    public void shouldSizeTest() {
        fileSystemCache.put(0, "value1");
        assertEquals(1, fileSystemCache.size());

        fileSystemCache.put(1, "value2");
        assertEquals(2, fileSystemCache.size());
    }

    @Test
    public void containsKeyTest() {
        assertFalse(fileSystemCache.containsKey(0));

        fileSystemCache.put(0, "value1");
        assertTrue(fileSystemCache.containsKey(0));
    }

    @Test
    public void isEmptyPlaceTest() {
        fileSystemCache = new FileSystemCache<>(5);

        IntStream.range(0, 4).forEach(i -> fileSystemCache.put(i, "String " + i));
        assertTrue(fileSystemCache.hasEmptyPlace());
        fileSystemCache.put(5, "String");
        assertFalse(fileSystemCache.hasEmptyPlace());
    }

    @Test
    public void shouldClearTest() {
        IntStream.range(0, 3).forEach(i -> fileSystemCache.put(i, "String " + i));

        assertEquals(3, fileSystemCache.size());
        fileSystemCache.clear();
        assertEquals(0, fileSystemCache.size());
    }
}
