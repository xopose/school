package com.new_db.sql_processor;

import com.new_db.Database;
import com.new_db.InMemoryRecord;
import com.new_db.Record;
import com.new_db.Table;
import com.new_db.exceptions.TableNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Insert {
    public static void insert(String[] tokens, Database database) {
        try {
            List<String> tokenList = Arrays.asList(tokens);
            try{
                Table table = database.getTable(tokenList.get(2));
                if(tokenList.get(3).equalsIgnoreCase("values")){
                    List<String> values = tokenList.subList(4, tokenList.size());
                    List<List<String>> result = getValues(values);

                    if(!result.isEmpty()){
                        Record record = new InMemoryRecord();
                        for (List<String> value: result){
                            record.setField(value.get(0), value.get(1));
                        }
                        table.addRecord(record);
                    }
                }
            } catch (TableNotFoundException e){
                System.out.println(e);
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static List<List<String>> getValues(List<String> values) {
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i += 2) {
            String first = values.get(i).replaceAll("[(),]", "").trim();
            String second = values.get(i + 1).replaceAll("[(),]", "").trim();
            List<String> pair = List.of(first, second);
            result.add(pair);
        }
        return result;
    }
}
