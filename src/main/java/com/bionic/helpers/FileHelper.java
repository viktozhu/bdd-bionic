package com.bionic.helpers;

import java.io.*;

public class FileHelper {

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
}