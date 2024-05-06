package crm.crm_service.Services;

import crm.crm_service.CRMExecutors.RequestExecutor;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Сервис по отправке данных о пополнении баланса на счете абонента.
 */
@Service
public class DataToPutMoneySenderService {
    /**
     * Клиент.
     */
    private OkHttpClient client;
    /**
     * URL-адрес контроллера BRT, принимающего данные о смене тарифа.
     */
    @Value("${brt.service.url.put-money}")
    private String putMoneyUrl;
    /**
     * Класс, отправляющий запросы.
     */
    private final RequestExecutor requestExecutor = new RequestExecutor();

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
     * Метод передающий данные для отправки в BRT в requestExecutor.
     *
     * @param json Строка с объектом для пополнения балансов на счетах абонентов.
     */
    public void sendDataToPutMoney(String json) {
        requestExecutor.execute(json, putMoneyUrl, client);
    }
}
