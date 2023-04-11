package com.digdes.school;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Update {
    public static List<Map<String, Object>> update(String[] tokens, List<Map<String, Object>> data) throws Exception {
        if (!tokens[1].equalsIgnoreCase("values")) {
            throw new Exception("Invalid command syntax: " + String.join(" ", tokens[1]));
        }
        int from = 0;
        for (int i=0; i<=tokens.length-1; i++){
            if (tokens[i].equalsIgnoreCase("where")) from=i;
        }
        if (from==0){
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
                System.out.println(sub);
                switch (sub.toLowerCase().replace(" ", "")){
                    case "id":
                        for (Map<String, Object> tmpMap: data){
                            if (value.equalsIgnoreCase("null") & tmpMap.containsKey("id")) tmpMap.replace("id", null);
                            else if (tmpMap.containsKey("id")) tmpMap.replace("id", Long.parseLong(value));
                        }
                        break;
                    case "lastname":
                        for (Map<String, Object> tmpMap: data){
                            if (value.equalsIgnoreCase("null") & tmpMap.containsKey("lastName")) tmpMap.replace("lastName", null);
                            else if (tmpMap.containsKey("lastName") && (value.matches("^[a-zA-Z ]*$") | value.matches("^[а-яА-Я ]*$"))) tmpMap.replace("lastName", value);
                        }
                        break;
                    case "age":
                        for (Map<String, Object> tmpMap: data){
                            if (value.equalsIgnoreCase("null") & tmpMap.containsKey("age")) tmpMap.replace("age", null);
                            else if (tmpMap.containsKey("age")) tmpMap.replace("age", Long.parseLong(value));
                        }
                        break;
                    case "cost":
                        for (Map<String, Object> tmpMap: data){
                            if (value.equalsIgnoreCase("null") & tmpMap.containsKey("cost")) tmpMap.replace("cost", null);
                            else if (tmpMap.containsKey("cost")) tmpMap.replace("cost", Double.parseDouble(value));
                        }
                        break;
                    case "active":
                        for (Map<String, Object> tmpMap: data){
                            if (value.equalsIgnoreCase("null") & tmpMap.containsKey("active")) tmpMap.replace("active", null);
                            else if (tmpMap.containsKey("active")) tmpMap.replace("active", Boolean.parseBoolean(value));
                        }
                        break;
                }
            }
        }
        else {
            String[] values = String.join(" ", Arrays.copyOfRange(tokens, 2, from)).split(",");
            String[] keys = Arrays.copyOfRange(tokens, from + 1, tokens.length);
            List<Map<String, Object>> whereList = Where.where(keys, data);
            for (String s : values) {
                String sub;
                String value;
                int firstQuoteIndex = s.indexOf("'") + 1;
                int secondQuoteIndex = s.indexOf("'", firstQuoteIndex + 1);
                sub = s.substring(firstQuoteIndex, secondQuoteIndex);
                if (sub.replace(" ", "").equalsIgnoreCase("lastName")) {
                    int thirdQuoteIndex = s.indexOf("'", secondQuoteIndex + 1);
                    int fourthQuoteIndex = s.lastIndexOf("'");
                    value = thirdQuoteIndex > 1 & fourthQuoteIndex > 1 ? s.substring(thirdQuoteIndex + 1, fourthQuoteIndex) : "";
                } else {
                    value = s.substring(s.indexOf("=")+1).trim();
                }
                switch (sub.toLowerCase().replace(" ", "")) {
                    case "id":
                        if (value.length() > 0) {
                            if (!value.equalsIgnoreCase("null")) {
                                for (Map<String, Object> map : data) {
                                    for (Map<String, Object> tmpMap : whereList) {
                                        if (map.keySet().containsAll(tmpMap.keySet()) && map.entrySet().containsAll(tmpMap.entrySet())) {
                                            map.replace("id", Long.parseLong(value));
                                            tmpMap.replace("id", Long.parseLong(value));
                                        }
                                    }
                                }
                            } else {
                                for (Map<String, Object> map : data) {
                                    for (Map<String, Object> tmpMap : whereList) {
                                        if (map.keySet().containsAll(tmpMap.keySet()) && map.entrySet().containsAll(tmpMap.entrySet())) {
                                            map.replace("id", null);
                                            tmpMap.replace("id", null);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "lastname":
                        if (value.length() > 0) {
                            if (!value.equalsIgnoreCase("null")) {

                                for (Map<String, Object> map : data) {
                                    for (Map<String, Object> tmpMap : whereList) {
                                        if (map.keySet().containsAll(tmpMap.keySet()) && map.entrySet().containsAll(tmpMap.entrySet())) {
                                            map.replace("lastName", (value.matches("^[a-zA-Z ]*$") | value.matches("^[а-яА-Я ]*$")) ? value: null);
                                        }
                                    }
                                }
                            } else {
                                for (Map<String, Object> map : data) {
                                    for (Map<String, Object> tmpMap : whereList) {
                                        if (map.keySet().containsAll(tmpMap.keySet()) && map.entrySet().containsAll(tmpMap.entrySet())) {
                                            map.replace("lastName", null);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "age":
                        if (value.length() > 0) {
                            if (!value.equalsIgnoreCase("null")) {
                                for (Map<String, Object> map : data) {
                                    for (Map<String, Object> tmpMap : whereList) {
                                        if (map.keySet().containsAll(tmpMap.keySet()) && map.entrySet().containsAll(tmpMap.entrySet())) {
                                            map.replace("age", Long.parseLong(value));
                                            tmpMap.replace("age", Long.parseLong(value));
                                        }
                                    }
                                }
                            } else {
                                for (Map<String, Object> map : data) {
                                    for (Map<String, Object> tmpMap : whereList) {
                                        if (map.keySet().containsAll(tmpMap.keySet()) && map.entrySet().containsAll(tmpMap.entrySet())) {
                                            map.replace("age", null);
                                            tmpMap.replace("age", null);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "cost":
                        if (value.length() > 0) {
                            if (!value.equalsIgnoreCase("null")) {
                                for (Map<String, Object> map : data) {
                                    for (Map<String, Object> tmpMap : whereList) {
                                        if (map.keySet().containsAll(tmpMap.keySet()) && map.entrySet().containsAll(tmpMap.entrySet())) {
                                            map.replace("cost", Double.parseDouble(value));
                                            tmpMap.replace("cost", Double.parseDouble(value));
                                        }
                                    }
                                }
                            } else {
                                for (Map<String, Object> map : data) {
                                    for (Map<String, Object> tmpMap : whereList) {
                                        if (map.keySet().containsAll(tmpMap.keySet()) && map.entrySet().containsAll(tmpMap.entrySet())) {
                                            map.replace("cost", null);
                                            tmpMap.replace("cost", null);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "active":
                        if (value.length() > 0) {
                            if (!value.equalsIgnoreCase("null")) {
                                for (Map<String, Object> map : data) {
                                    for (Map<String, Object> tmpMap : whereList) {
                                        if (map.keySet().containsAll(tmpMap.keySet()) && map.entrySet().containsAll(tmpMap.entrySet())) {
                                            map.replace("active", Boolean.parseBoolean(value));
                                            tmpMap.replace("active", Boolean.parseBoolean(value));
                                        }
                                    }
                                }
                            } else {
                                for (Map<String, Object> map : data) {
                                    for (Map<String, Object> tmpMap : whereList) {
                                        if (map.keySet().containsAll(tmpMap.keySet()) && map.entrySet().containsAll(tmpMap.entrySet())) {
                                            map.replace("active", null);
                                            tmpMap.replace("active", null);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        }
        return data;
    }
}
