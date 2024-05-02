package crm.crm_service.CRMExecutors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import crm.crm_service.CRMUtils.DataToPutMoney;
import crm.crm_service.Services.CRMService;
import crm.crm_service.Services.DataToPutMoneySenderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Поток, который кладет деньги на счета абонентов.
 */
public class MoneyPutter implements Runnable {
    /**
     * CRMService.
     */
    private final CRMService crmService;
    /**
     * Сервис, который отправляет данные о пополнении счета абонента в BRT.
     */
    private final DataToPutMoneySenderService dataToPutMoneySenderService;
    /**
     * Объект ObjectMapper для преобразования объекта в Json.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Минимум средств, сколько абонент может положить себе на счет.
     */
    private static final float MONEY_TO_PUT_BOTTOM_BORDER = 100F;
    /**
     * Число, которое прибавляется к MONEY_TO_PUT_BOTTOM_BORDER, чтобы получить максимум средств, которые абонент
     * может положить себе на счет.
     */
    private static final float MONEY_TO_PUT_TOP_BORDER = 100F;

    /**
     * Конструктор класса.
     *
     * @param crmService CRMService.
     * @param dataToPutMoneySenderService Сервис по отправке данных о пополнении счетов абонентов на BRT.
     */
    public MoneyPutter(CRMService crmService, DataToPutMoneySenderService dataToPutMoneySenderService) {
        this.crmService = crmService;
        this.dataToPutMoneySenderService = dataToPutMoneySenderService;
    }

    /**
     * Задача потоку, в которой он получает Map с абонентами, берет оттуда номера, и генерирует
     * количество денег для пополнения счета, затем передает в DataToPutMoneySenderService.
     */
    @Override
    public void run() {
        Map<String, Long> mapNumberToRateId = crmService.getMapNumberToRateId();
        List<String> msisdns = new ArrayList<>(mapNumberToRateId.keySet());
        for (String msisdn : msisdns) {
            float moneyToPut = (float) (Math.random() * MONEY_TO_PUT_BOTTOM_BORDER + MONEY_TO_PUT_TOP_BORDER);
            DataToPutMoney dataToPutMoney = new DataToPutMoney(msisdn, moneyToPut);
            try {
                String json = objectMapper.writeValueAsString(dataToPutMoney);
                dataToPutMoneySenderService.sendDataToPutMoney(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
