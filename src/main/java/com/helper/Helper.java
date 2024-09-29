package com.helper;

import java.util.*;
import java.util.stream.Stream;

public class Helper {
    public static void printFormatedTable(Map<Long, Map<String, Object>> data){
        HashSet<String> keysWithLength = new HashSet<>();
        int maxLength = 0;
        for (Map.Entry<Long, Map<String, Object>> entry : data.entrySet()) {
            Map<String, Object> innerMap = entry.getValue();
            for (Map.Entry<String, Object> innerEntry : innerMap.entrySet()) {
                int valLength = getObjectLength(innerEntry.getValue());
                maxLength = Math.max(maxLength, valLength);
                keysWithLength.add(innerEntry.getKey());
            }
        }
        printTable(data, keysWithLength, maxLength);
    }

    public static void printTable(Map<Long, Map<String, Object>> data, HashSet<String> keys, int max) {
        // Печать заголовков таблицы
        for (String key : keys) {
            System.out.print("|" + " ".repeat(1+(max - key.length()) / 2) + key + " ".repeat(1+(max - key.length()) / 2));
        }
        System.out.println("|");

        // Разделитель между заголовками и данными
        System.out.println("-".repeat(max * (keys.size()) + keys.size() + 1));

        // Печать данных таблицы
        data.forEach((id, value) -> {
            System.out.print("|");
            keys.forEach(k -> {
                Object v = value.get(k); // Получение значения по ключу
                if (v == null) v = "";   // Если значение null, то заменяем на пустую строку
                String valString = v.toString();
                System.out.print(" " + valString + " ".repeat(max - valString.length()) + "|");
            });
            System.out.println(); // Переход на новую строку после каждой строки данных
        });
    }

    private static int getObjectLength(Object value) {
        if (value instanceof String) {
            return ((String) value).length();
        } else if (value instanceof List) {
            int max = 0;
            for (Object obj : (List<?>) value) {
                max = Math.max(max, obj.toString().length());
            }
            return max;
        } else if (value instanceof int[]) {
            return ((int[]) value).length;
        }
        // Для других типов можно добавить обработку
        return 0;
    }

}
