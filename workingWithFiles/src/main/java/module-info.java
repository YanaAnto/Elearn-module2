module workingWithFiles {

    opens org.example.utils;
    exports org.example.connector.entity;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.google.gson;
    requires jackson.datatype.jsr310;
    requires com.google.common;
    requires org.apache.commons.io;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires spring.data.commons;
    requires mysql.connector.java;
}