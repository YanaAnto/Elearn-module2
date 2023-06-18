package org.example.utils;

import static java.nio.charset.Charset.defaultCharset;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

public class FileUtils {

    private FileUtils() {
    }

    public static String readFromFileNamed(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream in = new FileInputStream(fileName)) {
            stringBuilder.append(IOUtils.toString(in, defaultCharset()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed reading from file: " + e.getMessage());
        }
        return stringBuilder.toString();
    }

    public static String getProperty(String key) {
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(in);
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed getting property: " + e.getMessage());
        }
    }

    public static void writeToFile(String path, String value) {
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(value);
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
