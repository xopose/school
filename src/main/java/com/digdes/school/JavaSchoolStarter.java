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
                value = s.substring(thirdQuoteIndex + 1, fourthQuoteIndex);
            } else {
                value = s.substring(secondQuoteIndex + 2);
            }
            switch (sub.toLowerCase()) {
                case "id":
                    long val = Long.parseLong(value);
                    row.put("id", val);
                    break;
                case "lastname":
                    if (value.matches("^[a-zA-Z]*$") | value.matches("^[а-яА-Я]*$")) {
                        row.put("lastName", value);
                    } else {
                        throw new InvalidTypeException();
                    }
                    break;
                case "age":
                    long val2 = Long.parseLong(value);
                    row.put("age", val2);
                    break;
                case "cost":
                    double val3 = Double.parseDouble(value);
                    row.put("cost", val3);
                    break;
                case "active":
                    boolean val4 = Boolean.parseBoolean(value);
                    row.put("active", val4);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(row);
        data.add(row);
        return result;
    }

    private List<Map<String, Object>> update(String[] tokens) throws Exception {
        return null;
    }

    private List<Map<String, Object>> delete(String[] tokens) throws Exception {
        return null;
    }

    private List<Map<String, Object>> select(String[] tokens) throws Exception {
        Map<String, Object> row = new HashMap<>();
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
        for (int i = 0; i< a.length; i++) {
            if ((i < a.length - 1 && a[i + 1].equalsIgnoreCase("and")) |
                    ((i == a.length - 1 && !a[i].equalsIgnoreCase("and")) && !a[i].equalsIgnoreCase("or"))) {
                if (!a[i].equalsIgnoreCase("and") & !a[i].equalsIgnoreCase("or")) {
                    String sub;
                    String value;
                    int firstQuoteIndex = a[i].indexOf("'") + 1;
                    int secondQuoteIndex = a[i].indexOf("'", firstQuoteIndex + 1);
                    sub = a[i].substring(firstQuoteIndex, secondQuoteIndex);
                    if (sub.equalsIgnoreCase("lastName")) {
                        int thirdQuoteIndex = a[i].indexOf("'", secondQuoteIndex + 1);
                        int fourthQuoteIndex = a[i].lastIndexOf("'");
                        value = a[i].substring(thirdQuoteIndex + 1, fourthQuoteIndex);
                    } else {
                        value = a[i].substring(secondQuoteIndex + 1);
                        if (operator(value) != 0) {
                            value = value.substring(operator(value));
                        }
                    }
                    switch (sub.toLowerCase()) {
                        case "id":
                            long val = Long.parseLong(value);
                            for (Map<String, Object> stringObjectMap : data) {
                                if (stringObjectMap.containsKey("id") && stringObjectMap.containsValue(val)){
                                    buffMap.put("id", val);
                                }
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
                            long val2 = Long.parseLong(value);
                            for (Map<String, Object> stringObjectMap : data) {
                                if (stringObjectMap.containsKey("age") && stringObjectMap.containsValue(val2)){
                                    buffMap.put("age", val2);
                                }
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


        return new ArrayList<>();
    }

    private int operator(String a){
        if(a.startsWith(">=") |  a.startsWith("<=") | a.startsWith("!=") | a.startsWith("=")){ return 2; }
        else {if (a.startsWith(">") | a.startsWith("<")){ return 1; }
        else  if (a.startsWith("like")){ return 4; }
        else if (a.startsWith("ilike")){return 5;}
        return 0;
    }
    }

}
