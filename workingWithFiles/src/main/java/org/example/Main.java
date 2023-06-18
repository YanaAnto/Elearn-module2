package org.example;

import org.example.utils.DiskAnalyzer;
import org.example.utils.FastFileMover;

public class Main {

    public static void main(String[] args) {
        DiskAnalyzer.execute("/Users/Yana_Antaniuk/Desktop/eLearn", 2,
            "/Users/Yana_Antaniuk/Desktop/output2");
        FastFileMover.moveFileViaFileStreams("/Users/Yana_Antaniuk/Desktop/output.txt",
            "/Users/Yana_Antaniuk/Documents");
        FastFileMover.moveFileViaNIO("/Users/Yana_Antaniuk/Desktop/output4.txt",
            "/Users/Yana_Antaniuk/Documents/output4.txt");
    }
}
