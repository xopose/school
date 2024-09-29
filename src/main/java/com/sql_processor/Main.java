package com.sql_processor;
import com.helper.Helper;

import java.util.Map;

public class Main {

    public static void main(String... args) throws Exception {
        Starter starter = new Starter();
        try {
            //Вставка строки в коллекцию
            starter.execute("INSERT VALUES 'lastName' = 'Иванов', 'age'=18, 'active'=true, 'cost'=86.4");
            starter.execute("INSERT VALUES 'lastName' = 'Федоров', 'age'=30, 'active'=true, 'cost'=86.4");
            starter.execute("INSERT VALUES 'lastName' = 'Яблонский',  'age'=38, 'active'=false, 'cost'=40");
            starter.execute("INSERT VALUES 'lastName' = 'Васильев', 'age'=49, 'active'=false, 'cost'=60");
            starter.execute("INSERT VALUES 'lastName' = 'Остапов', 'age'=65, 'active'=true, 'cost'=152");

            starter.execute("Delete where 'age'=255");
            starter.execute("Update set 'lastName' = 'АНОНИМ', 'age ' = 11, 'cost'=99999, 'active'=false where 'lastName' = 'Федоров'");
            Map<Long, Map<String, Object>> result = starter.execute("SELECT where 'age' > 0 ");
//            result.forEach((k, v) -> {
//                System.out.println(k + " " + v.entrySet());
//            });
            Helper.printFormatedTable(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
