package com.new_db;

import com.new_db.exceptions.RecordNotFoundException;
import com.new_db.utils.InMemoryCriteria;
import com.new_db.utils.Transaction;

public class Main {
    public static void main(String[] args) {
        Database db = new InMemoryDatabase();

        // Работа с записью
        Record record1 = new InMemoryRecord();
        record1.setField("name", "Alice");
        record1.setField("age", 30);

        Record record2 = new InMemoryRecord();
        record2.setField("name", "Bob");
        record2.setField("age", 25);

        db.addRecord(db.nextRecordId(), record1);
        db.addRecord(db.nextRecordId(), record2);

        db.createIndex("name");

        InMemoryCriteria criteria = new InMemoryCriteria();
        criteria.equals("name", "Alice");

        System.out.println("Результаты запроса:");
        db.queryRecords(criteria).forEach(r -> System.out.println(r.serialize()));

        System.out.println("Сумма возрастов: " + db.sumField("age"));
        System.out.println("Средний возраст: " + db.averageField("age"));

        // Работа с транзакциями
        Transaction transaction = db.beginTransaction();
        Record record3 = new InMemoryRecord();
        record3.setField("name", "Charlie");
        record3.setField("age", 28);
        transaction.addRecord(db.nextRecordId(), record3);
        transaction.commit();

        System.out.println("После транзакции:");
        System.out.println("Сумма возрастов: " + db.sumField("age"));
        System.out.println("Средний возраст: " + db.averageField("age"));
        db.queryRecords(new InMemoryCriteria()).forEach(r -> System.out.println(r.serialize()));
    }
}