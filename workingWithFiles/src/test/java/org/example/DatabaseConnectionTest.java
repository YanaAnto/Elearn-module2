package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import org.example.connector.dao.FileDAO;
import org.example.connector.entity.File;
import org.example.connector.jaba.JabaRepositoryImpl;
import org.example.utils.DatabaseConnector;
import org.junit.jupiter.api.Test;

public class DatabaseConnectionTest {

    private static File testFile = new File("file", "Content for file");

    @Test
    public void verifyCreatingFileInDatabaseTest() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        FileDAO fileDAO = new FileDAO(databaseConnector.getConnection());
        testFile.setId(fileDAO.getMaxIdValue() + 1);
        fileDAO.create(testFile.getId(), testFile.getName(), testFile.getValue());
        File actualFile = fileDAO.get(testFile.getId());
        assertThat(actualFile)
            .usingRecursiveComparison()
            .isEqualTo(testFile);
    }

    @Test
    public void verifyCreatingFileViaJavaORMTest() {
        Connection connection = new DatabaseConnector().getConnection();
        JabaRepositoryImpl<File> jabaRepository = new JabaRepositoryImpl(connection, File.class);
        int maxId = jabaRepository.getMaxId() + 1;
        testFile.setId(maxId);
        jabaRepository.save(testFile);
        File actualFile = jabaRepository.load(maxId);
        assertThat(actualFile)
            .usingRecursiveComparison()
            .isEqualTo(testFile);
    }
}
