package com.digdes.school;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String... args) throws Exception {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            //Вставка строки в коллекцию
            List<Map<String,Object>> result1 = starter.execute("INSERT VALUES 'lastName' = 'aaaaaaaaaaaaaaaaaaaaa', 'id'= 7, 'age'=40, 'active'=true, 'cost'=86.4");
            List<Map<String,Object>> result12 = starter.execute("INSERT VALUES 'lastName' = 'Федоров', 'id'=7, 'age'=40, 'active'=true, 'cost'=86.4");
            List<Map<String,Object>> result22323 = starter.execute("INSERT VALUES 'lastName' = 'Федороввввв', 'id'=335463, 'age'=43330, 'active'=false, 'cost'=40");
            List<Map<String,Object>> result223 = starter.execute("INSERT VALUES 'lastName' = 'Федоровмвыфяив', 'id'=335463, 'age'=43330, 'active'=false, 'cost'=60");
            List<Map<String,Object>> result22443 = starter.execute("INSERT VALUES 'lastName' = 'esvaesvds', 'id'=5, 'age'=31131, 'active'=true, 'cost'=152");
            List<Map<String,Object>> result2236 = starter.execute("INSERT VALUES 'id'=5");

            List<Map<String,Object>> result32 = starter.execute("Update values 'id'=1, 'lastname'='аааааа', 'age'=1, 'cost'=666.66, 'active'=true where 'age'!=null");
            List<Map<String,Object>> result3 = starter.execute("SELECT where 'id'!=null");
            for (Map<String, Object> stringObjectMap : result3) {
                System.out.println(stringObjectMap.keySet() + " " + stringObjectMap.values());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
