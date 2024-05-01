package brt.brt_service.BRTUtils;

import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, отправляющий запросы.
 */
@Service
public class RequestExecutor {
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(RequestExecutor.class.getName());
    /**
     * Клиент.
     */
    private OkHttpClient client;

    /**
     * Метод, инициализирующий клиента на PostConstruct.
     */
    @PostConstruct
    public void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .writeTimeout(40000, TimeUnit.MILLISECONDS)
                .readTimeout(40000, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * Метод, отправляющий запрос с заголовочными полями.
     *
     * @param url URL-адрес, на который отправляется запрос.
     * @param body Тело запроса.
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     */
    public void executeWithHeaders(String url, RequestBody body, String username, String password) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", getBasicAuthenticationHeader(username, password))
                .build();
        send(request);
    }

    /**
     * Метод, отправляющий запрос без заголовков.
     *
     * @param url URL-адреса, на который отправляется запрос.
     * @param body Тело запроса.
     */
    public void execute(String url, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        send(request);
    }

    /**
     * Метод, непосредственно отправляющий запрос.
     *
     * @param request Запрос.
     */
    private void send(Request request) {
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

    /**
     * Метод, шифрующий данные для Basic аутентификации в base64.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @return Строка для Basic аутентификации.
     */
    @NotNull
    private String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
