package org.example.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShellCommandUtils {

    private static String osType = System.getProperty("os.name").toLowerCase();

    public static String executeCommand(String path, String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(path));
            if (osType.contains("windows")) {
                builder.command("cmd.exe", "/c", command);
            } else {
                builder.command("sh", "-c", command);
            }
            Process process = builder.start();
            InputStream inputStream = process.getInputStream();
            process.waitFor();
            return getStringFromInputStream(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getStringFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        }
        return stringBuilder.toString();
    }
}
