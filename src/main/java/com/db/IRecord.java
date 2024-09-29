package com.db;

import java.util.Map;

public interface IRecord {
    Object getField(String key);
    void setField(String key, Object value);
    void removeField(String key);
    Map<String, Object> getAllFields();
}
