package com.bionic.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class FileHelper {

    private static Logger logger = LoggerFactory.getLogger(FileHelper.class);

    public static String getFileHashSum(String pathToFile) {
        FileInputStream fileInputStream = null;
        String md5 = "";
        try {
            fileInputStream = new FileInputStream(new File(pathToFile));
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static void createTestFile(String name, int size) {
        try {
            String path = "target/" + name;
            RandomAccessFile file = new RandomAccessFile(path, "rw");
            file.setLength(size * 1024 * 1024);

            //change content of file with random values
            byte[] rndBytes = new byte[100];
            Random rnd = new Random();
            rnd.nextBytes(rndBytes);

            file.write(rndBytes);
            file.close();
            logger.info("File "+name+" was created");
        } catch (IOException e) {
            logger.error("Error while creating " + name, e);
        }
    }
}