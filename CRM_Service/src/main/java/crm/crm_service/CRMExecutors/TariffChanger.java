package crm.crm_service.CRMExecutors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import crm.crm_service.CRMUtils.DataToChangeTariff;
import crm.crm_service.Services.CRMService;
import crm.crm_service.Services.DataToChangeTariffSenderService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Поток, который меняет тарифы абонентам.
 */
@Service
public class TariffChanger implements Runnable {
    /**
     * CRMService.
     */
    private final CRMService crmService;
    /**
     * Сервис по отправке данных о смене тарифов на BRT.
     */
    private final DataToChangeTariffSenderService dataToChangeTariffSenderService;
    /**
     * Объект ObjectMapper для преобразования объектов в Json.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Конструктор класса.
     *
     * @param crmService CRMService.
     * @param dataToChangeTariffSenderService Сервис по отправке данных о смене тарифов на BRT.
     */
    public TariffChanger(CRMService crmService, DataToChangeTariffSenderService dataToChangeTariffSenderService) {
        this.crmService = crmService;
        this.dataToChangeTariffSenderService = dataToChangeTariffSenderService;
    }

    /**
     * Задача потоку, в которой он получает Map с информацией о тарифах абонентов, получает список номеров,
     * случайным образом тасует и выбирает от 1 до 3 первых абонентов для смены тарифа, затем меняет его и
     * передает данные об этом в DataToChangeTariffSenderService.
     */
    @Override
    public void run() {
        Map<String, Long> mapNumberToRateId = crmService.getMapNumberToRateId();
        List<String> keyList = new ArrayList<>(mapNumberToRateId.keySet());
        Collections.shuffle(keyList);
        int numbersToPick = (int) (Math.random() * 3 + 1);
        for (int i = 0; i < numbersToPick; i++) {
            String number = keyList.get(i);
            Long rate = mapNumberToRateId.get(keyList.get(i));
            DataToChangeTariff dataToChangeTariff = new DataToChangeTariff(number, rate == 11L ? 12L : 11L);
            crmService.getMapNumberToRateId().replace(number, rate == 11L ? 12L : 11L);
            try {
                String json = objectMapper.writeValueAsString(dataToChangeTariff);
                dataToChangeTariffSenderService.sendDataToChangeTariff(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
