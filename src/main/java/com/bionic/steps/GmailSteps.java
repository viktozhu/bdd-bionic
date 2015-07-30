package com.bionic.steps;

import com.bionic.google.GmailAuthorization;
import com.google.api.services.gmail.Gmail;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by viktozhu on 7/27/15.
 */
public class GmailSteps extends ScenarioSteps {

    private Map<String, Gmail> accounts = new HashMap<String, Gmail>();


    @Step
    public void authorizeInit() {
        accounts.put("bionic.bdd@gmail.com", userAuthorize("project-bdd", "bionic.bdd"));
        accounts.put("bionic.bdd.test@gmail.com", userAuthorize("project-bdd2", "bionic.bdd.test"));
    }

    @Step
    public void userSendsEmail(String from, String to, String subject, String text) {
        sendEmailWith(accounts.get(from), from, to, subject, text);
    }

    @Step
    public void executeAutoResponderOn(String email) {
        //Execute AutoResponder on accounts.get(email);
    }

    @Step
    public void shouldReceiveAutoReply(String account, String from) {
        /* Thread.sleep(7000);
        String query = "from:"+from+" subject:autoPeply is:unread";
        GmailEmailGetter emailGetter = new GmailEmailGetter(accounts.get(account));
        assertThat(emailGetter.listMessagesMatchingQuery(accounts.get(account), "me", query))
                   .isNotEmpty();*/
    }



    private void sendEmailWith(Gmail account, String from, String to, String subject, String text) {
        /*EmailSender emailSender = new EmailSender(account, from);
        emailSender.sendMessage(emailSender.createEmail(to, subject, text));
            */
    }

    private Gmail userAuthorize(String projectName, String email) {
        GmailAuthorization gmailAuthorization = null;
        try {
            gmailAuthorization = new GmailAuthorization(projectName, "src/main/resources/secrets/"+email+".secret.json");
            return gmailAuthorization.getGmailService();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
