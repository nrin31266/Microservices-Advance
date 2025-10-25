package com.rin;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonFileUtils {
    public static String readJsonFile(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            System.err.println("❌ Không đọc được file: " + path);
            return null;
        }
    }
}
