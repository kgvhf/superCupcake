package mi.superCupcake;

import lombok.SneakyThrows;
import lombok.val;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

class FileSystemCache<K, V extends Serializable> implements Cache<K, V> {
    private final Map<K, String> objectsStorage;
    private final Path tempDir;
    private int capacity;

    @SneakyThrows
    FileSystemCache() {
        this.tempDir = Files.createTempDirectory("cache");
        this.tempDir.toFile().deleteOnExit();
        this.objectsStorage = new HashMap<>();
    }

    @SneakyThrows
    FileSystemCache(int capacity) {
        this.tempDir = Files.createTempDirectory("cache");
        this.tempDir.toFile().deleteOnExit();
        this.capacity = capacity;
        this.objectsStorage = new HashMap<>(capacity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(K key) {
        if (containsKey(key)) {
            val fileName = objectsStorage.get(key);
            try (val fileInputStream = new FileInputStream(new File(tempDir + File.separator + fileName));
                 val objectInputStream = new ObjectInputStream(fileInputStream)) {
                return (V) objectInputStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                System.out.println(format("Не могу прочитать файл. %s: %s", fileName, e.getMessage()));
            }
        }
        return null;
    }

    @Override
    @SneakyThrows
    public void put(K key, V value) {
        val tmpFile = Files.createTempFile(tempDir, "", "").toFile();

        try (val outputStream = new ObjectOutputStream(new FileOutputStream(tmpFile))) {
            outputStream.writeObject(value);
            outputStream.flush();
            objectsStorage.put(key, tmpFile.getName());
        } catch (IOException e) {
            System.out.println("Не могу записать в файл " + tmpFile.getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void remove(K key) {
        val fileName = objectsStorage.get(key);
        val deletedFile = new File(tempDir + File.separator + fileName);
        if (deletedFile.delete()) {
            System.out.println(format("Кэш файл '%s' удален", fileName));
        } else {
            System.out.println(format("Не могу удалить файл %s", fileName));
        }
        objectsStorage.remove(key);
    }

    @Override
    public int size() {
        return objectsStorage.size();
    }

    @Override
    public boolean containsKey(K key) {
        return objectsStorage.containsKey(key);
    }

    public boolean hasEmptyPlace() {
        return size() < this.capacity;
    }

    @SneakyThrows
    @Override
    public void clear() {
        Files.walk(tempDir)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(file -> {
                    if (file.delete()) {
                        System.out.println(format("Кэш файл '%s' удален", file));
                    } else {
                        System.out.println(format("Не могу удалить файл %s", file));
                    }
                });
        objectsStorage.clear();
    }
}
