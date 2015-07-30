package com.bionic.google;

import com.bionic.utils.PropertyLoader;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;


import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;

/**
 * Created by kpe on 7/29/2015.
 */
public class DriveUpload {

    public static String getFileAbsPath(String filePath) {
        filePath = PropertyLoader.loadProperty("project.basedir") + filePath;
        java.io.File file = new java.io.File(filePath);
        String absolutePath = file.getAbsolutePath();
        return absolutePath;
    }

    public static String getMimeType(String filePath){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        return mimetypesFileTypeMap.getContentType(filePath);
    }

    public File insertFile(Drive service,String title, String description, String filename) {
        // File's metadata.
        String mimeType = getMimeType(getFileAbsPath(filename));
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);

        // File's content.
        java.io.File fileContent = new java.io.File(getFileAbsPath(filename));
        FileContent mediaContent = new FileContent(mimeType, fileContent);
        try {
            File file = service.files().insert(body, mediaContent).execute();

            System.out.println("File ID: " + file.getId());

            return file;
        } catch (IOException e) {
            System.out.println("An error occured: " + e);
            return null;
        }
    }
}
