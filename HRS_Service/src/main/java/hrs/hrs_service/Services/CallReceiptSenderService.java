package hrs.hrs_service.Services;

import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервис, занимающийся отправкой чеков в BRT.
 */
@Service
public class CallReceiptSenderService {
    /**
     * URL end-point'а в BRT, принимающего чеки.
     */
    @Value("${brt.service.url.post-call-receipt}")
    private String postCallReceiptUrl;
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(CallReceiptSenderService.class.getName());
    /**
     * Клиент, осуществляющий отправку.
     */
    private OkHttpClient client;

    /**
     * Метод, инициализирующий клиента на PostConstruct.
     */
    @PostConstruct
    public void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .build();
    }

    /**
     * Метод, отправляющий Json с чеком в BRT. Создает requestBody, request, отправляет, получает response и выводит
     * уведомление.
     *
     * @param json Чек по звонку.
     */
    public void sendCallReceipt(String json) {
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(postCallReceiptUrl)
                .post(requestBody)
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
}
