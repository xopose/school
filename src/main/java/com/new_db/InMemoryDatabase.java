package com.new_db;

import com.new_db.exceptions.RecordNotFoundException;
import com.new_db.utils.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryDatabase implements Database {
    private Map<Long, Record> records = new ConcurrentHashMap<>();
    private Map<String, Index> indexes = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public void addRecord(long id, Record record) {
        records.put(id, record);
        indexes.values().forEach(index -> index.indexRecord(record));
    }

    @Override
    public Record getRecord(long id) {
        return records.get(id);
    }

    @Override
    public void updateRecord(long id, Record record) throws RecordNotFoundException {
        Record oldRecord = records.get(id);
        if (oldRecord == null) {
            throw new RecordNotFoundException("Record with ID " + id + " not found.");
        }
        records.put(id, record);
        indexes.values().forEach(index -> index.updateRecord(oldRecord, record));
    }

    @Override
    public boolean deleteRecord(long id) {
        Record removed = records.remove(id);
        if (removed != null) {
            indexes.values().forEach(index -> index.removeRecord(removed));
            return true;
        }
        return false;
    }

    @Override
    public Collection<Record> queryRecords(Criteria criteria) {
        if (criteria instanceof InMemoryCriteria) {
            InMemoryCriteria inMemCriteria = (InMemoryCriteria) criteria;
            for (InMemoryCriteria.Condition condition : inMemCriteria.getConditions()) {
                if ((condition.getOperator() == InMemoryCriteria.Operator.EQUALS ||
                        condition.getOperator() == InMemoryCriteria.Operator.IN) &&
                        indexes.containsKey(condition.getFieldName())) {
                    Index index = indexes.get(condition.getFieldName());
                    Collection<Record> indexedRecords = index.search(condition.getValue());
                    return indexedRecords.stream()
                            .filter(criteria::matches)
                            .collect(Collectors.toList());
                }
            }
        }
        // Если индексы не применимы, выполнить полный перебор
        return records.values().stream()
                .filter(criteria::matches)
                .collect(Collectors.toList());
    }

    @Override
    public Number sumField(String fieldName) {
        return records.values().stream()
                .map(record -> record.getField(fieldName))
                .filter(java.util.Objects::nonNull)
                .map(Field::asNumber)
                .filter(java.util.Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    @Override
    public Number averageField(String fieldName) {
        return records.values().stream()
                .map(record -> record.getField(fieldName))
                .filter(java.util.Objects::nonNull)
                .map(Field::asNumber)
                .filter(java.util.Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(Double.NaN);
    }

    @Override
    public long countRecords() {
        return records.size();
    }

    @Override
    public long nextRecordId() {
        return records.size()+1;
    }

    @Override
    public Transaction beginTransaction() {
        return new InMemoryTransaction(this);
    }

    @Override
    public Index createIndex(String fieldName) {
        if (indexes.containsKey(fieldName)) {
            return indexes.get(fieldName);
        }
        Index index = new InMemoryIndex(fieldName);
        indexes.put(fieldName, index);

        records.values().forEach(index::indexRecord);
        return index;
    }

    @Override
    public Index getIndex(String fieldName) {
        return indexes.get(fieldName);
    }
}
