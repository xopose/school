package com.db;

import java.util.Map;

public interface IDatabase {
    Map<String, Object> findById(Long id);
    void save(Long id, Map<String, Object> data);
    void update(Long id, Map<String, Object> data);
    void delete(Long id);
    Map<Long, Map<String, Object>> findAll();
}
/*
* Обернуть мапу row в IRecord
* Вместо findByField( Field, value(expression тоже можно сделать интерфейсом)) (сделать интерфейс field) (назвение колонки и тип)
* какую-то логику типа суммы по field
*
* */