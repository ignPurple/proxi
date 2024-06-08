package net.ignpurple.proxi.api.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Storage<K, T> {
    protected final Map<K, T> storage = new ConcurrentHashMap<>();

    public T get(K key) {
        return this.storage.get(key);
    }

    public void register(K key, T type) {
        this.storage.put(key, type);
    }

    public void dispose(K key) {
        this.storage.remove(key);
    }
}
