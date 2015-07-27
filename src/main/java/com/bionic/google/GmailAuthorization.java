package com.bionic.google;


import com.bionic.utils.PropertyLoader;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import jline.internal.TestAccessible;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import static com.bionic.utils.PropertyLoader.getProperty;

/**
 * Created by viktozhu on 7/23/15.
 */
public class GmailAuthorization {

    private String applicationName;
    private java.io.File dataStoreDir;
    private FileDataStoreFactory dataStoreFactory;
    private String pathToClientSecret;
    private HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.MAIL_GOOGLE_COM);

    static {
        try {

        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }


    public GmailAuthorization(String applicationName, String pathToClientSecret) throws IOException, GeneralSecurityException {
        this.applicationName = applicationName;
        this.dataStoreDir = new java.io.File(getProperty("project.path"), "src/main/resources/credentials/gmail-api"+applicationName);
        this.dataStoreFactory = new FileDataStoreFactory(dataStoreDir);
        this.pathToClientSecret = pathToClientSecret;
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public  Credential authorize() throws IOException {
        InputStream in =  new FileInputStream(pathToClientSecret);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(dataStoreFactory)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(clientSecrets.getInstalled().getClientId());
        System.out.println("Credentials saved to " + dataStoreDir.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     * @throws IOException
     */

    public Gmail getGmailService() throws IOException {
        Credential credential = authorize();
        return new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(applicationName)
                .build();
    }

}
