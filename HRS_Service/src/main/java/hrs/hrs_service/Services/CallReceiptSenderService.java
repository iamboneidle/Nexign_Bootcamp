package hrs.hrs_service.Services;

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
    public void sendCallReceipt(String json) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(DESTINATION_URL)
                .post(body)
                .build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                LOGGER.log(Level.SEVERE, "ERROR: " + Objects.requireNonNull(response.body()).string() + "\n");
            }
            LOGGER.log(Level.INFO, "OK: " + Objects.requireNonNull(response.body()).string() + "\n");
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()) + "\n");
        }
    }
}