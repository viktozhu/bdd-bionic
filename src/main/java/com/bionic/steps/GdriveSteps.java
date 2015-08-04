package com.bionic.steps;

import com.bionic.google.DriveDownload;
import com.bionic.google.DriveUpload;
import com.bionic.google.GmailAuthorization;
import com.bionic.helpers.FileHelper;
import com.google.api.services.drive.Drive;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by bdd on 7/30/15.
 */
public class GdriveSteps extends ScenarioSteps{

    private Drive drive = null;
    private com.google.api.services.drive.model.File uploadedFile;
    private String pathToDownloadedFile = "pathTodownloadedFile";

    //TODO: Put properFilename here
    private String pathToOriginalFile = null;

    @Step
    public void authorizeToGdrive() {
        GmailAuthorization gmailAuthorization = null;
        try {
            gmailAuthorization = new GmailAuthorization("bdd-project","src/main/resources/secrets/bionic.bdd.secret.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        try {
            drive = gmailAuthorization.getDriveService("bionic.bdd@gmail.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step
    public void uploadFile(String filename) {
        this.pathToOriginalFile = filename;
        DriveUpload driveUpload = new DriveUpload();
        try {
            uploadedFile = driveUpload.insertFile(drive, "UploadedTestFile", "This is uploaded test file!", filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step
    public void downloadFile() {
        new DriveDownload(drive, uploadedFile).saveFileToHDD(pathToDownloadedFile);
    }

    @Step
    public void filesShouldBeEqual() {
        String originalFileMD5HashSum = FileHelper.getFileHashSum(pathToOriginalFile);
        String downloadedFileMD5HashSum = FileHelper.getFileHashSum(pathToDownloadedFile);
        assertThat(originalFileMD5HashSum, equals(downloadedFileMD5HashSum));
    }

    @Step
    public void uploadTimeShouldTakeLessThan15Seconds(String expectedSeconds) {
        //Todo
    }

    public void downloadTimeShouldTakeLessThan(String expectedSeconds) {
        //Todo
    }
}
