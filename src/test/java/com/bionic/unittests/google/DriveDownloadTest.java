package com.bionic.unittests.google;


import com.bionic.google.DriveDownload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DriveDownloadTest {
    private InputStream fileToStream;

    @Before
    public void prepareData() throws FileNotFoundException {
        File actualFile = new File("src/test/resources/testData/testGif.gif");
        fileToStream = new FileInputStream(actualFile);
    }

    @Test
    public void inputStreamToFileTest() {
        String fileToCreateFromStream = "target/testGifTest.gif";
        new DriveDownload().saveFileToHDD(fileToStream, fileToCreateFromStream);
        File fileFromCreatedFromStream = new File(fileToCreateFromStream);
        Assert.assertTrue(fileFromCreatedFromStream.exists());
    }
}
