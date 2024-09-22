package com.sql_processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Where {

    public static Map<Long, Map<String, Object>> where(String[] tokens, Map<Long, Map<String, Object>> data) {
        StringBuilder sb = new StringBuilder();
        for (String s : tokens) {
            String token = s.trim();
            if (token.equalsIgnoreCase("and") || token.equalsIgnoreCase("or")) {
                sb.append(" ");
                sb.append(token.toUpperCase());
                sb.append(" ");
            } else if (token.equals("=") || token.equals(">=") || token.equals("<=") || token.equals("<") || token.equals(">") || token.equals("like") || token.equals("ilike")) {
                sb.append(token);
            } else {
                sb.append(token);
            }
        }
        Map<Long, Map<String, Object>> buffList = new ConcurrentHashMap<>();
        String[] a = sb.toString().split(" ");
        if (a[0].equalsIgnoreCase("and") | a[0].equalsIgnoreCase("or") |
                a[a.length-1].equalsIgnoreCase("and") | a[a.length-1].equalsIgnoreCase("or")){
            throw new IllegalArgumentException();
        }
        boolean flag_end = false;
        for (int i = 0; i<= a.length-1; i+=2) {
            if (!a[i].equalsIgnoreCase("and") & !a[i].equalsIgnoreCase("or")) {
                if (a.length-1==0 || ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end && a[i+1].equalsIgnoreCase("and"))) || ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end && a[i+1].equalsIgnoreCase("or")))) {
                    if(flag_end) {
                        i = a.length-1;
                    } else if (i==a.length-3) {
                        flag_end = true;
                    }
                    String sub;
                    String[] value = { "" };
                    String[] oper = { "" };
                    String[] strOper = { "" };
                    int firstQuoteIndex = a[i].indexOf("'") + 1;
                    int secondQuoteIndex = a[i].indexOf("'", firstQuoteIndex + 1);
                    sub = a[i].substring(firstQuoteIndex, secondQuoteIndex);
                    if (sub.replace(" ", "").equalsIgnoreCase("lastName")) {
                        int thirdQuoteIndex = a[i].indexOf("'", secondQuoteIndex + 1);
                        int fourthQuoteIndex = a[i].lastIndexOf("'");
                        value[0] = a[i].substring(thirdQuoteIndex + 1, fourthQuoteIndex);
                        strOper[0] = a[i].substring(secondQuoteIndex+1, thirdQuoteIndex).strip();
                    } else {
                        value[0] = a[i].substring(secondQuoteIndex + 1);
                        if (!operator(value[0]).isEmpty()) {
                            oper[0]=operator(value[0]);
                            value[0] = value[0].substring(operator(value[0]).length());
                        }
                    }
                    switch (sub.toLowerCase().replace(" ", "")) {
                        case "id":
                            if (value[0].isEmpty()){
                                value[0]="null";
                            }
                            if (!buffList.isEmpty()) {
                                if ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end &&a[i+1].equalsIgnoreCase("and"))){
                                    buffList.forEach((k, v) -> {
                                        Object mapValue = v.get("id");
                                        if (!value[0].startsWith("null")) {
                                            if (!compareValues(mapValue, oper[0], Long.parseLong(value[0]))) {
                                                buffList.remove(k);
                                            }
                                        }
                                    });
                                }
                                else if ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end &&a[i+1].equalsIgnoreCase("or"))){
                                    Map<Long, Map<String, Object>> compareList = new ConcurrentHashMap<>();
                                    buffList.forEach((k, v) -> {
                                        Object mapValue = v.get("id");
                                        if (value[0].startsWith("null")) {
                                            if (!compareValues(mapValue, oper[0], Double.NaN)) {
                                                compareList.put(k, v);
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper[0], Long.parseLong(value[0]))) {
                                                compareList.put(k, v);
                                            }
                                        }
                                    });
                                    compareList.keySet().forEach(buffList::remove);
                                    compareList.entrySet().stream()
                                            .filter(entry -> !buffList.containsKey(entry.getKey()))
                                            .forEach(entry -> buffList.put(entry.getKey(), entry.getValue()));
                                    compareList.clear();
                                }

                            }

                            else {
                                data.forEach((k, v) -> {
                                    Object mapValue = v.get("id");
                                    if (value[0].startsWith("null")){
                                        if (compareValues(mapValue, oper[0], Double.NaN)) {
                                            buffList.put(k, v);
                                        }
                                    }
                                    else {
                                        if (compareValues(mapValue, oper[0], Long.parseLong(value[0]))) {
                                            buffList.put(k, v);
                                        }
                                    }
                                });
                            }
                            break;
                        case "lastname":
                            if(!value[0].isEmpty() && (value[0].matches("^.*[a-zA-Z ]*.*$") | value[0].matches("^.*[а-яА-Я ]*.*$"))) {
                                if (!buffList.isEmpty()) {
                                    if ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end &&a[i+1].equalsIgnoreCase("and"))){
                                        buffList.forEach((k, v) -> {
                                            Object mapValue = v.get("lastName");
                                            if (!compareString(mapValue, strOper[0], value[0])) {
                                                buffList.remove(k);
                                            }
                                        });
                                    }
                                    else if ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end &&a[i+1].equalsIgnoreCase("or"))){
                                        Map<Long, Map<String, Object>> compareList = new ConcurrentHashMap<>();
                                        buffList.forEach((k, v) -> {
                                            Object mapValue = v.get("lastName");
                                            if (value[0].startsWith("null")) {
                                                if (!compareString(mapValue, strOper[0], value[0])) {
                                                    compareList.put(k, v);
                                                }
                                            } else {
                                                if (!compareString(mapValue, strOper[0], value[0])) {
                                                    compareList.put(k, v);
                                                }
                                            }
                                        });
                                        compareList.keySet().forEach(buffList::remove);
                                        compareList.entrySet().stream()
                                                .filter(entry -> !buffList.containsKey(entry.getKey()))
                                                .forEach(entry -> buffList.put(entry.getKey(), entry.getValue()));
                                        compareList.clear();
                                    }
                                }

                                else {
                                    data.forEach((k, v) -> {
                                        Object mapValue = v.get("lastName");
                                        if (compareString(mapValue, strOper[0], value[0])) {
                                            buffList.put(k, v);
                                        }
                                    });
                                }
                            }
                            else {
                                throw new NullPointerException();
                            }

                            break;
                        case "age":
                            if (value[0].isEmpty()){
                                value[0]="null";
                            }
                            if (!buffList.isEmpty()) {
                                if ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end &&a[i+1].equalsIgnoreCase("and"))){
                                    buffList.forEach((k, v) -> {
                                        Object mapValue = v.get("age");
                                        if (value[0].startsWith("null")) {
                                            if (!compareValues(mapValue, oper[0], Double.NaN)) {
                                                buffList.remove(k);
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper[0], Short.parseShort(value[0]))) {
                                                buffList.remove(k);
                                            }
                                        }
                                    });
                                }
                                else if ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end &&a[i+1].equalsIgnoreCase("or"))){
                                    Map<Long, Map<String, Object>> compareList = new ConcurrentHashMap<>();
                                    buffList.forEach((k, v) -> {
                                        Object mapValue = v.get("age");
                                        if (value[0].startsWith("null")) {
                                            if (!compareValues(mapValue, oper[0], Double.NaN)) {
                                                compareList.put(k, v);
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper[0], Double.parseDouble(value[0]))) {
                                                compareList.put(k, v);
                                            }
                                        }
                                    });
                                    compareList.keySet().forEach(buffList::remove);
                                    compareList.entrySet().stream()
                                            .filter(entry -> !buffList.containsKey(entry.getKey()))
                                            .forEach(entry -> buffList.put(entry.getKey(), entry.getValue()));
                                    compareList.clear();
                                }
                            }

                            else {
                                data.forEach((k, v) -> {
                                    Object mapValue = v.get("age");
                                    if (value[0].startsWith("null")){
                                        if (compareValues(mapValue, oper[0], Double.NaN)) {
                                            buffList.put(k, v);
                                        }
                                    }
                                    else {
                                        if (compareValues(mapValue, oper[0], Double.parseDouble(value[0]))) {
                                            buffList.put(k, v);
                                        }
                                    }
                                });
                            }
                            break;
                        case "cost":
                            if (value[0].isEmpty()){
                                value[0]="null";
                            }
                            if (!buffList.isEmpty()) {
                                if ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end &&a[i+1].equalsIgnoreCase("and"))){
                                    buffList.forEach((k, v) -> {
                                        Object mapValue = v.get("cost");
                                        if (value[0].startsWith("null")) {
                                            if (!compareValues(mapValue, oper[0], Double.NaN)) {
                                                buffList.remove(k);
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper[0], Double.parseDouble(value[0]))) {
                                                buffList.remove(k);
                                            }
                                        }
                                    });
                                }
                                else if ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end &&a[i+1].equalsIgnoreCase("or"))){
                                    Map<Long, Map<String, Object>> compareList = new ConcurrentHashMap<>();
                                    buffList.forEach((k, v) -> {
                                        Object mapValue = v.get("cost");
                                        if (value[0].startsWith("null")) {
                                            if (!compareValues(mapValue, oper[0], Double.NaN)) {
                                                compareList.put(k, v);
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper[0], Double.parseDouble(value[0]))) {
                                                compareList.put(k, v);
                                            }
                                        }
                                    });
                                    compareList.keySet().forEach(buffList::remove);
                                    compareList.entrySet().stream()
                                            .filter(entry -> !buffList.containsKey(entry.getKey()))
                                            .forEach(entry -> buffList.put(entry.getKey(), entry.getValue()));
                                    compareList.clear();
                                }
                            }

                            else {
                                data.forEach((k, v) -> {
                                    Object mapValue = v.get("cost");
                                    if (value[0].startsWith("null")){
                                        if (compareValues(mapValue, oper[0], Double.NaN)) {
                                            buffList.put(k, v);
                                        }
                                    }
                                    else {
                                        if (compareValues(mapValue, oper[0], Double.parseDouble(value[0]))) {
                                            buffList.put(k, v);
                                        }
                                    }
                                });
                            }
                            break;
                        case "active":
                            if(!value[0].isEmpty()) {
                                boolean boolValue = Boolean.parseBoolean(value[0]);
                                if (boolValue) {
                                    if (!buffList.isEmpty()) {
                                        if ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end &&a[i+1].equalsIgnoreCase("and"))){
                                            buffList.forEach((k, v) -> {
                                                Object mapValue = v.get("active");
                                                if (value[0].startsWith("null")) {
                                                    if (!(boolean) mapValue == value[0].equalsIgnoreCase("null")) {
                                                        buffList.remove(k);
                                                    }
                                                } else {
                                                    if (!(boolean) mapValue == value[0].equalsIgnoreCase("true")) {
                                                        buffList.remove(k);
                                                    }
                                                }
                                            });
                                        }
                                        else if ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end &&a[i+1].equalsIgnoreCase("or"))){
                                            buffList.forEach((k, v) -> {
                                                Object mapValue = v.get("active");
                                                if (value[0].startsWith("null")) {
                                                    if (!compareValues(mapValue, oper[0], Double.NaN)) {
                                                        buffList.put(k, v);
                                                    }
                                                } else {
                                                    if (!compareValues(mapValue, oper[0], Double.parseDouble(value[0]))) {
                                                        buffList.put(k, v);
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        data.forEach((k, v) -> {
                                            Object mapValue = v.get("active");
                                            if (!(boolean) mapValue == value[0].equalsIgnoreCase("true")) {
                                                buffList.put(k, v);
                                            }
                                        });
                                    }
                                }
                                else {
                                    throw new IllegalArgumentException();
                                }
                            }
                            else {
                                throw new NullPointerException();
                            }
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                }
                else {
                    throw new IllegalArgumentException();
                }
            }
        }
        return buffList;
    }
    private static String operator(String a) {
        String[] operators = {">=", "<=", "!=", "=", ">", "<", "like", "ilike"};

        for (String operator : operators) {
            if (a.startsWith(operator)) {
                return operator;
            }
        }
        return "";
    }
    private static boolean compareValues(Object mapValue, String operator, double value) {
        if (mapValue == null & (operator.equals(">") | operator.equals(">="))) return false;
        if (mapValue == null & (!operator.equals(">") & operator.equals("!="))) return false;
        if (mapValue == null & Double.isNaN(value)) return true;
        if (mapValue!=null) {
            if (mapValue instanceof Long) {
                long longValue = (Long) mapValue;
                double doubleValue = (double) longValue;
                return compareValues(doubleValue, operator, value);
            } else if (mapValue instanceof Short) {
                    long longValue = Long.parseLong(mapValue.toString());
                    double doubleValue = (double) longValue;
                    return compareValues(doubleValue, operator, value);
            } else if (mapValue instanceof Double) {
                if(!(operator.equals("=") & Double.isNaN(value))) {
                    double doubleValue = Double.parseDouble(mapValue.toString());
                    return switch (operator) {
                        case "=" -> doubleValue == value;
                        case ">" -> doubleValue > value;
                        case ">=" -> doubleValue >= value;
                        case "<" -> doubleValue < value;
                        case "<=" -> doubleValue <= value;
                        case "!=" -> doubleValue != value;
                        default -> false;
                    };
                }
            }
        }
        return false;
    }

    private static boolean compareString(Object mapValue, String operator, String value) {
        if (mapValue == null & (value==null & operator.equals("!="))) return false;
        if (mapValue == null & (value==null | (value.length()>0 & value.equals("null")))) return true;
        if (mapValue != null) {
            return switch (operator) {
                case "=" -> mapValue.toString().equals(value);
                case "!=" -> !mapValue.toString().equals(value);
                case "like" -> mapValue.toString().matches(value.replaceAll("%", ".*"));
                case "ilike" -> mapValue.toString().toLowerCase().matches(value.toLowerCase().replaceAll("%", ".*"));
                default -> false;
            };
        }
        return false;
    }
}
