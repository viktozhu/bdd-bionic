package com.bionic.google;

import com.bionic.helpers.FileHelper;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.openqa.jetty.log.LogStream;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by 1 on 05.08.2015.
 */
public class GdriveApp {
    public static void main(String[] args) throws IOException {

        PropertyLoader.loadPropertys();
        GmailAuthorization gmailAuthorization = null;
        try {
            String secretPath = DriveUpload.getFilePath("/src/main/resources/secrets/bionic.bdd.secret.json");
            gmailAuthorization =
                    new GmailAuthorization("bdd-project", secretPath);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Drive service = gmailAuthorization.getDriveService("bionic.bdd@gmail.com");

        if (args[0].equals("-upload")) {
            try {
                DriveUpload driveUpload = new DriveUpload();
                driveUpload.insertFileInFolder
                        (service, "BDD", "testTxt.txt", "testTxt.txt", args[1]);
                System.out.println("OK");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("1");
            }
        } else if (args[0].equals("-download")) {
            System.out.println("Download not implemented completely");
        } else if (args[0].equals("-verify")) {
            String file1 = FileHelper
                    .getFileHashSum(DriveUpload.getFilePath(args[1]));
            String file2 = FileHelper
                    .getFileHashSum(DriveUpload.getFilePath(args[2]));
            if (file1.equals(file2)) {
                System.out.println("OK");
            } else {
                System.out.println("NOK");
            }
        } else {
            System.out.println("NOK. Incorrect argument");

        }
    }
}
