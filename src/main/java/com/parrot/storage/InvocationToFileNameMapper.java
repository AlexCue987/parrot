package com.parrot.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.parrot.InvocationInfo;
import javafx.util.Pair;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class InvocationToFileNameMapper {
    public static final String QUERIES_JSON = "queries.json";
    private final Path basePath;
    private final UuidGenerator uuidGenerator;
    private final Gson jsonPrettifier;
    private final FileSystemFolder fileSystemFolder;

    InvocationToFileNameMapper(String basePathStr, UuidGenerator uuidGenerator, FileSystemFolder fileSystemFolder) {
        this.fileSystemFolder = fileSystemFolder;
        basePath = Paths.get(basePathStr);
        this.uuidGenerator = uuidGenerator;
        jsonPrettifier = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    public InvocationToFileNameMapper(String basePathStr) {
        this(basePathStr, new UuidGenerator(), new FileSystemFolderImpl());
    }

    @SneakyThrows
    public String getFileName(InvocationInfo invocationInfo){
        SortedMap<InvocationInfo, String> fileNamesByInvocations = readFileNamesByInvocations(invocationInfo.getPath());
        if(fileNamesByInvocations.containsKey(invocationInfo)){
            return fileNamesByInvocations.get(invocationInfo);
        }
        String newFileName = uuidGenerator.get() + ".json";
        fileNamesByInvocations.put(invocationInfo, newFileName);
        saveFileNamesByInvocations(invocationInfo.getPath(), fileNamesByInvocations);
        return newFileName;
    }

    SortedMap<InvocationInfo, String> readFileNamesByInvocations(String relativePathStr) throws IOException {
        Path fullFolderPath = basePath.resolve(relativePathStr);
        fileSystemFolder.ensurePathExists(fullFolderPath.toString());
        Path fullPath = fullFolderPath.resolve(QUERIES_JSON);
        if(!fileSystemFolder.fileExists(fullPath.toString())){
            return new TreeMap<>();
        }
        String mapAsJson = fileSystemFolder.readFromFile(fullPath.toString());
        return getMapFromJson(mapAsJson);
    }

    void saveFileNamesByInvocations(String relativePathStr, Map<InvocationInfo, String> mapToSave) throws IOException {
        Path fullFolderPath = basePath.resolve(relativePathStr);
        fileSystemFolder.ensurePathExists(fullFolderPath.toString());
        Path filePath = fullFolderPath.resolve(QUERIES_JSON);
        List<Pair<InvocationInfo, String>> pivotedMap = pivotMap(mapToSave);
        String mapJson = jsonPrettifier.toJson(pivotedMap);
        fileSystemFolder.saveToFile(filePath.toString(), mapJson);
    }

    private List<Pair<InvocationInfo, String>> pivotMap(Map<InvocationInfo, String> mapToSave) {
        return mapToSave.entrySet().stream().
                    map(entry -> new Pair<>(entry.getKey(), entry.getValue())).
                    collect(Collectors.toList());
    }

    static Type resultType = new TypeToken<List<Pair<InvocationInfo, String>>>() { }.getType();

    private SortedMap<InvocationInfo, String> getMapFromJson(String mapAsJson) {
        List<Pair<InvocationInfo, String>> pivotedMap = jsonPrettifier.fromJson(mapAsJson, resultType);
        Map<InvocationInfo, String> ret = pivotedMap.stream().
                collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        return new TreeMap<>(ret);
    }
}
