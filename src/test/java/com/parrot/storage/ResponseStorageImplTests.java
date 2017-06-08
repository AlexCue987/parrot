package com.parrot.storage;

import com.parrot.InvocationInfo;
import com.parrot.ResponseStorage;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ResponseStorageImplTests {
    public static final String RESPONSE_FOR_HOURLY = "response for hourly";
    public static final String RESPONSE_FOR_DAILY = "response for daily";
    public static final String RESULT = "some results";
    public static final String FULL_PATH = "src/test/resources/recorded/weather/current/UUID1.json";
    final String basePath = "src/test/resources/recorded";
    final FileSystemFolderImpl fileSystemFolder = new FileSystemFolderImpl();

    @Ignore
    @Test
    public void smokeTestActuallyWritesFiles(){
        ResponseStorage responseStorage = getResponseStorage();
        responseStorage.save(InvocationToFileNameMapperTests.getInvocationInfoForDaily(), RESPONSE_FOR_DAILY);
        responseStorage.save(InvocationToFileNameMapperTests.getInvocationInfoForHourly(), RESPONSE_FOR_HOURLY);
        System.out.println(responseStorage.read(InvocationToFileNameMapperTests.getInvocationInfoForDaily()));
        System.out.println(responseStorage.read(InvocationToFileNameMapperTests.getInvocationInfoForHourly()));
    }

    @Test
    public void getPathToFileStr(){
        InvocationInfo invocationInfo = InvocationToFileNameMapperTests.getInvocationInfoForDaily();
        ResponseStorageImpl responseStorage = getResponseStorage();
        String actual = responseStorage.getPathToFileStr(invocationInfo);
        Assert.assertEquals(FULL_PATH, actual);
    }

    @Test
    public void saves(){
        FileSystemFolder fileSystemFolder = mock(FileSystemFolder.class);
        InvocationToFileNameMapper mapper = getInvocationToFileNameMapper();
        ResponseStorage responseStorage = new ResponseStorageImpl(fileSystemFolder, mapper, basePath);
        InvocationInfo invocationInfo = InvocationToFileNameMapperTests.getInvocationInfoForDaily();
        responseStorage.save(invocationInfo, RESULT);
        verify(fileSystemFolder, times(1)).saveToFile(eq(FULL_PATH), eq(RESULT));
    }

    @Test
    public void reads(){
        FileSystemFolder fileSystemFolder = mock(FileSystemFolder.class);
        when(fileSystemFolder.readFromFile(FULL_PATH)).thenReturn(RESULT);
        InvocationToFileNameMapper mapper = getInvocationToFileNameMapper();
        ResponseStorage responseStorage = new ResponseStorageImpl(fileSystemFolder, mapper, basePath);
        InvocationInfo invocationInfo = InvocationToFileNameMapperTests.getInvocationInfoForDaily();
        String actual = responseStorage.read(invocationInfo);
        Assert.assertEquals(RESULT, actual);
        verify(fileSystemFolder, times(1)).readFromFile(eq(FULL_PATH));
    }

    private ResponseStorageImpl getResponseStorage() {
        InvocationToFileNameMapper mapper = getInvocationToFileNameMapper();
        return new ResponseStorageImpl(fileSystemFolder, mapper, basePath);
    }

    private InvocationToFileNameMapper getInvocationToFileNameMapper() {
        UuidGenerator uuidGenerator = mock(UuidGenerator.class);
        when(uuidGenerator.get()).thenReturn("UUID1").thenReturn("UUID2");
        return new InvocationToFileNameMapper(basePath, uuidGenerator, fileSystemFolder);
    }

    private class MockEmptyStorage implements ResponseStorage{

        @Override
        public void save(InvocationInfo invocationInfo, String result) {

        }

        @Override
        public String read(InvocationInfo invocationInfo) {
            return null;
        }
    }
}
