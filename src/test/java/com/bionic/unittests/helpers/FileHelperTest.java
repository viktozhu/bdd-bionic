package com.bionic.unittests.helpers;

import com.bionic.helpers.FileHelper;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class FileHelperTest {

    @Test
    public void createFileTest() {
        File file = FileHelper.createTestFile("testFileName", 10);
        assertTrue(file.exists());
        assertTrue(getFileSize(file.getPath()) == 10);
        file.delete();
        assertFalse(file.exists());
    }

    @Test
    public void compareFilesHashSumPositiveTest() {
        String filePath = "src/test/resources/testData/testGif.gif";
        assertEquals(FileHelper.getFileHashSum(filePath), FileHelper.getFileHashSum(filePath));
    }

    @Test
    public void compareFilesHashSumNegativeTest() {
        String file1 = "src/test/resources/testData/testGif.gif";
        String file2 = "src/test/resources/testData/testJpg.jpg";
        assertNotEquals(FileHelper.getFileHashSum(file1), FileHelper.getFileHashSum(file2));
    }

    private long getFileSize(String filePath) {
        File file = new File(filePath);
        long size = 0;
        if (file.exists()) {
            size = file.length() / 1024 / 1024;
        }
        return size;
    }
}