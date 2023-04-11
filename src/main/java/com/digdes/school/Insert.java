package com.digdes.school;

import java.util.*;

public class Insert {
    static String[] legal = {"id", "lastName", "age", "cost", "active"};
    public static List<Map<String, Object>> insert(String[] tokens, List<Map<String, Object>> data) throws Exception {
        boolean notNull = false;
        if (!tokens[1].equalsIgnoreCase("values")) {
            throw new Exception("Invalid command syntax: " + String.join(" ", tokens[1]));
        }
        Map<String, Object> row = new HashMap<>();
        String[] values = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length)).split(",");
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
                case "id":
                    if(value.length()>0) {
                        row.put("id", Long.parseLong(value));
                        notNull = true;
                    }
                    else {
                        row.put("id", null);
                    }
                    break;
                case "lastname":
                    if (value.length()>0){
                        if(value.matches("^[a-zA-Z ]*$") | value.matches("^[а-яА-Я ]*$")) {
                            row.put("lastName", value);
                            notNull=true;

                        }
                    }
                    else {
                        row.put("lastName", null);
                    }
                    break;
                case "age":
                    if(value.length()>0) {
                        row.put("age", Long.parseLong(value));
                        notNull = true;
                    }
                    else {
                        row.put("age", null);
                    }
                    break;
                case "cost":
                    if(value.length()>0) {
                        row.put("cost", Double.parseDouble(value));
                        notNull = true;
                    }
                    else {
                        row.put("cost", null);
                    }
                    break;
                case "active":
                    if(value.length()>0) {
                        row.put("active", Boolean.parseBoolean(value));
                        notNull = true;
                    }
                    else {
                        row.put("active", null);
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        for (String s: legal) {
            if (!row.containsKey(s)) row.put(s, null);
        }
        if (notNull) {
            data.add(row);
            return data;
        }
        else throw new NoSuchElementException();
    }
}
