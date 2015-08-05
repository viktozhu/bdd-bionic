package com.bionic.google;

import com.bionic.utils.PropertyLoader;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;


import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by kpe on 7/29/2015.
 */
public class DriveUpload {

    public static String getFilePath(String fileAddr) {
        fileAddr = PropertyLoader.loadProperty("project.path") + fileAddr;
        java.io.File file = new java.io.File(fileAddr);
        String absolutePath = file.getAbsolutePath();
        return absolutePath;
    }

    public static String getMimeType(String filePath){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        return mimetypesFileTypeMap.getContentType(filePath);
    }


    public File insertFile(Drive service, String title, String description, String filename) throws IOException {
        // File's metadata.
        String mimeType = getMimeType(getFilePath(filename));
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);


        // File's content.
        java.io.File fileContent = new java.io.File(getFilePath(filename));
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

    public File insertFileInFolder(Drive service, String folderName, String title, String description, String filename) throws IOException {
        // File's metadata.
        String mimeType = getMimeType(getFilePath(filename));
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);

        // Set the parent folder.

        String parentId = createPublicFolder(service,folderName).getId();

        if (parentId != null && parentId.length() > 0) {
            body.setParents(
                    Arrays.asList(new ParentReference().setId(parentId)));
        }

        // File's content.
        java.io.File fileContent = new java.io.File(getFilePath(filename));
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

    private static File createPublicFolder(Drive service, String folderName) throws IOException {

        Drive.Files.List request = service.files().list().setQ(
                "mimeType='application/vnd.google-apps.folder' and trashed=false");
        FileList files = request.execute();
        File body = new File();

        try {
            for (int i=0;i<request.size();i++) {
                if (files.getItems().get(i).getTitle().equals(folderName)) {
                    body = files.getItems().get(i);
                    break;
                }
            }
        } catch (IndexOutOfBoundsException e){
            if(e.getMessage().contains("Index: 0, Size: 0")){
                body.setTitle(folderName);
                body.setMimeType("application/vnd.google-apps.folder");

                File file = service.files().insert(body).execute();

                Permission permission = new Permission();
                permission.setValue("");
                permission.setType("anyone");
                permission.setRole("reader");

                service.permissions().insert(file.getId(), permission).execute();

                System.out.println(file);
                return file;
            }
        }
        return body;
    }
}
