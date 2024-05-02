package brt.brt_service.Services.Utils;

import brt.brt_service.BRTUtils.RequestExecutor;
import brt.brt_service.Postgres.DAO.Models.Msisdns;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InitialDataPusherService {
    @Autowired
    private StartInfoPusherToPostgresService startInfoPusherToPostgresService;
    @Autowired
    private StartInfoPusherToRedisService startInfoPusherToRedisService;
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    @Autowired
    private RequestExecutor requestExecutor;
    /**
     * URL-адрес контроллера CRM, который при запуске ждет информацию по абонентам и их тарифам.
     */
    private static final String POST_TARIFFS_TO_CRM_URL = "http://localhost:2004/admin/post-tariffs";
    /**
     * Имя админа в сервисе CRM.
     */
    private static final String ADMIN_USERNAME = "admin";
    /**
     * Пароль админа в сервисе CRM.
     */
    private static final String ADMIN_PASSWORD = "admin";
    @PostConstruct
    public void sendDataToCRM() {
        startInfoPusherToPostgresService.pushToPostgres();
        startInfoPusherToRedisService.pushToRedis();
        pushMapToCRM();
    }

    /**
     * Метод отправляющий данные о пользователях и их тарифах в CRM.
     */
    private void pushMapToCRM() {
        Map<String, Long> mapNumberToRateId = new HashMap<>();
        List<Msisdns> msisdns = msisdnsRepository.findAll();
        for (Msisdns msisdn : msisdns) {
            mapNumberToRateId.put(msisdn.getNumber(), msisdn.getRateId());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(mapNumberToRateId);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            requestExecutor.executeWithHeaders(POST_TARIFFS_TO_CRM_URL, body, ADMIN_USERNAME, ADMIN_PASSWORD);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод, который на старте отправляет данные по тарифам в HRS кэш базу данных.
     */
    private void sendRatesToHRSCacheDB() {/* TODO: @PostConstruct */ }
}
