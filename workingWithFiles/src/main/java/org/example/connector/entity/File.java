package org.example.connector.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.example.connector.jaba.annotation.JabaEntity;

@JabaEntity(name = "file")
public class File {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String value;

    public File() {
    }

    public File(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public File(int id, String name, String value) {
        this.name = name;
        this.value = value;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
