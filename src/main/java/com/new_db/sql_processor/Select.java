package com.new_db.sql_processor;

import com.new_db.Database;
import com.new_db.Field;
import com.new_db.Record;
import com.new_db.Table;
import com.new_db.exceptions.TableNotFoundException;
import com.new_db.utils.InMemoryCriteria;
import com.new_db.utils.QueryTokenParser;

import java.util.Arrays;
import java.util.List;

import static com.helper.Helper.getInMemoryCriteria;
import static com.new_db.utils.QueryTokenParser.parseQuery;

public class Select {
    public static void select(String[] tokens, Database database) {
        try {
            List<String> tokenList = Arrays.asList(tokens);
            QueryTokenParser result = parseQuery(tokenList);
            try{
                Table table = database.getTable(result.tableName());
                InMemoryCriteria criteria = getInMemoryCriteria(result);
                for (Object[] entry : table.queryRecords(criteria)) {
                    long id = (long)entry[0];
                    Record record = (Record)entry[1];
                    System.out.println("Index: " + id + ", Record: ");
                    for(Field field: record.getFields().values()){
                        System.out.println("\t" +field.getName()+ " " +field.getValue());
                    }
                }
            } catch (TableNotFoundException e) {
                throw new TableNotFoundException("Table " + result.tableName() + " does not exists");
            }
        } catch (IllegalArgumentException | TableNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
