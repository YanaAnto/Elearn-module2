package org.example.connector.dao;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.connector.entity.File;
import org.example.connector.exception.DBConnectionException;

public class FileDAO implements Dao<File> {

    private final Connection connection;

    public FileDAO(Connection connection) {
        this.connection = connection;
    }

    public int getMaxIdValue() {
        String query = "call getLastId()";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            ResultSet resultSet = callableStatement.executeQuery();
            resultSet.next();
            int maxId = resultSet.getInt(1);
            callableStatement.close();
            return maxId;
        } catch (SQLException e) {
            throw new DBConnectionException("Failed getting max id: " + e.getMessage());
        }
    }

    public File get(int id) {
        String query = "call getFileById(?)";
        try {
            CallableStatement statement = connection.prepareCall(query);
            statement.setInt("fileId", id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            File foundFile = new File(resultSet.getInt("id"), resultSet.getString("name"),
                resultSet.getString("value"));
            statement.close();
            return foundFile;
        } catch (Exception e) {
            throw new DBConnectionException("Failed getting file by id: " + e.getMessage());
        }
    }

    public void create(int id, String name, String value) {
        String query = "call createFile(?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, value);
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            throw new DBConnectionException("Failed creating file: " + e.getMessage());
        }
    }

    public void delete() {
        throw new UnsupportedOperationException("Method is not implemented!");
    }

    public void update(File file) {
        throw new UnsupportedOperationException("Method is not implemented!");
    }
}
