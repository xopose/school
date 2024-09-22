package com.db;

import java.util.Map;

public class Record implements iRecord {
    private final Map<String, Object> data;

    public Record(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public Object getField(String key) {
        return data.get(key);
    }

    @Override
    public void setField(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public void removeField(String key) {
        data.remove(key);
    }

    @Override
    public Map<String, Object> getAllFields() {
        return data;
    }
}
