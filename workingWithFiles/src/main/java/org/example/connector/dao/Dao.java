package org.example.connector.dao;

public interface Dao<T> {

    T get(int id);

    void create(int id, String name, String value);

    void delete();

    void update(T object);
}
