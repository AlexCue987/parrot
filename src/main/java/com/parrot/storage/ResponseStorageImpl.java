package com.parrot.storage;

import com.parrot.InvocationInfo;
import com.parrot.ResponseStorage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ResponseStorageImpl implements ResponseStorage {
    private final FileSystemFolder fileSystemFolder;
    private final InvocationToFileNameMapper fileNameMapper;
    private final Path basePath;

    ResponseStorageImpl(FileSystemFolder fileSystemFolder,
                        InvocationToFileNameMapper fileNameMapper,
                        String basePathStr) {
        this.fileSystemFolder = fileSystemFolder;
        this.fileNameMapper = fileNameMapper;
        basePath = Paths.get(basePathStr);
    }

    public ResponseStorageImpl(String basePathStr) {
        this(new FileSystemFolderImpl(),
                new InvocationToFileNameMapper(basePathStr),
                basePathStr);
    }

    @Override
    public void save(InvocationInfo invocationInfo, String result) {
        String pathToFileStr = getPathToFileStr(invocationInfo);
        fileSystemFolder.saveToFile(pathToFileStr, result);
    }

    @Override
    public String read(InvocationInfo invocationInfo) {
        String pathToFileStr = getPathToFileStr(invocationInfo);
        return fileSystemFolder.readFromFile(pathToFileStr);
    }

    String getPathToFileStr(InvocationInfo invocationInfo) {
        String fileName = fileNameMapper.getFileName(invocationInfo);
        Path pathToFolder = basePath.resolve(invocationInfo.getPath());
        Path pathToFile = pathToFolder.resolve(fileName);
        return pathToFile.toString();
    }
}
