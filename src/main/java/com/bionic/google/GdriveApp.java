package com.bionic.google;

import com.bionic.helpers.FileHelper;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.Instant;

/**
 * Created by 1 on 05.08.2015.
 */
public class GdriveApp {

    public static File fileUpload(Drive service,String folderName, String title, String desc,String filePath){
        File file = null;
        try {
            DriveUpload driveUpload = new DriveUpload();
            file = driveUpload.insertFileInFolder
                    (service, folderName, title, desc, filePath);
            System.out.println("OK");
        } catch (Exception e) {
            System.out.println("NOK");
            System.exit(1);
        }
        return file;
    }
    public static void main(String[] args) throws IOException {

        PropertyLoader.loadPropertys();
        GmailAuthorization gmailAuthorization = null;
        try {
            String secretPath = DriveUpload.getFilePath("/src/main/resources/secrets/bionic.bdd.secret.json");
            gmailAuthorization =
                    new GmailAuthorization("bdd-project", secretPath);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Drive service = gmailAuthorization.getDriveService("bionic.bdd@gmail.com");
        String filePathParts [] = args[1].split("/");

        if (args[0].equals("-upload")) {
            fileUpload(service,"BDD",filePathParts[filePathParts.length - 1],"test desc",args[1]);
        }
        else if (args[0].equals("-download")) {
            System.out.println
                    ("Prerequisite: file \"" + filePathParts[filePathParts.length - 1] + "\" should be uploaded");
            File file = fileUpload(service,"BDD",filePathParts[filePathParts.length - 1],"test desc",args[1]);
            DriveDownload driveDownload = new DriveDownload();
            InputStream stream = driveDownload.downloadFile(service, file);
            driveDownload.saveFileToHDD(stream, DriveUpload.getFilePath(args[2]));
        }
        else if (args[0].equals("-verify")) {
            String file1 = FileHelper
                    .getFileHashSum(DriveUpload.getFilePath(args[1]));
            String file2 = FileHelper
                    .getFileHashSum(DriveUpload.getFilePath(args[2]));
            if (file1.equals(file2)) {
                System.out.println("OK");
            } else {
                System.out.println("NOK");
                System.exit(1);
            }
        } else {
            System.out.println("NOK");
            System.exit(1);
        }
    }
}