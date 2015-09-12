package com.bionic.helpers;

import com.bionic.google.DriveUpload;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunHelper {
    private static Logger logger = LoggerFactory.getLogger(FileHelper.class);

    public static String runJar(String jarPath, String command,String filePath) {
        String[] cmdArr = {"java", "-jar", DriveUpload.getFilePath(jarPath),command};
        String[] filePaths = filePath.split(" ");
        String [] fullFilePaths=new String[filePaths.length];

        for(int i=0; i<filePaths.length;i++){
            fullFilePaths[i]=DriveUpload.getFilePath(filePaths[i]);
        }
        ProcessBuilder pb = new ProcessBuilder(ArrayUtils.addAll(cmdArr, fullFilePaths));
        Process process;
        try {
            process = pb.start();
            process.waitFor();
            return output(process.getInputStream());
        } catch (IOException | InterruptedException e) {
            logger.error("Error while running Jar " + jarPath, e);
        }
        return "";
    }

    private static String output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }

}
