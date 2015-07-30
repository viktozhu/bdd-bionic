package com.bionic.jbehave.gdrive;

import com.bionic.steps.GdriveSteps;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

/**
 * Created by bdd on 7/30/15.
 */
public class GdriveDefinitions {
    @Steps
    GdriveSteps steps;

    @Given("authorized connection to Gdrive")
    @Pending
    public void givenAuthorizedConnectionToGdrive() {
        // PENDING
    }

    @When("I upload <filename> to GDrive with <filesize>")
    @Pending
    public void whenIUploadfilenameToGDriveWithfilesize() {
        // PENDING
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

}
