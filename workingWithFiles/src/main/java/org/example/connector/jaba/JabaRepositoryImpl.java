package org.example.connector.jaba;

import static org.example.connector.jaba.JabaRepositoryImpl.Operation.SELECT;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.connector.jaba.annotation.JabaEntity;

public class JabaRepositoryImpl<T> implements JabaRepository<T> {

    private final Connection connection;
    private final Class<T> entityType;
    private final String tableName;

    public JabaRepositoryImpl(Connection connection, Class<T> entityType) {
        JabaEntity jabaEntity = entityType.getDeclaredAnnotation(JabaEntity.class);
        if (jabaEntity == null) {
            throw new JabaRepositoryException("Failed getting table name!");
        }
        this.connection = connection;
        this.tableName = jabaEntity.name();
        this.entityType = entityType;
    }

    @Override
    public void save(T object) {
        List<String> valueList = new ArrayList<>();
        Field[] declaredFields = entityType.getDeclaredFields();
        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                valueList.add("'%s'".formatted(field.get(object).toString()));
            }
            String insertedValues = String.join(",", valueList);
            String query = Operation.INSERT.getPattern(tableName)
                .replace("{VALUES}", insertedValues);
            connection.prepareStatement(query).execute();
        } catch (Exception e) {
            throw new JabaRepositoryException(e.getMessage());
        }
    }

    @Override
    public T load(int id) {
        String query = SELECT.getPattern(tableName).replace("{CONDITION}", "id = %s".formatted(id));
        try {
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            Object newInstance = entityType.getDeclaredConstructor().newInstance();
            Field[] entityDeclaredFields = entityType.getDeclaredFields();
            for (Field entityField : entityDeclaredFields) {
                entityField.setAccessible(true);
                String name = entityField.getName();
                Object value = resultSet.getObject(resultSet.findColumn(name));
                entityField.set(newInstance, value);
            }
            return entityType.cast(newInstance);
        } catch (Exception e) {
            throw new JabaRepositoryException(e.getMessage());
        }
    }

    @Override
    public void update(int id, T object) {
        Map<String, String> typeValueMap = new HashMap<>();
        Field[] declaredFields = entityType.getDeclaredFields();
        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                String value = "'%s'".formatted(field.get(object).toString());
                typeValueMap.put(field.getName(), value);
            }
            String insertedValues = typeValueMap.entrySet().stream()
                .map(e -> e.getKey() + " = " + e.getValue())
                .collect(Collectors.joining(", "));
            String query = Operation.UPDATE.getPattern(tableName)
                .replace("{VALUES}", insertedValues)
                .replace("{CONDITION}", "id = %s".formatted(id));
            connection.prepareStatement(query).execute();
        } catch (Exception e) {
            throw new JabaRepositoryException(e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String query = Operation.DELETE.getPattern(tableName)
            .replace("{CONDITION}", "id = %s".formatted(id));
        try {
            connection.prepareStatement(query).execute();
        } catch (Exception e) {
            throw new JabaRepositoryException(e.getMessage());
        }
    }

    public int getMaxId() {
        String query = Operation.GET_MAX_ID.getPattern(tableName);
        try {
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (Exception e) {
            throw new JabaRepositoryException(e.getMessage());
        }
    }

    public enum Operation {

        INSERT("insert into {TABLE_NAME} values ({VALUES})"),
        SELECT("select * from {TABLE_NAME} where {CONDITION}"),
        GET_MAX_ID("select max(id) from {TABLE_NAME}"),
        UPDATE("update {TABLE_NAME} set {VALUES} where {CONDITION}"),
        DELETE("delete from {TABLE_NAME} where {CONDITION}");
        private String pattern;

        Operation(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern(String tableName) {
            return pattern.replace("{TABLE_NAME}", tableName);
        }
    }
}
