package com.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database implements iDatabase {
    private final Map<Long, Map<String, Object>> database = new ConcurrentHashMap<>();

    @Override
    public Map<String, Object> findById(Long id) {
        return database.get(id);
    }

    @Override
    public void save(Long id, Map<String, Object> data) {
        database.put(id, data);
    }

    @Override
    public void update(Long id, Map<String, Object> data) {
        if (database.containsKey(id)) {
            database.put(id, data);
        } else {
            throw new IllegalArgumentException("Record with ID " + id + " not found.");
        }
    }

    @Override
    public void delete(Long id) {
        database.remove(id);
    }

    @Override
    public Map<Long, Map<String, Object>> findAll() {
        return new HashMap<>(database);
    }
}
