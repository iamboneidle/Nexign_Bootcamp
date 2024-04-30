package crm.crm_service.CRMUtils;

import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestExecutor {
    private static final Logger LOGGER = Logger.getLogger(RequestExecutor.class.getName());

    public void execute(String json, String url, OkHttpClient client) {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOGGER.log(Level.INFO, "OK: " + Objects.requireNonNull(response.body()).string() + "\n");
            } else {
                LOGGER.log(Level.SEVERE, "ERROR: " + Objects.requireNonNull(response.body()).string() + "\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
