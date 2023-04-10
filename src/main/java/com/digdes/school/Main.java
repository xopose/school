package com.digdes.school;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String... args) throws Exception {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            //Вставка строки в коллекцию
            List<Map<String,Object>> result1 = starter.execute("INSERT VALUES 'lastName' = 'Фед оров', 'id'=3, 'age'=40, 'active'=true, 'cost'=86.4");
            List<Map<String,Object>> result22323 = starter.execute("INSERT VALUES 'lastName' = 'Федороввввв', 'id'=335463, 'age'=43330, 'active'=false, 'cost'=40");
            List<Map<String,Object>> result223 = starter.execute("INSERT VALUES 'lastName' = 'Федоромвыфяив', 'id'=335463, 'age'=43330, 'active'=false, 'cost'=60");
            //System.out.println(result1.get(0).keySet() + " " + result1.get(0).values());
            //Изменение значения которое выше записывали
            //List<Map<String,Object>> result2 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3");
            //Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
            //and 'lastName' like 'Федоров'
            List<Map<String,Object>> result3 = starter.execute("SELECT WHERE 'id'>=1 or 'cost'>=50 and 'cost'<100");
            for (Map<String, Object> stringObjectMap : result3) {
                System.out.println(stringObjectMap.keySet() + " " + stringObjectMap.values());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
