package com.new_db.exceptions;

public class TableNotFoundException extends Exception {
    public TableNotFoundException(String message) {
        super(message);
    }
}
