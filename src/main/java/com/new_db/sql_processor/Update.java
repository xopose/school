package com.new_db.sql_processor;

import com.new_db.*;
import com.new_db.Record;
import com.new_db.exceptions.TableNotFoundException;
import com.new_db.utils.InMemoryCriteria;
import com.new_db.utils.QueryTokenParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.helper.Helper.getInMemoryCriteria;

public class Update {
    public static void update(String[] tokens, Database database) {
        try {
            List<String> tokenList = Arrays.asList(tokens);
            Table table = database.getTable(tokenList.get(1));
            getWhereCondition(tokenList);
            InMemoryCriteria criteria = getInMemoryCriteria(
                    new QueryTokenParser(
                            null, null, getWhereCondition(tokenList)
                    ));
            List<List<String>> parsedValues = Insert.getValues(extractValues(tokenList));
            for (Object[] entry : table.queryRecords(criteria)) {
                if(!parsedValues.isEmpty()){
                    Record record = new InMemoryRecord();
                    for (List<String> value: parsedValues){
                        record.setField(value.get(0), value.get(1));
                    }
                    table.addRecordById((long)entry[0], record);
                }

            }
        } catch (IllegalArgumentException | TableNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static List<String> getWhereCondition(List<String> tokenList) {
        List<String> whereConditions = new ArrayList<>();
        String query = String.join(" ", tokenList);
        if (Pattern.compile(Pattern.quote("where"), Pattern.CASE_INSENSITIVE).matcher(query).find()) {
            int whereIndex = -1;
            for (int i = 0; i < tokenList.size(); i++) {

                if (tokenList.get(i).equalsIgnoreCase("where")) {
                    whereIndex = i;
                    break;
                }
            }
            if (whereIndex != -1) {
                for (int i = whereIndex + 1; i < tokenList.size(); i++) {
                    String element = tokenList.get(i);
                    if (!element.equals(",") && !element.isEmpty()) {
                        whereConditions.add(element);
                    }
                }
            }
        }
        return whereConditions;
    }

    private static List<String> extractValues(List<String> queryArray) {
        int setIndex = -1;
        int whereIndex = queryArray.size();

        for (int i = 0; i < queryArray.size(); i++) {
            if (queryArray.get(i).equalsIgnoreCase("set")) {
                setIndex = i;
                break;
            }
        }

        for (int i = setIndex + 1; i < queryArray.size(); i++) {
            if (queryArray.get(i).equalsIgnoreCase("where")) {
                whereIndex = i;
                break;
            }
        }
        if (setIndex != -1 && setIndex + 1 < whereIndex) {
            return queryArray.subList(setIndex + 1, whereIndex);
        } else {
            return new ArrayList<>();
        }
    }
}
