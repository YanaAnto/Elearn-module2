package org.example;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.example.connector.jaba.JabaRepositoryImpl;
import org.example.utils.DatabaseConnector;
import org.example.utils.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBUnitTest {

    private static org.example.connector.entity.File testFile =
        new org.example.connector.entity.File("file", "Content for file");
    private IDatabaseConnection connection;

    @Before
    public void setUp() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
        Connection jdbcConnection = new DatabaseConnector().getConnection();
        connection = new DatabaseConnection(jdbcConnection);
        IDataSet dataSet = readDataSet();
        String url = FileUtils.getProperty("url");
        String password = FileUtils.getProperty("password");
        String username = FileUtils.getProperty("username");
        IDatabaseTester databaseTester = new JdbcDatabaseTester("com.mysql.jdbc.Driver", url,
            username, password);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

    private IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(
            new File("src/test/resources/databaseData/dataset.xml"));
    }

    @Test
    public void getMaxIdTest() throws SQLException {
        JabaRepositoryImpl<org.example.connector.entity.File> jabaRepository =
            new JabaRepositoryImpl(connection.getConnection(),
                org.example.connector.entity.File.class);
        int maxId = jabaRepository.getMaxId();
        assertThat(maxId).isNotNull();
    }

    @Test
    public void loadFileTest() throws SQLException {
        JabaRepositoryImpl<org.example.connector.entity.File> jabaRepository =
            new JabaRepositoryImpl(connection.getConnection(),
                org.example.connector.entity.File.class);
        int maxId = jabaRepository.getMaxId();
        assertThat(jabaRepository.load(maxId)).isNotNull();
    }

    @Test
    public void saveFileTest() {
        JabaRepositoryImpl<org.example.connector.entity.File> jabaRepository = null;
        try {
            jabaRepository = new JabaRepositoryImpl(connection.getConnection(),
                org.example.connector.entity.File.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int testFileId = jabaRepository.getMaxId() + 1;
        testFile.setId(testFileId);
        jabaRepository.save(testFile);
        org.example.connector.entity.File file = jabaRepository.load(testFileId);
        assertThat(file)
            .usingRecursiveComparison()
            .isEqualTo(testFile);
    }

    @Test
    public void updateFileTest() {
        JabaRepositoryImpl<org.example.connector.entity.File> jabaRepository = null;
        try {
            jabaRepository = new JabaRepositoryImpl(connection.getConnection(),
                org.example.connector.entity.File.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int testFileId = jabaRepository.getMaxId() + 1;
        testFile.setId(testFileId);
        jabaRepository.save(testFile);
        var updatedFile = new org.example.connector.entity.File(testFileId, "newName", "new value");
        jabaRepository.update(testFileId, updatedFile);
        org.example.connector.entity.File file = jabaRepository.load(testFileId);
        assertThat(file)
            .usingRecursiveComparison()
            .isEqualTo(updatedFile);
    }

    @Test
    public void deleteFileTest() {
        JabaRepositoryImpl<org.example.connector.entity.File> jabaRepository = null;
        try {
            jabaRepository = new JabaRepositoryImpl(connection.getConnection(),
                org.example.connector.entity.File.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int testFileId = jabaRepository.getMaxId() + 1;
        testFile.setId(testFileId);
        jabaRepository.save(testFile);
        jabaRepository.delete(testFileId);
        org.example.connector.entity.File file = jabaRepository.load(testFileId);
        assertThat(file).isNull();
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
