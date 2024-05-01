package brt.brt_service.Services;

import brt.brt_service.BRTUtils.CallReceipt;
import brt.brt_service.BRTUtils.RequestExecutor;
import brt.brt_service.DAO.Models.Msisdns;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
import brt.brt_service.Services.Handlers.CDRFileHandlerService;
import brt.brt_service.Services.Handlers.CallReceiptHandlerService;
import brt.brt_service.Services.Utils.StartInfoPusherService;
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
public class BRTService {
    /**
     * Сервис, обрабатывающий информацию о звонках.
     */
    @Autowired
    private CDRFileHandlerService callRecordsHandlerService;
    /**
     * Сервис, обрабатывающий информацию о чеках по звонкам.
     */
    @Autowired
    private CallReceiptHandlerService callReceiptHandlerService;
    /**
     * Репозиторий абонентов.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    /**
     * Класс, отправляющий запросы.
     */
    @Autowired
    private RequestExecutor requestExecutor;
    /**
     * Сервис, который заполняет базу данных сервиса на старте, если та пуста.
     */
    @Autowired
    private StartInfoPusherService startInfoPusherService;
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

    /**
     * Метод, который передает с контроллера CDR файл на обработку в CallRecordsHandlerService.
     *
     * @param callDataRecord Содержание CDR файла.
     */
    public void handleCDRFile(String callDataRecord) {
        callRecordsHandlerService.makeCallRecords(callDataRecord);
    }

    /**
     * Метод, который передает с контроллера чек по звонку в CallReceiptHandlerService.
     *
     * @param callReceipt Объект с данными чека по звонку.
     */
    public void handleCallReceipt(CallReceipt callReceipt) {
        callReceiptHandlerService.validateCallReceipt(callReceipt);
    }

    /**
     * Метод, который на старте отправляет данные по тарифам в HRS кэш базу данных.
     */
    private void sendRatesToHRSCacheDB() {/* TODO: @PostConstruct */ }

    /**
     * Метод, который вызывает startInfoPusherService.pushToDB() на старте сервиса и отправляет
     * данные по абонентам и их тарифам в CRM.
     *
     * @throws JsonProcessingException В случае ошибки преобразования объекта в Json.
     */
    @PostConstruct
    public void sendDataToCRM() throws JsonProcessingException {
        startInfoPusherService.pushToDB();
        Map<String, Long> mapNumberToRateId = new HashMap<>();
        List<Msisdns> msisdns = msisdnsRepository.findAll();
        for (Msisdns msisdn : msisdns) {
            mapNumberToRateId.put(msisdn.getNumber(), msisdn.getRateId());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(mapNumberToRateId);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        requestExecutor.executeWithHeaders(POST_TARIFFS_TO_CRM_URL, body, ADMIN_USERNAME, ADMIN_PASSWORD);
    }
}
