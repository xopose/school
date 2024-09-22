package com.sql_processor;

import java.util.*;

public class Insert {
    static final String[] legal = {"id", "lastName", "age", "cost", "active"};
    public static Map<Long,Map<String, Object>> insert(String[] tokens, Map<Long,Map<String, Object>> data) throws Exception {
        if (!tokens[1].equalsIgnoreCase("values")) {
            throw new Exception("Invalid command syntax: " + String.join(" ", tokens[1]));
        }
        Map<String, Object> row = new HashMap<>();
        String[] values = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length)).split(",");

        long lastId = data.keySet().stream().max(Long::compare).orElse(0L);

        row.put("id", ++lastId);

        for (String s : values) {
            String sub;
            String value;
            int firstQuoteIndex = s.indexOf("'") + 1;
            int secondQuoteIndex = s.indexOf("'", firstQuoteIndex + 1);
            sub = s.substring(firstQuoteIndex, secondQuoteIndex);
            if (sub.replace(" ", "").equalsIgnoreCase("lastName")) {
                int thirdQuoteIndex = s.indexOf("'", secondQuoteIndex + 1);
                int fourthQuoteIndex = s.lastIndexOf("'");
                value = thirdQuoteIndex>1 & fourthQuoteIndex>1 ? s.substring(thirdQuoteIndex + 1, fourthQuoteIndex) : "";
            } else {
                value = s.substring(s.indexOf("=")+1).trim();
            }
            switch (sub.toLowerCase().replace(" ", "")) {
                case "lastname":
                    if (!value.isEmpty()){
                        if(value.matches("^[a-zA-Z ]*$") | value.matches("^[а-яА-Я ]*$")) {
                            row.put("lastName", value);
                        }
                    }
                    else {
                        row.put("lastName", null);
                    }
                    break;
                case "age":
                    if(!value.isEmpty()) {
                        row.put("age", Short.parseShort(value));
                    }
                    else {
                        row.put("age", null);
                    }
                    break;
                case "cost":
                    if(!value.isEmpty()) {
                        row.put("cost", Double.parseDouble(value));
                    }
                    else {
                        row.put("cost", null);
                    }
                    break;
                case "active":
                    if(!value.isEmpty()) {
                        row.put("active", Boolean.parseBoolean(value));
                    }
                    else {
                        row.put("active", null);
                    }
                    break;
                case "id":
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        for (String s: legal) {
            if (!row.containsKey(s)) row.put(s, null);
        }
        data.put(lastId, row);
        return data;
    }
}
