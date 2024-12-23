package com.new_db.sql_processor;

import com.new_db.Database;
import com.new_db.Table;
import com.new_db.exceptions.IncorrectCommandException;
import com.new_db.exceptions.TableNotFoundException;
import com.new_db.utils.InMemoryCriteria;
import com.new_db.utils.QueryTokenParser;

import java.util.Arrays;
import java.util.List;

import static com.helper.Helper.getInMemoryCriteria;
import static com.new_db.utils.QueryTokenParser.parseQuery;

public class Delete {
    public static void deleteFrom(String[] tokens, Database database) {
        try {
            List<String> tokenList = Arrays.asList(tokens);
            QueryTokenParser result = parseQuery(tokenList);

            try{
                Table table = database.getTable(result.tableName());
                InMemoryCriteria criteria = getInMemoryCriteria(result);
                for (Object[] entry : table.queryRecords(criteria)) {
                    table.deleteRecord((long)entry[0]);
                }
            } catch (TableNotFoundException e) {
                throw new TableNotFoundException("Table " + result.tableName() + " does not exists");
            }
        } catch (IllegalArgumentException | TableNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void deleteTable(String tableName, Database database) throws TableNotFoundException {
            database.getTable(tableName);
            database.deleteTable(tableName);
    }

    public static void delete(String[] tokens, Database database) throws IncorrectCommandException, TableNotFoundException {
        String command = tokens[1].toUpperCase();
        switch (command) {
            case "TABLE" -> deleteTable(tokens[2], database);
            case "FROM" -> deleteFrom(tokens, database);
            default -> throw new IncorrectCommandException("Unknown command: " + command);
        };
    }
}
