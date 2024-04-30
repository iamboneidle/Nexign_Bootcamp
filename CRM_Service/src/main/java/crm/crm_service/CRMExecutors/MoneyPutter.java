package crm.crm_service.CRMExecutors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import crm.crm_service.CRMUtils.DataToPutMoney;
import crm.crm_service.Services.CRMService;
import crm.crm_service.Services.DataToPutMoneySenderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoneyPutter implements Runnable {
    private final CRMService crmService;
    private final DataToPutMoneySenderService dataToPutMoneySenderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MoneyPutter(CRMService crmService, DataToPutMoneySenderService dataToPutMoneySenderService) {
        this.crmService = crmService;
        this.dataToPutMoneySenderService = dataToPutMoneySenderService;
    }

    @Override
    public void run() {
        Map<String, Long> mapNumberToRateId = crmService.getMapNumberToRateId();
        List<String> msisdns = new ArrayList<>(mapNumberToRateId.keySet());
        for (String msisdn : msisdns) {
            float moneyToPut = (float) (Math.random() * 100 + 100);
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
