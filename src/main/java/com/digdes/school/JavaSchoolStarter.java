package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {
    private final List<Map<String, Object>> data = new ArrayList<>();

    //Дефолтный конструктор
    public JavaSchoolStarter() {

    }

    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String, Object>> execute(String request) throws Exception {
        String[] tokens = request.trim().split("(?=([^\"]*\"[^\"]*\")*[^\"]*$)\\s+");
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
        if (notNull) {
            List<Map<String, Object>> result = new ArrayList<>();
            result.add(row);
            data.add(row);
            return data;
        }
        else throw new NoSuchElementException();
    }

    private List<Map<String, Object>> update(String[] tokens) throws Exception {
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
            List<Map<String, Object>> whereList = where(keys);
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

    private List<Map<String, Object>> delete(String[] tokens) throws Exception {
        if (tokens.length > 1 && !tokens[1].equalsIgnoreCase("where")) {
            throw new Exception("Invalid command syntax: " + String.join(" ", tokens));
        }

        else if (tokens.length > 1 && tokens[1].equalsIgnoreCase("where")) {
            String[] keys = Arrays.copyOfRange(tokens, 2, tokens.length);
            data.removeAll(where(keys));
        }

        else { data.clear(); }
        return data;
    }

    private List<Map<String, Object>> select(String[] tokens) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        if (tokens.length > 1 && !tokens[1].equalsIgnoreCase("where")) {
            throw new Exception("Invalid command syntax: " + String.join(" ", tokens));
        }

        else if (tokens.length > 1 && tokens[1].equalsIgnoreCase("where")) {
            String[] keys = Arrays.copyOfRange(tokens, 2, tokens.length);
            result.addAll(where(keys));
        }

        else { result.addAll(data); }
        return result;
    }
    private List<Map<String, Object>> where(String[] tokens) {
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
    private boolean compareValues(Object mapValue, String operator, double value) {
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

    private boolean compareString(Object mapValue, String operator, String value) {
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
