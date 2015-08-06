package com.bionic.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DriveDownload {
    /**
     * Download a file's content.
     *
     * @return InputStream containing the file's content if successful,
     * {@code null} otherwise.
     */
    public InputStream downloadFile(Drive service, File file) {
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                // uses alt=media query parameter to request content
                return service.files().get(file.getId()).executeMediaAsInputStream();
            } catch (IOException e) {
                // An error occurred.
                e.printStackTrace();
                return null;
            }
        } else {
            // The file doesn't have any content stored on Drive.
            return null;
        }
    }

    public void saveFileToHDD(InputStream stream, String pathWithFileNameToSave) {//pathWithFileNameToSave - target/testTxt.txt
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            // read this file into InputStream
            inputStream = stream;

            // write the inputStream to a FileOutputStream
            outputStream =
                    new FileOutputStream(new java.io.File(pathWithFileNameToSave));

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            System.out.println("OK");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

