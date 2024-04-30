package brt.brt_service.BRTUtils;

import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RequestExecutor {
    private static final Logger LOGGER = Logger.getLogger(RequestExecutor.class.getName());
    private OkHttpClient client;

    @PostConstruct
    public void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .writeTimeout(40000, TimeUnit.MILLISECONDS)
                .readTimeout(40000, TimeUnit.MILLISECONDS)
                .build();
    }

    public void executeWithHeaders(String DESTINATION_URL, RequestBody body, String ADMIN_USERNAME, String ADMIN_PASSWORD) {

        Request request = new Request.Builder()
                .url(DESTINATION_URL)
                .post(body)
                .addHeader("Authorization", getBasicAuthenticationHeader(ADMIN_USERNAME, ADMIN_PASSWORD))
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                LOGGER.log(Level.SEVERE, "ERROR: " + Objects.requireNonNull(response.body()).string());
            } else {
                LOGGER.log(Level.INFO, "OK: " + Objects.requireNonNull(response.body()).string());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void execute(String DESTINATION_URL, RequestBody body) {

        Request request = new Request.Builder()
                .url(DESTINATION_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                LOGGER.log(Level.SEVERE, "ERROR: " + Objects.requireNonNull(response.body()).string());
            } else {
                LOGGER.log(Level.INFO, "OK: " + Objects.requireNonNull(response.body()).string());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
