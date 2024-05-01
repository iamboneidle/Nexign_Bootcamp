package crm.crm_service.CRMExecutors;

import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, отправляющий запрос.
 */
public class RequestExecutor {
    /**
     * Логгер для вывода уведомлений.
     */
    private static final Logger LOGGER = Logger.getLogger(RequestExecutor.class.getName());

    /**
     * Метод для отправки запроса.
     *
     * @param json Строка с информацией для отправки.
     * @param url URL-адрес, на который отправляется запрос.
     */
    public void execute(String json, String url, OkHttpClient client) {
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOGGER.log(Level.INFO, "OK: " + Objects.requireNonNull(response.body()).string());
            } else {
                LOGGER.log(Level.SEVERE, "ERROR: " + Objects.requireNonNull(response.body()).string());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
