package com.digdes.school;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String... args) throws Exception {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            //Вставка строки в коллекцию
            List<Map<String,Object>> result1 = starter.execute("INSERT VALUES 'lastName' = 'Федоров', 'id'=3, 'age'=40, 'active'=true");
            for (Map<String, Object> stringObjectMap : result1) {
                System.out.println(stringObjectMap.keySet() + " " + stringObjectMap.values());
            }
            List<Map<String,Object>> result22323 = starter.execute("INSERT VALUES 'lastName' = 'Федороввввв', 'id'=335463, 'age'=43330, 'active'=true");
            //System.out.println(result1.get(0).keySet() + " " + result1.get(0).values());
            //Изменение значения которое выше записывали
            //List<Map<String,Object>> result2 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3");
            //Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
            List<Map<String,Object>> result3 = starter.execute("SELECT WHERE 'age'>=40 and 'lastName' = 'Федоров'");
            for (Map<String, Object> stringObjectMap : result3) {
                System.out.println(stringObjectMap.keySet() + " " + stringObjectMap.values());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}