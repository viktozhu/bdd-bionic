package com.bionic.helpers;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Illya on 04.08.2015.
 */
public class RunHelper {
    private static Logger logger = LoggerFactory.getLogger(FileHelper.class);

    public static String runJar(String path, String parameters) {
        String[] cmdArr = {"java", "-jar", path};
        String[] paramsArr = parameters.split(" ");
        ProcessBuilder pb = new ProcessBuilder(ArrayUtils.addAll(cmdArr, paramsArr));
        Process process = null;
        try {
            process = pb.start();
            int returnCode = process.waitFor();
            return output(process.getInputStream());
        } catch (IOException | InterruptedException e) {
            logger.error("Error while running Jar " + path, e);
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
