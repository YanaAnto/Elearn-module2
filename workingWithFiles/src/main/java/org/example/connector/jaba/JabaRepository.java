package org.example.connector.jaba;


public interface JabaRepository<T> {

    void save(T object);
    T load(int id);
    void update(int id, T object);
    void delete(int id);
}
