package com.bionic.steps;

import com.bionic.google.DriveDownload;
import com.bionic.google.DriveUpload;
import com.bionic.google.GoogleAuthorization;
import com.bionic.helpers.FileHelper;
import com.bionic.helpers.RunHelper;
import com.google.api.services.drive.Drive;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class GdriveSteps extends ScenarioSteps {

    private Drive drive = null;
    private com.google.api.services.drive.model.File uploadedFile;
    private String pathToDownloadedFile = "target\\temp.txt";

    //TODO: Put properFilename here
    private String pathToOriginalFile = null;

    @Step
    public void authorizeToGdrive() {
        GoogleAuthorization googleAuthorization = null;
        try {
            googleAuthorization = new GoogleAuthorization("bdd-project", "src/main/resources/secrets/bionic.bdd.secret.json");
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        try {
            drive = googleAuthorization.getDriveService("bionic.bdd@gmail.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step
    public void uploadFile(String filename) {
        this.pathToOriginalFile = filename;
        this.pathToDownloadedFile = new StringBuilder(filename).insert(filename.indexOf('.'),
                new char[]{'D', 'o', 'w', 'n'}).toString();
        DriveUpload driveUpload = new DriveUpload();
        try {
            Instant start = Instant.now();
            uploadedFile = driveUpload.insertFileInFolder(drive, "BDD", "This is uploaded test file!", "test", filename);
            Instant end = Instant.now();
            Serenity.getCurrentSession().put("uploadTime", Duration.between(start, end).getSeconds());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step
    public void downloadFile() {
        DriveDownload driveDownload = new DriveDownload();
        Instant start = Instant.now();
        InputStream stream = driveDownload.downloadFile(drive, uploadedFile);
        driveDownload.saveFileToHDD(stream, pathToDownloadedFile);
        Instant end = Instant.now();
        Serenity.getCurrentSession().put("downloadTime", Duration.between(start, end).getSeconds());
    }

    @Step
    public void filesShouldBeEqual() {
        String originalFileMD5HashSum = FileHelper.getFileHashSum(pathToOriginalFile);
        String downloadedFileMD5HashSum = FileHelper.getFileHashSum(pathToDownloadedFile);
        Assert.assertTrue(originalFileMD5HashSum.equals(downloadedFileMD5HashSum));
    }

    @Step
    public void uploadTimeShouldTakeLessThan(int expectedSeconds) {
        long actual = (long) Serenity.getCurrentSession().get("uploadTime");
        Assert.assertTrue("Upload Time took more than " + expectedSeconds, actual < expectedSeconds);
    }

    @Step
    public void downloadTimeShouldTakeLessThan(int expectedSeconds) {
        long actual = (long) Serenity.getCurrentSession().get("downloadTime");
        Assert.assertTrue("Download Time took more than " + expectedSeconds, actual < expectedSeconds);
    }

    @Step
    public void createTestFile(String name, int size) {
        FileHelper.createTestFile(name, size);
    }

    @Step
    public void runApplication(String parameters) {
        String output = RunHelper.runJar("src\\test\\resources\\App.jar", parameters);
        Serenity.getCurrentSession().put("appOutput", output);
    }

    public void checkAppOutput(String expected) {
        String output = (String) Serenity.getCurrentSession().get("appOutput");
        //Assert.assertTrue("Return code is not " + expectedCode, returnCode == expectedCode);
        assertThat(output, containsString(expected));
    }
}
