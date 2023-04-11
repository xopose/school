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
            case "SELECT" -> Select.select(tokens, data);
            case "INSERT" -> Insert.insert(tokens, data);
            case "UPDATE" -> Update.update(tokens, data);
            case "DELETE" -> Delete.delete(tokens, data);
            default -> throw new Exception("Unknown command: " + command);
        };

    }







}
