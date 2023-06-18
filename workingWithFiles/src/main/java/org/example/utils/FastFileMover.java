package org.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FastFileMover {

    public static void moveFileViaNIO(String pathFrom, String pathTo) {
        try {
            Files.move(Paths.get(pathFrom), Paths.get(pathTo), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void moveFileViaFileStreams(String pathFrom, String pathTo) {
        try {
            File sourceFile = new File(pathFrom);
            File destinationDirectory = new File(pathTo);
            FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(
                new File(destinationDirectory, sourceFile.getName()));
            byte[] buffer = new byte[100000];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fis.close();
            fos.close();
            sourceFile.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void moveFileViaFileChannel(String pathFrom, String pathTo) {
        try {
            File fromFile = new File(pathFrom);
            File toFile = new File(pathTo);
            FileInputStream inFile = new FileInputStream(fromFile);
            FileOutputStream outFile = new FileOutputStream(toFile);
            FileChannel inChannel = inFile.getChannel();
            FileChannel outChannel = outFile.getChannel();
            int bytesWritten = 0;
            long byteCount = inChannel.size();
            while (bytesWritten < byteCount) {
                bytesWritten += inChannel.transferTo(bytesWritten, byteCount - bytesWritten, outChannel);
            }
            inFile.close();
            outFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
