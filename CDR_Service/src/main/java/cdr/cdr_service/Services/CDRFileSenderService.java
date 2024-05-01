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


/**
 * Сервис, который отвечает за отправку CDR файлов на BRT.
 */
@Service
public class CDRFileSenderService {
    /**
     * URL-адрес end-point'а BRT, ожидающего нового файла.
     */
    private static final String POST_CDR_URL = "http://localhost:2002/post-CDR";
    /**
     * Логгер для выводы уведомлений.
     */
    private static final Logger LOGGER = Logger.getLogger(CDRFileSenderService.class.getName());
    /**
     * HTTP-клиент.
     */
    private OkHttpClient client;

    /**
     * Метод, инициализирующий клиента.
     */
    @PostConstruct
    public void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Метод, отправляющий request в BRT. Создается fileName, requestBody, request, затем идет попытка отправки и логирование
     * ответа.
     *
     * @param file CDR файл, который мы отправляем в BRT.
     */
    public void sendFile(File file) {
        String fileName = file.toString().substring(file.toString().lastIndexOf("/") + 1);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("text/plain")))
                .addFormDataPart("fileName", fileName)
                .build();

        Request request = new Request.Builder()
                .url(POST_CDR_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                LOGGER.log(Level.SEVERE, "ERROR:" + Objects.requireNonNull(response.body()).string());
            } else {
                LOGGER.log(Level.INFO, "OK: " + fileName + " was sent successfully");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
