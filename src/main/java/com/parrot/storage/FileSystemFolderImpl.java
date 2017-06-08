package com.parrot.storage;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemFolderImpl implements FileSystemFolder{
    @Override
    public String resolve(String folderName, String fileName) {
        Path folderPath = Paths.get(folderName);
        Path fullPath = folderPath.resolve(fileName);
        return fullPath.toString();
    }

    @Override
    public boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    @SneakyThrows
    @Override
    public void saveToFile(String fileName, String contentsToSave) {
        try(FileWriter fileWriter = new FileWriter(fileName)){
            fileWriter.write(contentsToSave);
        }
    }

    @SneakyThrows
    @Override
    public String readFromFile(String fileName) {
        File file = new File(fileName);
        return FileUtils.readFileToString(file);
    }

    @Override
    public void ensurePathExists(String path){
        File dirs = new File(path);
        if(!dirs.exists()){
            //noinspection ResultOfMethodCallIgnored
            dirs.mkdirs();
        }
    }
}
