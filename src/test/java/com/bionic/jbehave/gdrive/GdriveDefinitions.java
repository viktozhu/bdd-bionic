package com.bionic.jbehave.gdrive;

import com.bionic.google.DriveUpload;
import com.bionic.google.GmailAuthorization;
import com.bionic.helpers.FileHelper;
import com.bionic.steps.GdriveSteps;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by bdd on 7/30/15.
 */
public class GdriveDefinitions {
    @Steps
    GdriveSteps steps;

    @Given("authorized connection to Gdrive")
    public void givenAuthorizedConnectionToGdrive() throws IOException {
        PropertyLoader.loadPropertys();
        GmailAuthorization gmailAuthorization = null;
        try {
            gmailAuthorization = new GmailAuthorization("bdd-project", "src/main/resources/secrets/client_secret_drive.json");

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Drive service = gmailAuthorization.getDriveService("bionic.bdd@gmail.com");

        DriveUpload driveUpload = new DriveUpload();
        driveUpload.insertFile
                (service,"BDD","test","test","/src/test/resources/testData/testGif.gif");
    }

    @When("I upload <filename> to GDrive with <filesize>")
    public void whenIUploadfilenameToGDriveWithfilesize(@Named("filename") String name, @Named("filesize") Integer size) {
        steps.createFile(name,size);


    }

    @When("I download <filename> from GDrive")
    @Pending
    public void whenIDownloadfilenameFromGDrive() {
        // PENDING
    }

    @Then("Downloaded file equals initial one")
    @Pending
    public void thenDownloadedFileEqualsInitialOne() {
        // PENDING
    }

    @Then("Upload time took less than '15' seconds")
    @Pending
    public void thenUploadTimeTookLessThan15Seconds() {
        // PENDING
    }

    @Then("Download time tool less than '20' seconds")
    @Pending
    public void thenDownloadTimeToolLessThan20Seconds() {
        // PENDING
    }

    @Given("a file $name with size of $size Mb")
    @Alias("a differ file $name with size of $size Mb is in the same directory")
    public void givenAFiletesttxtWithSizeOf3Mb(String name, int size) {
        steps.createFile(name,size);
    }

    @Given("another file $name isn't in the same directory")
    public void givenAnotherFileIsntInDirectory(String name) {
        //Pending
    }

    @When("I use '-verify' option with these two files")
    @Pending
    public void whenIUseverifyOptionWithTheseTwoFiles() {

    }


   @Then("App notifies me that files are different")
   @Pending
   public void thenAppNotifiesMeThatFilesAreDifferent() {
      // PENDING
   }

}
