package com.new_db.utils;

import com.new_db.Record;
import com.new_db.exceptions.RecordNotFoundException;

public interface Transaction {

    /**
     * Выполняет добавление записи в рамках транзакции.
     *
     * @param id     Идентификатор записи.
     * @param record Объект записи.
     */
    void addRecord(long id, Record record);

    /**
     * Выполняет обновление записи в рамках транзакции.
     *
     * @param id     Идентификатор записи.
     * @param record Обновленные данные записи.
     * @throws RecordNotFoundException Если запись с указанным ID не найдена.
     */
    void updateRecord(long id, Record record) throws RecordNotFoundException;

    /**
     * Выполняет удаление записи в рамках транзакции.
     *
     * @param id Идентификатор записи.
     * @return Результат удаления записи
     */
    boolean deleteRecord(long id);

    /**
     * Фиксирует все операции, выполненные в рамках транзакции.
     */
    void commit();

    /**
     * Откатывает все операции, выполненные в рамках транзакции.
     */
    void rollback();

    /**
     * Проверяет, активна ли транзакция.
     *
     * @return Результат проверки статуса транзакции
     */
    boolean isActive();
}
