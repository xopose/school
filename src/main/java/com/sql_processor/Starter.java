package com.sql_processor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Starter {
    private final Map<Long, Map<String, Object>> data = new ConcurrentHashMap<>();
    //Дефолтный конструктор
    public Starter() {

    }

    //На вход запрос, на выход результат выполнения запроса
    public Map<Long,Map<String, Object>> execute(String request) throws Exception {
        String[] tokens = request.trim().split("(?=([^\"]*\"[^\"]*\")*[^\"]*$)\\s+");
        if (tokens.length == 0) {
            throw new Exception("Empty command");
        }
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].replace("’", "'").replace("‘", "'");
        }
        String command = tokens[0].toUpperCase();
        return switch (command) {
            case "SELECT" -> Select.select(tokens, data);
            case "INSERT" -> Insert.insert(tokens, data);
            case "UPDATE" -> Update.update(tokens, data);
            case "DELETE" -> Delete.delete(tokens, data);
            default -> throw new Exception("Unknown command: " + command);
        };

    }
}
