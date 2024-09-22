package com.db;

import java.util.Map;

public interface iDatabase {
    Map<String, Object> findById(Long id);
    void save(Long id, Map<String, Object> data);
    void update(Long id, Map<String, Object> data);
    void delete(Long id);
    Map<Long, Map<String, Object>> findAll();
}
