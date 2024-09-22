package com.sql_processor;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Select {
    public static Map<Long, Map<String, Object>> select(String[] tokens, Map<Long, Map<String, Object>> data) throws Exception {
        Map<Long, Map<String, Object>> result = new ConcurrentHashMap<>();
        if (tokens.length > 1 && !tokens[1].equalsIgnoreCase("where")) {
            throw new Exception("Invalid command syntax: " + String.join(" ", tokens));
        }

        else if (tokens.length > 1 && tokens[1].equalsIgnoreCase("where")) {
            String[] keys = Arrays.copyOfRange(tokens, 2, tokens.length);
            result.putAll(Where.where(keys, data));
        }

        else { result.putAll(data); }
        return result;
    }
}
