package com.bionic.jbehave.gdrive;

import com.bionic.steps.GdriveSteps;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

/**
 * Created by bdd on 7/30/15.
 */
public class GdriveDefinitions {
    @Steps
    GdriveSteps steps;

    /** Overwritten by my merge forever :-)  
    @Given("authorized connection to Gdrive")
    public void givenAuthorizedConnectionToGdrive2() throws IOException {
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
    } **/

    @Given("authorized connection to Gdrive")
    public void givenAuthorizedConnectionToGdrive() {
        steps.authorizeToGdrive();
    }


    @When("I upload <filename> to GDrive with <filesize>")
    public void whenIUploadfilenameToGDriveWithfilesize(String filename) {
        steps.uploadFile(filename);
    }


    @When("I download <filename> from GDrive")
    public void whenIDownloadfilenameFromGDrive() {
        steps.downloadFile();
    }

    @Then("Downloaded file equals initial one")
    public void thenDownloadedFileEqualsInitialOne() {
        steps.filesShouldBeEqual();
    }

    @Then("Upload time took less than '15' seconds")
    @Pending
    public void thenUploadTimeTookLessThan15Seconds(String seconds) {
        steps.uploadTimeShouldTakeLessThan15Seconds(seconds);
    }

    @Then("Download time tool less than '20' seconds")
    @Pending
    public void thenDownloadTimeToolLessThan(String seconds) {
        steps.downloadTimeShouldTakeLessThan(seconds);
    }

    @Given("a file $name with size of $size Mb")
    @Alias("a differ file $name with size of $size Mb is in the same directory")
    public void givenAFiletesttxtWithSizeOf3Mb(String name, int size) {
        steps.createTestFile(name, size);
    }

    @Given("another file $name isn't in the same directory")
    public void givenAnotherFileIsntInDirectory(String name) {
        //Pending
    }

    @When("I run application with parameters '$parameters'")
    @Pending
    public void whenIRunApplicationWithParameters(String parameters) {
        steps.runApplication(parameters);
    }


   @Then("App notifies me that files are different")
   @Pending
   public void thenAppNotifiesMeThatFilesAreDifferent() {
      steps.checkAppOutput("FAILED");
   }

}
