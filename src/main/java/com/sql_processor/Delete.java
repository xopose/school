package com.sql_processor;

import java.util.Arrays;
import java.util.Map;

public class Delete {
    public static Map<Long, Map<String, Object>> delete(String[] tokens, Map<Long, Map<String, Object>> data) throws Exception {
        if (tokens.length > 1 && !tokens[1].equalsIgnoreCase("where")) {
            throw new Exception("Invalid command syntax: " + String.join(" ", tokens));
        }

        else if (tokens.length > 1 && tokens[1].equalsIgnoreCase("where")) {
            String[] keys = Arrays.copyOfRange(tokens, 2, tokens.length);
            Map<Long, Map<String, Object>> filteredData = Where.where(keys, data);
            filteredData.keySet().forEach(data::remove);
        }

        else { data.clear(); }
        return data;
    }
}
