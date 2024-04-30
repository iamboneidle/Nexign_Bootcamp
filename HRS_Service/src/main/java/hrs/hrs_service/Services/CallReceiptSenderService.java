package hrs.hrs_service.Services;

import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CallReceiptSenderService {
    private static final String DESTINATION_URL = "http://localhost:2002/catch-call-receipt";
    private static final Logger LOGGER = Logger.getLogger(CallReceiptSenderService.class.getName());
    private OkHttpClient client;
    @PostConstruct
    public void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .build();
    }
    public void sendCallReceipt(String json) {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(DESTINATION_URL)
                .post(body)
                .build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                LOGGER.log(Level.SEVERE, "ERROR: " + Objects.requireNonNull(response.body()).string() + "\n");
            } else {
                LOGGER.log(Level.INFO, "OK: " + Objects.requireNonNull(response.body()).string() + "\n");
            }
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()) + "\n");
        }
    }
}
