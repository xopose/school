package com.new_db.sql_processor;

import com.new_db.Database;
import com.new_db.exceptions.CantCreateDatabaseException;
import com.new_db.exceptions.IncorrectCommandException;
import com.new_db.exceptions.TableNotFoundException;

import java.util.Arrays;

public class Create {

    public static void create(String[] tokens, Database database) throws IncorrectCommandException, CantCreateDatabaseException {
        switch (tokens[1]){
            case ("table"):
                try{
                    database.getTable(tokens[2]);
                }
                catch (TableNotFoundException e){
                    database.createTable(tokens[2]);
                }
                break;
            default:
                throw new IncorrectCommandException("Invalid command syntax: " + String.join(" ", tokens));
        }
    }
}
