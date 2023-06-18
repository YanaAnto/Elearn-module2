package org.example.utils;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.example.utils.DiskAnalyzer.Command.GET_DIRECTORIES;
import static org.example.utils.DiskAnalyzer.Command.GET_FILES;
import static org.example.utils.DiskAnalyzer.Command.GET_SORTED_BY_SIZE_FILES;
import static org.example.utils.FileUtils.writeToFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiskAnalyzer {

    public static void execute(String path, int functionNumber, String fileOutputPath) {
        String outputValue;
        switch (functionNumber) {
            case 1:
                outputValue = getFileNameWithMaxSLetters(path);
                break;
            case 2:
                outputValue = getFiveLargestFiles(path);
                 break;
            case 3:
                outputValue = getAvgFileSize(path);
                 break;
            case 4:
                outputValue = getFilesAndDirectoriesStartWith(path, "ABC");
                 break;
            default:
                throw new RuntimeException("Invalid number of function!");
        }
        File file;
        try {
            file = new File(fileOutputPath + ".txt");
            file.createNewFile();
            writeToFile(fileOutputPath + ".txt", outputValue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFilesAndDirectoriesStartWith(String path, String letters) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(letters.toLowerCase().split("")).forEach(letter -> {
            List<String> filteredFiles = getFiles(path).stream().filter(fileName ->
                fileName.toLowerCase().startsWith(letter)).toList();
            List<String> filteredDirectories = getDirectories(path).stream().filter(fileName ->
                fileName.toLowerCase().startsWith(letter)).toList();
            stringBuilder.append(format("%d files and %d folders begin with the letter %s\n",
                filteredFiles.size(), filteredDirectories.size(), letter));
        });
        return stringBuilder.toString();
    }

    private static String getFiveLargestFiles(String path) {
        Map<String, Integer> filesSizeMap = getSortedFilesBySize(path);
        StringBuilder stringBuilder = new StringBuilder();
        filesSizeMap.entrySet().stream()
            .limit(5)
            .forEach(e -> stringBuilder.append(format("%s - %d bytes\n", e.getKey(), e.getValue())));
        return stringBuilder.toString();
    }

    private static String getAvgFileSize(String path) {
        Map<String, Integer> filesSizeMap = getSortedFilesBySize(path);
        Integer valuesSum = filesSizeMap.values().stream().reduce(Integer::sum).orElse(0);
        return valueOf(valuesSum / filesSizeMap.values().size());
    }

    private static String getFileNameWithMaxSLetters(String path) {
        Map<String, Integer> filesSizeMap = getSortedFilesBySize(path);
        Map<String, Integer> filesLettersMap = new LinkedHashMap<>();
        filesSizeMap.keySet().forEach(fileName -> {
            var pattern = Pattern.compile("(s)");
            int lettersCount = (int) pattern.matcher(fileName).results().count();
            filesLettersMap.put(fileName, lettersCount);
        });
        var entry = Collections
            .max(filesLettersMap.entrySet(), Map.Entry.comparingByValue());
        return format("Max number of letters 's' - %d was found in file %s/%s'", entry.getValue(),
            path, entry.getKey().trim());
    }

    private static Map<String, Integer> getSortedFilesBySize(String path) {
        Map<String, Integer> sizeFileNameMap = new LinkedHashMap<>();
        String input = ShellCommandUtils.executeCommand(path,
            GET_SORTED_BY_SIZE_FILES.getPattern());
        Pattern pattern = Pattern.compile("(\\d+)(.+)");
        Matcher matcher = pattern.matcher(input);
        matcher.results().forEach(item ->
            sizeFileNameMap.put(item.group(2), parseInt(item.group(1))));
        return sizeFileNameMap;
    }

    private static List<String> getDirectories(String path) {
        return getFilesOrDirectories(path, GET_DIRECTORIES);
    }

    private static List<String> getFiles(String path) {
        return getFilesOrDirectories(path, GET_FILES);
    }

    private static List<String> getFilesOrDirectories(String path, Command command) {
        String input = ShellCommandUtils.executeCommand(path, command.getPattern());
        Matcher matcher = Pattern.compile("(.+)").matcher(input);
        return matcher.results().toList().stream()
            .map(MatchResult::group)
            .toList();
    }

    public enum Command {

        GET_DIRECTORIES("ls -1 -d */"),
        GET_FILES("ls -1 | grep '\\.'"),
        GET_SORTED_BY_SIZE_FILES("ls -S -l | awk '{print $5, $9}' | grep '\\.'");

        private String pattern;

        Command(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }
    }
}
