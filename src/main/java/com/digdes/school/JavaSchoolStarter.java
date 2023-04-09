package com.digdes.school;

import com.sun.jdi.InvalidTypeException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSchoolStarter {
    private List<Map<String, Object>> data = new ArrayList<>();

    //Дефолтный конструктор
    public JavaSchoolStarter() {

    }

    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String, Object>> execute(String request) throws Exception {
        String[] tokens = request.trim().split("\\s+");
        if (tokens.length == 0) {
            throw new Exception("Empty command");
        }
        String command = tokens[0].toUpperCase();
        return switch (command) {
            case "SELECT" -> select(tokens);
            case "INSERT" -> insert(tokens);
            case "UPDATE" -> update(tokens);
            case "DELETE" -> delete(tokens);
            default -> throw new Exception("Unknown command: " + command);
        };
    }

    private List<Map<String, Object>> insert(String[] tokens) throws Exception {
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
            if (sub.equalsIgnoreCase("lastName")) {
                int thirdQuoteIndex = s.indexOf("'", secondQuoteIndex + 1);
                int fourthQuoteIndex = s.lastIndexOf("'");
                value = thirdQuoteIndex>1 & fourthQuoteIndex>1 ? s.substring(thirdQuoteIndex + 1, fourthQuoteIndex) : "";
            } else {
                value = s.substring(secondQuoteIndex + 2);
            }
            switch (sub.toLowerCase()) {
                case "id":
                    if(value.length()>0) {
                        row.put("id", Long.parseLong(value));
                        notNull = true;
                    }
                    break;
                case "lastname":
                    if (value.length()>0){
                        if(value.matches("^[a-zA-Z]*$") | value.matches("^[а-яА-Я]*$")) {
                            row.put("lastName", value);
                            notNull=true;

                        } else {
                            throw new InvalidTypeException();
                        }
                    }

                    break;
                case "age":
                    if(value.length()>0) {
                        row.put("age", Long.parseLong(value));
                        notNull = true;
                    }
                    break;
                case "cost":
                    if(value.length()>0) {
                        row.put("cost", Double.parseDouble(value));
                        notNull = true;
                    }
                    break;
                case "active":
                    if(value.length()>0) {
                        row.put("active", Boolean.parseBoolean(value));
                        notNull = true;
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        if (notNull) {
            List<Map<String, Object>> result = new ArrayList<>();
            result.add(row);
            data.add(row);
            return result;
        }
        else throw new NoSuchElementException();
    }

    private List<Map<String, Object>> update(String[] tokens) throws Exception {
        return null;
    }

    private List<Map<String, Object>> delete(String[] tokens) throws Exception {
        return null;
    }

    private List<Map<String, Object>> select(String[] tokens) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        if (tokens.length > 1 && !tokens[1].equalsIgnoreCase("where")) {
            throw new Exception("Invalid command syntax: " + String.join(" ", tokens));
        }

        else if (tokens.length > 1 && tokens[1].equalsIgnoreCase("where")) {
            String[] keys = Arrays.copyOfRange(tokens, 2, tokens.length);
            for (String s: keys){
                //System.out.println(s);

            }
            result.addAll(where(keys));
        }

        else { result.addAll(data); }
        return result;
    }
    private List<Map<String, Object>> where(String[] tokens) throws InvalidTypeException {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();

            if (token.equalsIgnoreCase("and") || token.equalsIgnoreCase("or")) {
                sb.append(" ");
                sb.append(token.toUpperCase());
                sb.append(" ");
            } else if (token.equals("=") || token.equals(">=") || token.equals("<=") || token.equals("<") || token.equals(">")) {
                sb.append(token);
            } else {
                sb.append(token);
            }
        }
        List<Map<String, Object>> buffList = new ArrayList<>();
        Map<String, Object> buffMap= new HashMap<>();
        String[] a = sb.toString().split(" ");
        if (a[0].equalsIgnoreCase("and") | a[0].equalsIgnoreCase("or") |
                a[a.length-1].equalsIgnoreCase("and") | a[a.length-1].equalsIgnoreCase("or")){
            throw new IllegalArgumentException();
        }
        boolean flag_end = false;
        for (int i = 0; i<= a.length-1; i+=2) {
            if (!a[i].equalsIgnoreCase("and") & !a[i].equalsIgnoreCase("or")) {
                if (a.length-1==0 | (flag_end && a[a.length-2].equalsIgnoreCase("and")) || a[i+1].equalsIgnoreCase("and")) {
                    if(flag_end) {
                        i = a.length-1;
                    } else if (i==a.length-3) {
                        flag_end = true;
                    }

                    String sub;
                    String value;
                    String oper = "";
                    int firstQuoteIndex = a[i].indexOf("'") + 1;
                    int secondQuoteIndex = a[i].indexOf("'", firstQuoteIndex + 1);
                    sub = a[i].substring(firstQuoteIndex, secondQuoteIndex);
                    if (sub.equalsIgnoreCase("lastName")) {
                        int thirdQuoteIndex = a[i].indexOf("'", secondQuoteIndex + 1);
                        int fourthQuoteIndex = a[i].lastIndexOf("'");
                        value = a[i].substring(thirdQuoteIndex + 1, fourthQuoteIndex);
                    } else {
                        value = a[i].substring(secondQuoteIndex + 1);
                        if (operator(value).length() > 0) {
                            oper=operator(value);
                            value = value.substring(operator(value).length());
                        }
                    }

                    switch (sub.toLowerCase()) {
                        case "id":
                            if(value.length()>0) {
                                if (buffList.size()>0) {
                                    for (Map<String, Object> map : buffList) {
                                        Object mapValue = map.get("id");
                                        if (compareValues(mapValue, oper, Long.parseLong(value))) {
                                            if (!buffList.contains(map)) {
                                                buffList.add(map);
                                            }
                                        }
                                    }
                                }
                                else {
                                    for (Map<String, Object> map : data) {
                                        Object mapValue = map.get("id");
                                        if (compareValues(mapValue, oper, Long.parseLong(value))) {
                                            if (!buffList.contains(map)) {
                                                buffList.add(map);
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                throw new NullPointerException();
                            }
                            break;
                        case "lastname":
                            if (value.matches("^[a-zA-Z]*$") | value.matches("^[а-яА-Я]*$")) {
                                for (Map<String, Object> stringObjectMap : data) {
                                    if (stringObjectMap.containsKey("lastName") && stringObjectMap.containsValue(value)){
                                        buffMap.put("lastName", value);
                                    }
                                }
                            } else {
                                throw new InvalidTypeException();
                            }
                            break;
                        case "age":
                            if(value.length()>0) {
                                if (buffList.size()>0) {
                                    for (Map<String, Object> map : buffList) {
                                        Object mapValue = map.get("age");
                                        if (compareValues(mapValue, oper, Long.parseLong(value))) {
                                            if (!buffList.contains(map)) {
                                                buffList.add(map);
                                            }
                                        }
                                    }
                                }
                                else {
                                    for (Map<String, Object> map : data) {
                                        Object mapValue = map.get("age");
                                        if (compareValues(mapValue, oper, Long.parseLong(value))) {
                                            if (!buffList.contains(map)) {
                                                buffList.add(map);
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                throw new NullPointerException();
                            }
                            break;
                        case "cost":
                            double val3 = Double.parseDouble(value);
                            for (Map<String, Object> stringObjectMap : data) {
                                if (stringObjectMap.containsKey("cost") && stringObjectMap.containsValue(val3)){
                                    buffMap.put("cost", val3);
                                }
                            }
                            break;
                        case "active":
                            boolean val4 = Boolean.parseBoolean(value);

                            for (Map<String, Object> stringObjectMap : data) {
                                if (stringObjectMap.containsKey("active") && stringObjectMap.containsValue(val4)){
                                    buffMap.put("active", val4);
                                }
                            }
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                }

            }
            else {

            }
        }
        buffList.add(buffMap);
        for (Map<String, Object> j: buffList){
            System.out.println(j.keySet() + " " + j.values());
        }


        return buffList;
    }

    private String operator(String a){
        if(a.startsWith(">="))     return ">=";
        if(a.startsWith("<="))     return "<=";
        if(a.startsWith("!="))     return "!=";
        if(a.startsWith("="))      return "=";
        if(a.startsWith(">"))      return ">";
        if(a.startsWith("<"))      return "<";
        if(a.startsWith("like"))   return "like";
        if (a.startsWith("ilike")) return "ilike";
        return "";
    }
    private boolean compareValues(Object mapValue, String operator, long value) {
        if (mapValue instanceof Long) {
            long longValue = (Long) mapValue;
            switch (operator) {
                case ">":
                    return longValue > value;
                case ">=":
                    return longValue >= value;
                case "<":
                    return longValue < value;
                case "<=":
                    return longValue <= value;
                case "!=":
                    return longValue != value;
                default:
                    return false;
            }
        }
        return false;
    }

}
