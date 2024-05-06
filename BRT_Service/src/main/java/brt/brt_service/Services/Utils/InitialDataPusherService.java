package brt.brt_service.Services.Utils;

import brt.brt_service.BRTUtils.RateData;
import brt.brt_service.BRTUtils.RequestExecutor;
import brt.brt_service.Postgres.DAO.Models.Msisdns;
import brt.brt_service.Postgres.DAO.Models.Rates;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import brt.brt_service.Postgres.DAO.Repository.RatesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис, который отправляет данные во все инстанции при запуске.
 */
@Service
public class InitialDataPusherService {
    /**
     * Сервис, который заполняет базу данных сервиса на старте.
     */
    @Autowired
    private StartInfoPusherToPostgresService startInfoPusherToPostgresService;
    /**
     * Сервис, который отправляет данные о пользователях и остатках их минут в Redis на старте.
     */
    @Autowired
    private StartInfoPusherToRedisService startInfoPusherToRedisService;
    /**
     * Репозиторий Msisdns.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    /**
     * Репозиторий Rates.
     */
    @Autowired
    private RatesRepository ratesRepository;
    /**
     * Класс, отправляющий запросы.
     */
    @Autowired
    private RequestExecutor requestExecutor;
    /**
     * URL-адрес контроллера CRM, который при запуске ждет информацию по абонентам и их тарифам.
     */
    @Value("${crm.service.url.post-tariffs}")
    private String postTariffsToCrmUrl;
    /**
     * URL-адрес контроллера HRS, который при запуске ждет информацию о тарифах для Redis.
     */
    @Value("${hrs.service.url.post-rate-data}")
    private String postRatesToHrsRedis;
    /**
     * Имя админа в сервисе CRM.
     */
    @Value("${crm.admin.username}")
    private String adminUsername;
    /**
     * Пароль админа в сервисе CRM.
     */
    @Value("${crm.admin.password}")
    private String adminPassword;

    /**
     * Метод, вызывающий при запуске сервиса и вызывающий другие метода для занесения нужных данных.
     */
    @PostConstruct
    public void sendDataToCRM() {
        startInfoPusherToPostgresService.pushToPostgres();
        startInfoPusherToRedisService.pushToRedis();
        pushMapToCRM();
        sendRatesToHRSRedis();
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

            requestExecutor.executeWithHeaders(postTariffsToCrmUrl, body, adminUsername, adminPassword);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод, который на старте отправляет данные по тарифам в HRS кэш базу данных.
     */
    private void sendRatesToHRSRedis() {
        List<Rates> rates = ratesRepository.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Rates rate: rates) {
            RateData rateData = new RateData(
                    rate.getId(),
                    rate.getRateName(),
                    rate.getStartCost(),
                    rate.getMinLimit(),
                    rate.getOutcomingCallsCostServiced(),
                    rate.getOutcomingCallsCostOthers(),
                    rate.getIncomingCallsCostServiced(),
                    rate.getIncomingCallsCostOthers()
            );

            try {
                String json = objectMapper.writeValueAsString(rateData);
                RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

                requestExecutor.execute(postRatesToHrsRedis, body);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
