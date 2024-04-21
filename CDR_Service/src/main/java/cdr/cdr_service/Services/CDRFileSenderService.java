package cdr.cdr_service.Services;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class CDRFileSenderService {
    private static final String DESTINATION_URL = "http://localhost:2002/catchCDR";
    private static final Logger LOGGER = Logger.getLogger(CDRFileSenderService.class.getName());
    public void sendFile(File file) {
        OkHttpClient client = new OkHttpClient();
        String fileName = file.toString().substring(file.toString().lastIndexOf("/") + 1);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("text/plain"), file))
                .addFormDataPart("fileName", fileName)
                .build();

        Request request = new Request.Builder()
                .url(DESTINATION_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("error during sending");
                LOGGER.log(Level.SEVERE, "ERROR:" + Objects.requireNonNull(response.body()).string() + "\n");
            }
            LOGGER.log(Level.INFO, "OK: " + fileName + " was sent successfully" + "\n");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()) + "\n");
        }
    }
}
