package com.digdes.school;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Delete {
    public static List<Map<String, Object>> delete(String[] tokens, List<Map<String, Object>> data) throws Exception {
        if (tokens.length > 1 && !tokens[1].equalsIgnoreCase("where")) {
            throw new Exception("Invalid command syntax: " + String.join(" ", tokens));
        }

        else if (tokens.length > 1 && tokens[1].equalsIgnoreCase("where")) {
            String[] keys = Arrays.copyOfRange(tokens, 2, tokens.length);
            data.removeAll(Where.where(keys, data));
        }

        else { data.clear(); }
        return data;
    }
}
