package com.new_db;

import com.new_db.exceptions.CantCreateDatabaseException;
import com.new_db.exceptions.IncorrectCommandException;
import com.new_db.exceptions.TableNotFoundException;
import com.new_db.sql_processor.Starter;
import com.new_db.utils.InMemoryCriteria;
import com.new_db.utils.Transaction;

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws TableNotFoundException, CantCreateDatabaseException, IncorrectCommandException {
        Database database = new InMemoryDatabase();
        Starter starter = new Starter(database);
        starter.execute("CREATE table iHateDatabase");
        Table table = database.getTable("iHateDatabase");

        // Работа с записью
        Record record1 = new InMemoryRecord();
        record1.setField("name", "Alice");
        record1.setField("age", 30);

        Record record2 = new InMemoryRecord();
        record2.setField("name", "Bob");
        record2.setField("age", 25);

        table.addRecord(table.nextRecordId(), record1);
        table.addRecord(table.nextRecordId(), record2);
        starter.execute("Select name, age FROM iHateDatabase Where age=1");
        System.out.println("-".repeat(100));
        table.createIndex("name");

        InMemoryCriteria criteria = new InMemoryCriteria();
        criteria.equals("name", "Alice");

        System.out.println("Результаты запроса:");
        starter.execute("Select * from iHateDatabase");
        System.out.println("-".repeat(100));

        table.queryRecords(criteria).forEach(r -> System.out.println(r.serialize(new ArrayList<>())));

        System.out.println("Сумма возрастов: " + table.sumField("age"));
        System.out.println("Средний возраст: " + table.averageField("age"));

        // Работа с транзакциями
        Transaction transaction = table.beginTransaction();
        Record record3 = new InMemoryRecord();
        record3.setField("name", "Charlie");
        record3.setField("age", 28);
        transaction.addRecord(table.nextRecordId(), record3);
        transaction.commit();

        System.out.println("После транзакции:");
        System.out.println("Сумма возрастов: " + table.sumField("age"));
        System.out.println("Средний возраст: " + table.averageField("age"));
        table.queryRecords(new InMemoryCriteria()).forEach(r -> System.out.println(r.serialize(new ArrayList<>())));
    }
}