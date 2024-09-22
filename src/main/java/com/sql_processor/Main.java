package com.sql_processor;
import java.util.Map;

public class Main {

    public static void main(String... args) throws Exception {
        Starter starter = new Starter();
        try {
            //Вставка строки в коллекцию
            starter.execute("INSERT VALUES 'lastName' = 'aaaaaaaaaaaaaaaaaaaaa', 'age'=40, 'active'=true, 'cost'=86.4");
            starter.execute("INSERT VALUES 'lastName' = 'Федоров', 'age'=40, 'active'=true, 'cost'=86.4");
            starter.execute("INSERT VALUES 'lastName' = 'Федороввввв',  'age'=100, 'active'=false, 'cost'=40");
            starter.execute("INSERT VALUES 'lastName' = 'Федоровмвыфяив', 'age'=255, 'active'=false, 'cost'=60");
            starter.execute("INSERT VALUES 'lastName' = 'esvaesvds', 'age'=255, 'active'=true, 'cost'=152");

            starter.execute("Delete where 'age'=255");
            starter.execute("Update set 'lastName' = 'aaaaa', 'age ' = 11, 'cost'=99999, 'active'=false where 'lastName' = 'Федоров'");
            Map<Long, Map<String, Object>> result3 = starter.execute("SELECT where 'age' > 0 ");
            result3.forEach((k, v) -> {
                System.out.println(k + " " + v.entrySet());
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
