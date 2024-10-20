package com.new_db.sql_processor;

import com.new_db.Database;
import com.new_db.Field;
import com.new_db.Record;
import com.new_db.Table;
import com.new_db.exceptions.TableNotFoundException;
import com.new_db.utils.InMemoryCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.helper.Helper.getInMemoryCriteria;

public class Select {
    public static void select(String[] tokens, Database database) {
        try {
            List<String> tokenList = Arrays.asList(tokens);
            QueryResult result = parseQuery(tokenList);
            try{
                Table table = database.getTable(result.getTableName());
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
                throw new TableNotFoundException("Table " + result.getTableName() + " does not exists");
            }
        } catch (IllegalArgumentException | TableNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static class QueryResult {
        private final List<String> columns;
        private final String tableName;
        private final List<String> whereConditions;

        public QueryResult(List<String> columns, String tableName, List<String> whereConditions) {
            this.columns = columns;
            this.tableName = tableName;
            this.whereConditions = whereConditions;
        }

        public List<String> getColumns() {
            return columns;
        }

        public String getTableName() {
            return tableName;
        }

        public List<String> getWhereConditions() {
            return whereConditions;
        }
    }

    public static QueryResult parseQuery(List<String> queryArray) {
        if (!queryArray.contains("FROM") && !queryArray.contains("from")) {
            throw new IllegalArgumentException("Отсутствует ключевое слово 'FROM'.");
        }

        int selectIndex = queryArray.indexOf("Select");
        int fromIndex = Math.max(queryArray.indexOf("FROM"), queryArray.indexOf("from"));
        List<String> columns = new ArrayList<>();

        for (int i = selectIndex + 1; i < fromIndex; i++) {
            String element = queryArray.get(i);
            if (!element.equals(",") && !element.isEmpty()) {
                columns.add(element.replaceAll(",$", ""));
            }
        }

        String tableName = queryArray.get(fromIndex + 1);

        List<String> whereConditions = new ArrayList<>();
        if (queryArray.contains("Where")) {
            int whereIndex = queryArray.indexOf("Where");

            for (int i = whereIndex + 1; i < queryArray.size(); i++) {
                String element = queryArray.get(i);
                if (!element.equals(",") && !element.isEmpty()) {
                    whereConditions.add(element);
                }
            }
        }
        return new QueryResult(columns, tableName, whereConditions);
    }
}
