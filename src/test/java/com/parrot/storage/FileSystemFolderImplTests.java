package com.parrot.storage;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class FileSystemFolderImplTests {
    public static final String FILE_NAME = "src/test/resources/sample.txt";
    FileSystemFolder fileSystemFolder = new FileSystemFolderImpl();

    @Test
    public void resolves(){
        String actual = fileSystemFolder.resolve("activities/outdoors/hiking", "campsites.json");
        //this might or might not work for Windows - never used
        Assert.assertEquals("activities/outdoors/hiking/campsites.json", actual);
    }

    @Test
    public void fileExists_returnsTrue(){
        Assert.assertTrue(fileSystemFolder.fileExists(FILE_NAME));
    }

    @Test
    public void fileExists_returnsFalse(){
        Assert.assertFalse(fileSystemFolder.fileExists("src/test/resources/noSuchFile"));
    }

    @Ignore
    @Test
    public void saveToFile_writesTOFileSystem(){
        String contents = "Do it or do not, there is no try.";
        String fileName = "src/test/resources/writtenByTest.txt";
        fileSystemFolder.saveToFile(fileName, contents);
    }

    @Test
    public void readFromFile(){
        String actual = fileSystemFolder.readFromFile(FILE_NAME);
        Assert.assertEquals("sample data", actual);
    }
}
