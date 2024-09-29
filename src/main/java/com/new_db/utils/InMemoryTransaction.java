package com.new_db.utils;

import com.new_db.Database;
import com.new_db.Record;
import com.new_db.exceptions.RecordNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class InMemoryTransaction implements Transaction {
    private Database database;
    private Map<Long, Record> addedRecords;
    private Map<Long, Record> updatedRecords;
    private Map<Long, Record> deletedRecords;
    private boolean active;

    public InMemoryTransaction(Database database) {
        this.database = database;
        this.addedRecords = new HashMap<>();
        this.updatedRecords = new HashMap<>();
        this.deletedRecords = new HashMap<>();
        this.active = true;
    }

    @Override
    public void addRecord(long id, Record record) {
        if (!active) throw new IllegalStateException("Transaction is not active");
        addedRecords.put(id, record);
    }

    @Override
    public void updateRecord(long id, Record record) throws RecordNotFoundException {
        if (!active) throw new IllegalStateException("Transaction is not active");
        Record existing = database.getRecord(id);
        if (existing == null) {
            throw new RecordNotFoundException("Record with ID " + id + " not found.");
        }
        updatedRecords.put(id, record);
    }

    @Override
    public boolean deleteRecord(long id) {
        if (!active) throw new IllegalStateException("Transaction is not active");
        Record existing = database.getRecord(id);
        if (existing != null) {
            deletedRecords.put(id, existing);
            return true;
        }
        return false;
    }

    @Override
    public void commit() {
        if (!active) throw new IllegalStateException("Transaction is not active");

        for (Map.Entry<Long, Record> entry : addedRecords.entrySet()) {
            database.addRecord(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Long, Record> entry : updatedRecords.entrySet()) {
            try {
                database.updateRecord(entry.getKey(), entry.getValue());
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }
        }

        for (Long id : deletedRecords.keySet()) {
            database.deleteRecord(id);
        }
        active = false;
    }

    @Override
    public void rollback() {
        if (!active) throw new IllegalStateException("Transaction is not active");

        addedRecords.clear();
        updatedRecords.clear();
        deletedRecords.clear();
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}