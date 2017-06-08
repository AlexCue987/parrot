package com.parrot.storage;

public interface FileSystemFolder {
    String resolve(String folderName, String fileName);
    boolean fileExists(String fileName);
    void saveToFile(String fileName, String contentsToSave);
    String readFromFile(String fileName);
    void ensurePathExists(String path);
}
