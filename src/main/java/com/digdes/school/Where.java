package com.digdes.school;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Where {

    public static List<Map<String, Object>> where(String[] tokens, List<Map<String, Object>> data) {
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
        List<Map<String, Object>> buffList = new ArrayList<>();
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
                    String value;
                    String oper = "";
                    String strOper = "";
                    int firstQuoteIndex = a[i].indexOf("'") + 1;
                    int secondQuoteIndex = a[i].indexOf("'", firstQuoteIndex + 1);
                    sub = a[i].substring(firstQuoteIndex, secondQuoteIndex);
                    if (sub.replace(" ", "").equalsIgnoreCase("lastName")) {
                        int thirdQuoteIndex = a[i].indexOf("'", secondQuoteIndex + 1);
                        int fourthQuoteIndex = a[i].lastIndexOf("'");
                        value = a[i].substring(thirdQuoteIndex + 1, fourthQuoteIndex);
                        strOper = a[i].substring(secondQuoteIndex+1, thirdQuoteIndex).strip();
                    } else {
                        value = a[i].substring(secondQuoteIndex + 1);
                        if (operator(value).length() > 0) {
                            oper=operator(value);
                            value = value.substring(operator(value).length());
                        }
                    }
                    switch (sub.toLowerCase().replace(" ", "")) {
                        case "id":
                            if (value.length()==0){
                                value="null";
                            }
                            if (buffList.size() > 0) {
                                if ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end &&a[i+1].equalsIgnoreCase("and"))){
                                    Iterator<Map<String, Object>> iterator = buffList.iterator();
                                    while (iterator.hasNext()) {
                                        Map<String, Object> map = iterator.next();
                                        Object mapValue = map.get("id");
                                        if (value.startsWith("null")) {
                                            if (!compareValues(mapValue, oper, Double.NaN)) {
                                                iterator.remove();
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper, Long.parseLong(value))) {
                                                iterator.remove();
                                            }
                                        }
                                    }
                                }
                                else if ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end &&a[i+1].equalsIgnoreCase("or"))){
                                    List<Map<String, Object>> compareList = new ArrayList<>();
                                    for (Map<String, Object> map : buffList) {
                                        Object mapValue = map.get("id");
                                        if (value.startsWith("null")) {
                                            if (!compareValues(mapValue, oper, Double.NaN)) {
                                                compareList.add(map);
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper, Long.parseLong(value))) {
                                                compareList.add(map);
                                            }
                                        }
                                    }
                                    buffList.removeAll(compareList);
                                    buffList.addAll(compareList.stream().filter(item -> !buffList.contains(item)).toList());
                                    compareList.clear();
                                }

                            }

                            else {
                                for (Map<String, Object> map : data) {
                                    Object mapValue = map.get("id");
                                    if (value.startsWith("null")){
                                        if (compareValues(mapValue, oper, Double.NaN)) {
                                            buffList.add(map);
                                        }
                                    }
                                    else {
                                        if (compareValues(mapValue, oper, Long.parseLong(value))) {
                                            buffList.add(map);
                                        }
                                    }
                                }
                            }
                            break;
                        case "lastname":
                            if(value.length()>0 && (value.matches("^.*[a-zA-Z ]*.*$") | value.matches("^.*[а-яА-Я ]*.*$"))) {
                                if (buffList.size() > 0) {
                                    if ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end &&a[i+1].equalsIgnoreCase("and"))){
                                        Iterator<Map<String, Object>> iterator = buffList.iterator();
                                        while (iterator.hasNext()) {
                                            Map<String, Object> map = iterator.next();
                                            Object mapValue = map.get("lastName");
                                            if (!compareString(mapValue, strOper, value)) {
                                                iterator.remove();
                                            }
                                        }
                                    }
                                    else if ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end &&a[i+1].equalsIgnoreCase("or"))){
                                        List<Map<String, Object>> compareList = new ArrayList<>();
                                        for (Map<String, Object> map : buffList) {
                                            Object mapValue = map.get("lastName");
                                            if (value.startsWith("null")) {
                                                if (!compareString(mapValue, strOper, value)) {
                                                    compareList.add(map);
                                                }
                                            } else {
                                                if (!compareString(mapValue, strOper, value)) {
                                                    compareList.add(map);
                                                }
                                            }
                                        }
                                        buffList.removeAll(compareList);
                                        buffList.addAll(compareList.stream().filter(item -> !buffList.contains(item)).toList());
                                        compareList.clear();
                                    }
                                }

                                else {
                                    for (Map<String, Object> map : data) {
                                        Object mapValue = map.get("lastName");
                                        if (compareString(mapValue, strOper, value)) {
                                            buffList.add(map);
                                        }
                                    }
                                }
                            }
                            else {
                                throw new NullPointerException();
                            }

                            break;
                        case "age":
                            if (value.length()==0){
                                value="null";
                            }
                            if (buffList.size() > 0) {
                                if ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end &&a[i+1].equalsIgnoreCase("and"))){
                                    Iterator<Map<String, Object>> iterator = buffList.iterator();
                                    while (iterator.hasNext()) {
                                        Map<String, Object> map = iterator.next();
                                        Object mapValue = map.get("age");
                                        if (value.startsWith("null")) {
                                            if (!compareValues(mapValue, oper, Double.NaN)) {
                                                iterator.remove();
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper, Long.parseLong(value))) {
                                                iterator.remove();
                                            }
                                        }
                                    }
                                }
                                else if ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end &&a[i+1].equalsIgnoreCase("or"))){
                                    List<Map<String, Object>> compareList = new ArrayList<>();
                                    for (Map<String, Object> map : buffList) {
                                        Object mapValue = map.get("age");
                                        if (value.startsWith("null")) {
                                            if (!compareValues(mapValue, oper, Double.NaN)) {
                                                compareList.add(map);
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper, Long.parseLong(value))) {
                                                compareList.add(map);
                                            }
                                        }
                                    }
                                    buffList.removeAll(compareList);
                                    buffList.addAll(compareList.stream().filter(item -> !buffList.contains(item)).toList());
                                    compareList.clear();
                                }
                            }

                            else {
                                for (Map<String, Object> map : data) {
                                    Object mapValue = map.get("age");
                                    if (value.startsWith("null")){
                                        if (compareValues(mapValue, oper, Double.NaN)) {
                                            buffList.add(map);
                                        }
                                    }
                                    else {
                                        if (compareValues(mapValue, oper, Long.parseLong(value))) {
                                            buffList.add(map);
                                        }
                                    }
                                }
                            }
                            break;
                        case "cost":
                            if (value.length()==0){
                                value="null";
                            }
                            if (buffList.size() > 0) {
                                if ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end &&a[i+1].equalsIgnoreCase("and"))){
                                    Iterator<Map<String, Object>> iterator = buffList.iterator();
                                    while (iterator.hasNext()) {
                                        Map<String, Object> map = iterator.next();
                                        Object mapValue = map.get("cost");
                                        if (value.startsWith("null")) {
                                            if (!compareValues(mapValue, oper, Double.NaN)) {
                                                iterator.remove();
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper, Double.parseDouble(value))) {
                                                iterator.remove();
                                            }
                                        }
                                    }
                                }
                                else if ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end &&a[i+1].equalsIgnoreCase("or"))){
                                    List<Map<String, Object>> compareList = new ArrayList<>();
                                    for (Map<String, Object> map : buffList) {
                                        Object mapValue = map.get("cost");
                                        if (value.startsWith("null")) {
                                            if (!compareValues(mapValue, oper, Double.NaN)) {
                                                compareList.add(map);
                                            }
                                        } else {
                                            if (!compareValues(mapValue, oper, Double.parseDouble(value))) {
                                                compareList.add(map);
                                            }
                                        }
                                    }
                                    buffList.removeAll(compareList);
                                    buffList.addAll(compareList.stream().filter(item -> !buffList.contains(item)).toList());
                                    compareList.clear();
                                }
                            }

                            else {
                                for (Map<String, Object> map : data) {
                                    Object mapValue = map.get("age");
                                    if (value.startsWith("null")){
                                        if (compareValues(mapValue, oper, Double.NaN)) {
                                            buffList.add(map);
                                        }
                                    }
                                    else {
                                        if (compareValues(mapValue, oper, Double.parseDouble(value))) {
                                            buffList.add(map);
                                        }
                                    }
                                }
                            }
                            break;
                        case "active":
                            if(value.length()>0) {
                                boolean boolValue = value.equalsIgnoreCase("true") | value.equalsIgnoreCase("false");
                                if (boolValue) {
                                    if (buffList.size() > 0) {
                                        if ((flag_end && a[a.length-2].equalsIgnoreCase("and")) || (!flag_end &&a[i+1].equalsIgnoreCase("and"))){
                                            Iterator<Map<String, Object>> iterator = buffList.iterator();
                                            while (iterator.hasNext()) {
                                                Map<String, Object> map = iterator.next();
                                                Object mapValue = map.get("active");
                                                if (value.startsWith("null")) {
                                                    if (!(boolean) mapValue == value.equalsIgnoreCase("null")) {
                                                        iterator.remove();
                                                    }
                                                } else {
                                                    if (!(boolean) mapValue == value.equalsIgnoreCase("true")) {
                                                        iterator.remove();
                                                    }
                                                }
                                            }
                                        }
                                        else if ((flag_end && a[a.length-2].equalsIgnoreCase("or")) || (!flag_end &&a[i+1].equalsIgnoreCase("or"))){
                                            List<Map<String, Object>> compareList = new ArrayList<>();
                                            for (Map<String, Object> map : buffList) {
                                                Object mapValue = map.get("cost");
                                                if (value.startsWith("null")) {
                                                    if (!compareValues(mapValue, oper, Double.NaN)) {
                                                        compareList.add(map);
                                                    }
                                                } else {
                                                    if (!compareValues(mapValue, oper, Double.parseDouble(value))) {
                                                        compareList.add(map);
                                                    }
                                                }
                                            }
                                            buffList.removeAll(compareList);
                                            buffList.addAll(compareList.stream().filter(item -> !buffList.contains(item)).toList());
                                            compareList.clear();
                                        }
                                    } else {
                                        for (Map<String, Object> map : data) {
                                            Object mapValue = map.get("active");
                                            if (!(boolean) mapValue == value.equalsIgnoreCase("true")) {
                                                buffList.add(map);
                                            }
                                        }
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
    private static String operator(String a){
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
    private static boolean compareValues(Object mapValue, String operator, double value) {
        if (mapValue == null & (operator.equals(">") | operator.equals(">="))) return false;
        if (mapValue == null & (!operator.equals(">") & operator.equals("!="))) return false;
        if (mapValue == null & Double.isNaN(value)) return true;
        if (mapValue!=null) {
            if (mapValue instanceof Long) {
                long longValue = (Long) mapValue;
                double doubleValue = (double) longValue;
                return compareValues(doubleValue, operator, value);
            } else if (mapValue instanceof Double) {
                double doubleValue = (Double) mapValue;
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
