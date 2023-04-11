package com.digdes.school;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Select {
    public static List<Map<String, Object>> select(String[] tokens, List<Map<String, Object>> data) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        if (tokens.length > 1 && !tokens[1].equalsIgnoreCase("where")) {
            throw new Exception("Invalid command syntax: " + String.join(" ", tokens));
        }

        else if (tokens.length > 1 && tokens[1].equalsIgnoreCase("where")) {
            String[] keys = Arrays.copyOfRange(tokens, 2, tokens.length);
            result.addAll(Where.where(keys, data));
        }

        else { result.addAll(data); }
        return result;
    }
}
