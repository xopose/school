package com.new_db.sql_processor;

import com.new_db.Database;
import com.new_db.exceptions.CantCreateDatabaseException;
import com.new_db.exceptions.IncorrectCommandException;
import com.new_db.exceptions.TableNotFoundException;

public class Starter {
    Database database;

    public Starter(Database database){
        this.database = database;
    }
    public void execute(String request) throws IncorrectCommandException, CantCreateDatabaseException, TableNotFoundException {
        String[] tokens = request.trim().split("(?=([^\"]*\"[^\"]*\")*[^\"]*$)\\s+");
        if (tokens.length == 0) {
            throw new IncorrectCommandException("Empty command");
        }
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].replace("’", "'").replace("‘", "'");
        }
        String command = tokens[0].toUpperCase();
        switch (command) {
            case "SELECT" -> Select.select(tokens, database);
            case "INSERT" -> Insert.insert(tokens, database);
            case "UPDATE" -> Update.update(tokens, database);
            case "DELETE" -> Delete.delete(tokens, database);
            case "CREATE" -> Create.create(tokens, database);
            default -> throw new IncorrectCommandException("Unknown command: " + command);
        };
    }
}
