package com.new_db.sql_processor;

import com.new_db.Database;
import com.new_db.Table;
import com.new_db.exceptions.TableNotFoundException;
import com.new_db.utils.InMemoryCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.helper.Helper.getInMemoryCriteria;
import static com.new_db.sql_processor.Select.parseQuery;

public class Delete {
    public static void delete(String[] tokens, Database database) {
        try {
            List<String> tokenList = Arrays.asList(tokens);
            Select.QueryResult result = parseQuery(tokenList);
            try{
                Table table = database.getTable(result.getTableName());
                InMemoryCriteria criteria = getInMemoryCriteria(result);
                table.queryRecords(criteria);

            } catch (TableNotFoundException e) {
                throw new TableNotFoundException("Table " + result.getTableName() + " does not exists");
            }
        } catch (IllegalArgumentException | TableNotFoundException e) {
             System.err.println(e.getMessage());
        }
    }
}
