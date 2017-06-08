package com.parrot.storage;

import com.parrot.InvocationInfo;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvocationToFileNameMapperTests {

    public static final String UUID1 = "uuid1";
    public static final String UUID2 = "uuid2";

    MockFileSystemFolder fileSystemFolder = new MockFileSystemFolder();

    @Test
    public void getFileName_works() throws IOException {
        InvocationInfo invocationInfo = getInvocationInfoForHourly();
        InvocationToFileNameMapper invocationToFileNameMapper = getInvocationToFileNameMap();
        String actual = invocationToFileNameMapper.getFileName(invocationInfo);
        Assert.assertEquals(UUID1 + ".json", actual);
    }

    @Test
    public void readsFromClassPathAndWritesToIt() throws IOException {
        InvocationToFileNameMapper invocationToFileNameMapper = getInvocationToFileNameMap();
        for(int i=0; i<3; i++) {
            String actualHourly = invocationToFileNameMapper.getFileName(getInvocationInfoForHourly());
            Assert.assertEquals(UUID1+".json", actualHourly);
            String actualDaily = invocationToFileNameMapper.getFileName(getInvocationInfoForDaily());
            Assert.assertEquals(UUID2+".json", actualDaily);
        }
        String expected = "[\n" +
                "  {\n" +
                "    \"key\": {\n" +
                "      \"methodName\": \"get\",\n" +
                "      \"path\": \"weather/current/\",\n" +
                "      \"queryParameters\": {\n" +
                "        \"frequency\": \"daily\",\n" +
                "        \"level\": \"detailed\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"value\": \"uuid2.json\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": {\n" +
                "      \"methodName\": \"get\",\n" +
                "      \"path\": \"weather/current/\",\n" +
                "      \"queryParameters\": {\n" +
                "        \"frequency\": \"hourly\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"value\": \"uuid1.json\"\n" +
                "  }\n" +
                "]";
        Assert.assertEquals(expected, fileSystemFolder.fileContents);
    }

    private InvocationToFileNameMapper getInvocationToFileNameMap() {
        UuidGenerator uuidGenerator = mock(UuidGenerator.class);
        when(uuidGenerator.get()).thenReturn(UUID1).thenReturn(UUID2);
        String basePathStr = "src/test/resources/recorded";
        return new InvocationToFileNameMapper(basePathStr, uuidGenerator, fileSystemFolder);
    }

    static InvocationInfo getInvocationInfoForHourly() {
        SortedMap<String, String> queryParameters = new TreeMap<>();
        queryParameters.put("frequency", "hourly");
        return new InvocationInfo("get", "weather/current/", queryParameters);
    }

    static InvocationInfo getInvocationInfoForDaily() {
        SortedMap<String, String> queryParameters = new TreeMap<>();
        queryParameters.put("frequency", "daily");
        queryParameters.put("level", "detailed");
        return new InvocationInfo("get", "weather/current/", queryParameters);
    }

    private class MockFileSystemFolder implements FileSystemFolder{
        final String existingFileName = "src/test/resources/recorded/weather/current/queries.json";
        int fileExistsCallCount = 0;
        String fileContents = "";

        @Override
        public String resolve(String folderName, String fileName) {
            return folderName + "/" + fileName;
        }

        @Override
        public boolean fileExists(String fileName) {
            if(!fileName.equals(existingFileName)){
                return false;
            }
            return (fileExistsCallCount++ > 0);
        }

        @Override
        public void saveToFile(String fileName, String contentsToSave) {
            assertCorrectFileName(fileName);
            fileContents = contentsToSave;
        }

        @Override
        public String readFromFile(String fileName) {
            assertCorrectFileName(fileName);
            return fileContents;
        }

        @Override
        public void ensurePathExists(String path) {

        }

        private void assertCorrectFileName(String fileName){
            if(!fileName.equals(existingFileName)){
                throw new RuntimeException("Unknown file: " + fileName);
            }
        }
    }
}
