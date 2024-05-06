package crm.crm_service.Services;

import crm.crm_service.CRMExecutors.RequestExecutor;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Сервис, отправляющий данные о смене тарифа в BRT.
 */
@Service
public class DataToChangeTariffSenderService {
    /**
     * URL-адрес контроллера в BRT для смены тарифа.
     */
    @Value("${brt.service.url.change-tariff}")
    private String changeTariffUrl;
    /**
     * Класс, который отправляет запросы.
     */
    private final RequestExecutor requestExecutor = new RequestExecutor();
    /**
     * Клиент.
     */
    private OkHttpClient client;

    /**
     * Метод инициализирующий клиента на PostConstruct.
     */
    @PostConstruct
    private void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .writeTimeout(40000, TimeUnit.MILLISECONDS)
                .readTimeout(40000, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * Метод отправляющий данные в requestExecutor для отправки в BRT.
     *
     * @param json Строка с данными о смене тарифа.
     */
    public void sendDataToChangeTariff(String json) {
        requestExecutor.execute(json, changeTariffUrl, client);
    }
}
