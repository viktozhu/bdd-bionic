package com.bionic.google;

import com.bionic.helpers.FileHelper;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Scanner;

public class GdriveApp {

    public static File fileUpload(Drive service, String folderName, String title, String desc, String filePath) {
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

    public static void main(String[] args){

        PropertyLoader.loadProperties();
        GoogleAuthorization googleAuthorization = null;
        try {
            String secretPath = DriveUpload.getFilePath("/src/main/resources/secrets/bionic.bdd.secret.json");
            try {
                googleAuthorization =
                        new GoogleAuthorization("bdd-project", secretPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Drive service = null;
        try {
            service = googleAuthorization.getDriveService("bionic.bdd@gmail.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filePathParts[] = args[1].split("/");

        if (args[0].equals("-upload")) {
            fileUpload(service, "BDD", filePathParts[filePathParts.length - 1], "test desc", args[1]);
        } else if (args[0].equals("-download")) {
            System.out.println
                    ("Prerequisite: file \"" + filePathParts[filePathParts.length - 1] + "\" should be uploaded");
            File file = fileUpload(service, "BDD", filePathParts[filePathParts.length - 1], "test desc", args[1]);
            DriveDownload driveDownload = new DriveDownload();

            System.out.println("Enter FULL file path with file name");
            Scanner sc = new Scanner(System.in);
            InputStream stream = driveDownload.downloadFile(service, file);
            driveDownload.saveFileToHDD(stream,  sc.nextLine());
        } else if (args[0].equals("-verify")) {
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
