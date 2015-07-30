package com.bionic.helpers;

import java.io.*;

public class FileHelper {

<<<<<<< HEAD
    public static String getFileHashSum(String filename){
        //TODO
        return "";
=======
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
>>>>>>> c9fd182b3b3c3484e25d45d0399176583a6a01ee
    }
}