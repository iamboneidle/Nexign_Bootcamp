package crm.crm_service.Services;

import crm.crm_service.CRMExecutors.RequestExecutor;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DataToChangeTariffSenderService {
    private static final String CHANGE_TARIFF_URL = "http://localhost:2002/change-tariff";
    private OkHttpClient client;
    private final RequestExecutor requestExecutor = new RequestExecutor();

    @PostConstruct
    private void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .writeTimeout(40000, TimeUnit.MILLISECONDS)
                .readTimeout(40000, TimeUnit.MILLISECONDS)
                .build();
    }

    public void sendDataToChangeTariff(String json) {
        requestExecutor.execute(json, CHANGE_TARIFF_URL, client);
    }
}
