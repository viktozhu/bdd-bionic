package com.bionic.jbehave;

import com.bionic.google.GmailAuthorization;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import org.jbehave.core.annotations.Given;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by viktozhu on 7/23/15.
 */
public class GoogleDefinitions {

    @Given("authorized connection to gmail")
    public void authorizedConnection() throws IOException {
        PropertyLoader.loadPropertys();
        Gmail service = GmailAuthorization.getGmailService();

        String user = "me";
        ListLabelsResponse listResponse =
                service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.size() == 0) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }
    }
}
