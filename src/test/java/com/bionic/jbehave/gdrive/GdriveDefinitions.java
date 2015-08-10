package com.bionic.jbehave.gdrive;

import com.bionic.steps.GdriveSteps;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.*;

/**
 * Created by bdd on 7/30/15.
 */
public class GdriveDefinitions {
    @Steps
    GdriveSteps steps;

    @Given("authorized connection to Gdrive")
    public void givenAuthorizedConnectionToGdrive() {
        steps.authorizeToGdrive();
    }


    @When("I upload <filename> to GDrive with <filesize>")
    public void whenIUploadfilenameToGDriveWithfilesize(@Named("filename") String filename,
                                                        @Named("filesize") String filesize ) {
        steps.createTestFile(filename, Integer.valueOf(filesize.substring(0, filesize.indexOf('M'))));
        steps.uploadFile("target/" + filename);
    }

    @When("I download <filename> from GDrive")
    public void whenIDownloadfilenameFromGDrive() {
        steps.downloadFile();
    }

    @Then("Downloaded file equals initial one")
    public void thenDownloadedFileEqualsInitialOne() {
        steps.filesShouldBeEqual();
    }

    @Then("Upload time took less than '$seconds' seconds")
    public void thenUploadTimeTookLessThan15Seconds(int seconds) {
        steps.uploadTimeShouldTakeLessThan(seconds);
    }

    @Then("Download time tool less than '$seconds' seconds")
    public void thenDownloadTimeToolLessThan(int seconds) {
        steps.downloadTimeShouldTakeLessThan(seconds);
    }

    @Given("a file $name with size of $size Mb")
    @Alias("a differ file $name with size of $size Mb is in the same directory")
    public void givenAFiletesttxtWithSizeOf3Mb(String name, int size) {
        steps.createTestFile(name, size);
    }

    @Given("another file $name isn't in the same directory")
    public void givenAnotherFileIsntInDirectory(String name) {
        //Nothing to do
    }

    @When("I run application with parameters '$parameters'")
    public void whenIRunApplicationWithParameters(String parameters) {
        steps.runApplication(parameters);
    }


    @Then("App notifies me that files are different")
    public void thenAppNotifiesMeThatFilesAreDifferent() {
      steps.checkAppOutput("NOK");
    }

}
