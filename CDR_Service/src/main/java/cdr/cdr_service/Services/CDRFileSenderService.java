package cdr.cdr_service.Services;

import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class CDRFileSenderService {
    private static final String DESTINATION_URL = "http://localhost:2002/catchCDR";
    private static final Logger LOGGER = Logger.getLogger(CDRFileSenderService.class.getName());
    private OkHttpClient client;

    @PostConstruct
    public void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();
    }

    public void sendFile(File file) {
        String fileName = file.toString().substring(file.toString().lastIndexOf("/") + 1);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("text/plain")))
                .addFormDataPart("fileName", fileName)
                .build();

        Request request = new Request.Builder()
                .url(DESTINATION_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                LOGGER.log(Level.SEVERE, "ERROR:" + Objects.requireNonNull(response.body()).string() + "\n");
            } else {
                LOGGER.log(Level.INFO, "OK: " + fileName + " was sent successfully" + "\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()) + "\n");
        }
    }
}
