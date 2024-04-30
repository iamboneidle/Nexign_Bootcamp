package crm.crm_service.Services;

import crm.crm_service.CRMExecutors.RequestExecutor;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DataToPutMoneySenderService {
    private static final String PUT_MONEY_URL = "http://localhost:2002/put-money-on-accounts";
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

    public void sendDataToPutMoney(String json) {
        requestExecutor.execute(json, PUT_MONEY_URL, client);
    }
}
