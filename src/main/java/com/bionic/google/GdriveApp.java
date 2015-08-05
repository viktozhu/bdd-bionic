package com.bionic.google;

import com.bionic.helpers.FileHelper;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.drive.Drive;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by 1 on 05.08.2015.
 */
public class GdriveApp {
    public static void main(String [] args) throws IOException {

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

        for(int i=0; i<args.length;i++){
            if(args[i].equals("Upload")){
                DriveUpload driveUpload = new DriveUpload();
                driveUpload.insertFileInFolder
                        (service, "BDD", "testTxt.txt", "testTxt.txt", "/src/test/resources/testData/testTxt.txt");
            }
            else if(args[i].equals("Download")){
                System.out.println("Download not implemented completely");
            }
            else if(args[i].equals("Verify")) {
                String file1 =FileHelper
                        .getFileHashSum(DriveUpload.getFilePath("/src/test/resources/testData/testTxt.txt"));
                String file2 =FileHelper
                        .getFileHashSum(DriveUpload.getFilePath("/src/test/resources/testData/testTxt.txt"));
                if(file1.equals(file2)){
                    System.out.println("Verify method works correctly");
                }
            }
            else {
                System.out.println("Incorrect argument");
            }
        }
    }
}
